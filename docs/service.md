# UserService

## Methods

- boolean isUniqueUsername(String username)
- boolean isUniqueEmail(String email)
- User save(User user)
- User findById(UUID id)
- User findByUsername(String username)
- List<User> findAll()
- boolean update(UUID srcUserId, UUID dstUserId, User userData)
- boolean delete(UUID srcUserId, UUID dstUserId)

## Design Notes

- username, email 중복 방지를 위해 isUnique 메서드 사용
- username으로 단일 조회
- update, delete
  - 1번 parm - 로그인한 회원, 2번 parm - 변경하려는 객체, 3번 parm, 변경하려는 값
  - 1번의 parm와 2번 parm의 소유자를 비교하여 권한 검증

# ChannelService

## Methods

- boolean isUniqueName(String name)
- Channel save(Channel channel)
- Channel findById(UUID id)
- Channel findByName(String name)
- List<Channel> findByOwner(UUID ownerId)
- List<Channel> findAll()
- boolean update(UUID userId, UUID channelId, String name)
- boolean delete(UUID userId, UUID channelId)

## Design Notes

- name으로 단일 조회
- owner 중복 가능 → List 반환
- update, delete
  - 1번 parm - 로그인한 회원, 2번 parm - 변경하려는 객체, 3번 parm, 변경하려는 값
  - 1번의 parm와 2번 parm의 소유자를 비교하여 권한 검증

# MessageService

## Methods

- Message save(Message message)
- Message findById(UUID id)
- List<Message> findByChannelId(UUID channelId)
- List<Message> findByUserId(UUID userId)
- List<Message> findAll()
- boolean update(UUID userId, UUID messageId, String content)
- boolean delete(UUID userId, UUID messageId)

## Design Notes

- update, delete
  - 1번 parm - 로그인한 회원, 2번 parm - 변경하려는 객체, 3번 parm, 변경하려는 값
  - 1번의 parm와 2번 parm의 소유자를 비교하여 권한 검증

### 추가 정리

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

---

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

### 간단 정리
- 최초 생성자 실행시 지정한 경로에 있는 파일을 체크하여 map으로 데이터 저장.
- 모든 메서드마다 파일 저장 및 로드가 아닌, save나 delete일 경우에만 저장.
- 파일 저장이 아닐 경우 메서드에서 map을 이용하여 동작

---

## Basic*Service

- UserService를 구현하여 사용
- 비즈니스 로직 담당 (검증, 중복 검사, 권한 확인 등)
- save, find, update, delete의 실제 데이터 처리는 Repository에 위임

---

## Spring 마이그레이션 후

## Basic*Service

- @Service 어노테이션 선언으로 bean 등록
- @RequiredArgsConstructor로 UserRepository의 DI 시행.
- repository에서 반환한 optional 값을 orElseThorw로 예외처리.
- 간단한 처리문을 stream으로 가독성 확보.