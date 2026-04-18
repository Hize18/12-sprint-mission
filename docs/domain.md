## Domain Model

### User
- UUID id
- UUID profileId (BinaryContent 참조)
- String username (unique)
- String email (unique)
- String password
- Instant createdAt
- Instant updatedAt

### Channel
- UUID id
- UUID ownerId
- ChannelType channelType (PUBLIC / PRIVATE)
- String name (PUBLIC일 때만 사용)
- Instant createdAt
- Instant updatedAt

### Message
- UUID id
- UUID channelId
- UUID userId
- String content
- List<UUID> attachmentIds (BinaryContent 참조)
- Instant createdAt
- Instant updatedAt

### BinaryContent(immutable)
- UUID id
- String fileName
- String contentType
- byte[] content
- Instant createdAt

### ReadStatus
- UUID id
- UUID userId
- UUID channelId
- Instant createdAt
- Instant updatedAt

### UserStatus
- UUID id
- UUID userId
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
- BinaryContent - 파일이나 메시지등을 첨부 파일로 하는 경우에 대한 엔티티이다.
- User의 프로필사진일 경우 UUID로 참조, Message의 다중 참조일 경우 List로 전달한다.(List가 비어있을 경우 없는 것.)
- ReadStatus - 현재 privateChannel에만 생성 됨. 마지막으로 메시지를 읽은 시간을 저장
- UserStatus - 유저의 온라인 여부에 대한 엔티티로, 현재 시간 5분 이내일 경우 온라인.