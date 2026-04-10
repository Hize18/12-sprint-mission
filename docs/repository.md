# Repository 설계 및 구현

## File*Repository

- FileIO를 통해 데이터를 파일로 직렬화하여 저장, 애플리케이션 재시작 시에도 데이터를 유지하도록 구현.
- 생성자에서 파일을 1회 로드하여 Map에 데이터를 유지한다.
- find 계열은 Map을 기준으로 수행한다.
- save 및 delete 시 파일에 직렬화하여 영속성을 유지한다.

### 데이터 일관성 처리

- save와 delete 수행 시, 기존 데이터를 복사한 임시 Map에 먼저 반영한다.
- 파일 저장이 성공한 경우에만 실제 내부 Map에 반영한다.

---

## Repository 인터페이스 설계

### UserRepository

- User save(User user)
- Optional<User> findById(UUID id)
- Optional<User> findByUsername(String username)
- List<User> findAll()
- void delete(UUID id)

### MessageRepository

- Message save(Message message)
- Optional<Message> findById(UUID id)
- List<Message> findByChannelId(UUID channelId)
- List<Message> findByUserId(UUID userId)
- List<Message> findAll()
- void delete(UUID id)

### ChannelRepository

- Channel save(Channel channel)
- Optional<Channel> findById(UUID id)
- Optional<Channel> findByName(String name)
- List<Channel> findByOwnerId(UUID ownerId)
- List<Channel> findAll()
- void delete(UUID id)

---

## 조회 방식

- 단일 객체 조회(findById, findByUsername 등)는 Stream API를 사용하여 가독성을 높였다.
- 다중 객체 조회(findAll, findByChannelId 등)는 정렬 기준(updatedAt)을 명확히 하기 위해 리스트로 변환 후 정렬하여 반환한다.

---

## 공통 설계 특징

- 엔티티 간 직접 참조 대신 UUID 기반 식별자를 사용하여 객체 간 결합도를 낮췄다.
- Repository는 순수한 데이터 저장 및 조회 역할만 담당한다.
- 비즈니스 로직 및 데이터 조합은 Service 계층에서 수행하도록 책임을 분리하였다.

---

## Spring 마이그레이션 고려

- @Repository를 통해 스프링 컨테이너에 Bean으로 등록된다.
- @ConditionalOnProperty를 사용하여 설정 값에 따라 JCF 또는 File Repository 구현체가 선택되도록 구성하였다.
- 이를 통해 구현체 변경을 코드 수정 없이 설정으로 제어할 수 있도록 설계하였다.