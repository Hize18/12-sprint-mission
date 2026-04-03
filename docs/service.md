# UserService

## Methods
- boolean isUniqueUsername(String username)
- boolean isUniqueEmail(String email)
- User save(User user)
- User findById(UUID id)
- User findByUsername(String username)
- List<User> findByNickname(String nickname)
- List<User> findAll()
- boolean update(UUID srcUserId, UUID dstUserId, User userData)
- boolean delete(UUID srcUserId, UUID dstUserId)

## Design Notes
- username, email 중복 방지를 위해 isUnique 메서드 사용
- 단일 조회는 username, email 기준
    - 실제 사용은 username으로
- nickname은 중복 허용 → List 반환
- update
    - 요청 userId와 channel.owner 비교
    - 소유자만 수정/삭제 가능
    - 문제가 있을 경우 throw로 예외 전달

# ChannelService

## Methods
- boolean isUniqueHandle(String handle)
- String createHandle(String name)
- Channel save(Channel channel)
- Channel findById(UUID id)
- Channel findByHandle(String handle)
- List<Channel> findByOwner(UUID ownerId)
- List<Channel> findByName(String name)
- List<Channel> findAll()
- boolean update(UUID userId, UUID channelId, String name)
- boolean delete(UUID userId, UUID channelId)

## Design Notes
- handle은 unique 값 → isUniqueHandle로 검증
- createHandle -> handle의 unique 보장
- 단일 조회는 handle 기준
- owner, name은 중복 가능 → List 반환
- update, delete
    - 요청 userId와 channel.owner 비교
    - 소유자만 수정/삭제 가능
    - 문제가 있을 경우 throw로 예외 전달

# MessageService

## Methods
- Message save(Message message)
- Message findById(UUID id)
- List<Message> findByChannelId(UUID channelId)
- List<Message> findByUserId(UUID userId)
- List<Message> findByKeyword(String keyword)
- List<Message> findAll()
- boolean update(UUID userId, UUID messageId, String content)
- boolean delete(UUID userId, UUID messageId)

## Design Notes
- findByChannelId
    - 특정 채널에 속한 메시지 조회
- findByUserId
    - 특정 유저가 작성한 메시지 조회
- findByKeyword
    - 채널명, 유저명, 메시지 내용 기준 contains 검색
- update, delete
    - 요청 userId와 channel.owner 비교
    - 소유자만 수정/삭제 가능
    - 문제가 있을 경우 throw로 예외 전달


###  추가 정리

- ArrayList와 HashMap 두개를 같이 쓰지 않고 여기서는 Map만 사용

- Map의 키로는 unique한 id를 사용함

- Map의 성능은 findById를 사용하는 update나 delete에서 더 효과적으로 나타남

- 나머지 find 계열 메소드의 경우 List와 HashMap 모두 시간복잡도는 동일 (O(n))

- 대신 Map의 경우 순서 보장이 안됨

- 기본적으로 HashMap으로 관리하고, find로 탐색 시 List로 변환하여  
  `sort(updatedAt)` 기준으로 정렬 후 전달

- null 에러를 방지하기 위해 서비스 메소드 시작 부분에서 `IllegalArgumentException`을 throw하여 검증 수행

- 실행부에서는 try-catch를 사용하여 예외를 처리하고, 에러 메시지를 출력하도록 구성

- try-catch 로직은 별도의 메소드로 분리하여 재사용성을 고려



Service를 jcf -> file로 변경
기존 로직은 그대로 활용.
각 객체마다 저장시 로드, 수정 및 삭제를 쉽게 할 수 있음.
하지만 service에서 map을 활용하는 방법을 사용하기 위해 하나의 파일로 조작.
저장 방법은 각 service의 map 마다 하나의 파일로 저장 (*.ser)
data/
  user.ser
  channel.ser
  message.ser

map은 그대로 유지(repo로 변경시 분리) 및 map을 이용하여 find 사용(매번 load시 낭비 심함)
생성자로 *.ser를 불러와서 map을 load.
save는 마지막에 map에 저장후 map을 user.ser로 저장(있으면 덮어쓰기)
find는 map을 이용한 로직 그대로
update는 마지막에 map 그대로 저장
delete도 마지막에 map 그대로 저장


## File 기반 Service 설계

- Service를 JCF → File 기반으로 변경
- 기존 비즈니스 로직은 그대로 활용
- Service에서 Map을 유지하면서 File IO를 통해 데이터 영속화 처리

### 저장 방식

- 각 도메인별 Map을 하나의 파일로 저장 (*.ser)
```
└data
    user.ser
    channel.ser
    message.ser
```

- Map 전체를 직렬화하여 파일에 저장
- 하나의 파일을 해당 도메인의 전체 데이터 저장소로 사용

### 동작 방식

- Map은 그대로 유지 (추후 Repository로 분리 예정)
- find는 Map을 이용하여 처리 (매번 load 시 비효율적이므로 메모리 사용)

#### 생성자
- *.ser 파일을 읽어서 Map으로 load
- 파일이 없으면 빈 Map으로 초기화

#### save
- Map에 데이터 저장
- 이후 Map 전체를 파일에 저장 (기존 파일 덮어쓰기)

#### find
- 기존 Map 기반 조회 로직 그대로 사용

#### update
- Map 데이터 수정 후 전체 Map을 파일에 저장

#### delete
- Map에서 데이터 제거 후 전체 Map을 파일에 저장

---
## Basic*Service
- UserService를 구현하여 사용
- 생성자에서 JCF 또는 File Repository를 주입받아 데이터 처리 방식 결정
- 비즈니스 로직 담당 (검증, 중복 검사, 권한 확인 등)
- Optional 대신 null 기반 검증 사용
- save, find, update, delete의 실제 데이터 처리는 Repository에 위임

- 기존 서비스 로직은 유지하고, 저장 방식만 변경
- Application에서는 Basic*Service를 사용하여 동일한 방식으로 실행 가능