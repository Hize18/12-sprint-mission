## User

### 필드
- UUID id
- String username (unique)
- String email (unique)
- String password
- String nickname
- Long createdAt
- Long updatedAt

### 설계 메모
- id는 생성 시 자동 생성
- username, email은 unique 키
- nickname은 중복 허용 (표시용 이름)

---

## Channel

### 필드
- UUID id
- User owner
- String name
- String handle (unique)
- Long createdAt
- Long updatedAt

### 설계 메모
- 채널 생성 시 User 참조
- handle은 생성 시 name 뒤에 4자리 숫자를 붙여 자동 생성해 unique
- User를 기준으로 수정 및 삭제 권한을 검증

---

## Message

### 필드
- UUID id
- Channel channel
- User user
- String content
- Long createdAt
- Long updatedAt

### 설계 메모
- Message는 Channel과 User에 의존
- 메시지 생성 시 채널과 작성자를 함께 저장
- 권한 검증 시 작성자(User)를 기준으로 판단 가능