## Domain Model

### User
- UUID id
- String username (unique)
- String email (unique)
- String password
- Instant createdAt
- Instant updatedAt

### Channel
- UUID id
- UUID ownerId
- String name (unique)
- Instant createdAt
- Instant updatedAt

### Message
- UUID id
- UUID channelId
- UUID userId
- String content
- Instant createdAt
- Instant updatedAt

---

## 공통 메서드

- copyOf(Object)
- update(...)

### copyOf

copyOf는 객체 복사를 위한 메서드, 내부적으로 복사 생성자(Object를 인자로 받는 생성자)를 호출하여 동일한 값을 가진 새로운 객체를 생성 후 반환

공통 필드인 UUID id와 createdAt은 final로 선언되어 있기 때문에 직접 생성 후 전달이 불가능함.

---

## 설계 의도

- 객체 수정 및 삭제 시 검증을 위해 ownerId(userId)를 기준으로 소유권을 확인한다.
- 기존에는 객체를 직접 참조하는 강한 결합 구조였으나, UUID를 통해 참조하도록 변경하여 결합도를 낮췄다.