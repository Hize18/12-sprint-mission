# UserService

## Methods
- boolean isUniqueUsername(String username)
- boolean isUniqueEmail(String email)
- User save(User user)
- User findByUsername(String username)
- User findByEmail(String email)
- List<User> findByNickname(String nickname)
- List<User> findAll()
- void update(UUID srcUserId, UUID dstUserId, User userData)
- void delete(UUID srcUserId, UUID dstUserId)

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
- Channel findByHandle(String handle)
- List<Channel> findByOwner(UUID ownerId)
- List<Channel> findByName(String name)
- List<Channel> findAll()
- void update(UUID userId, UUID channelId, String name)
- void delete(UUID userId, UUID channelId)

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
- void update(UUID userId, UUID messageId, String content)
- void delete(UUID userId, UUID messageId)

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

