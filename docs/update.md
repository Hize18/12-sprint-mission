<details>
<summary style="font-size: 20px; font-weight: bold;">Sprint 3</summary>

### Sprint 3

- Sprint 2에서 스프링 프로젝트로 변경.
- docs를 현재 프로젝트 기준으로 최신화.
- 멘토님의 첨삭을 참고하여 기존 코드 수정 및 리펙토링 진행.
- 엔티티
    - 복사 생성자는 그대로 유지하고 copyOf로 생성 의도 명확히 전달.
    - 객체를 직접 참조하지 않고 UUID를 참조해서 약한 결합으로 수정.
    - 엔티티의 필요없는 필드 및 메소드 제거.(단순화)
    - 생성, 수정시간을 Instant로 수정.
    - getter 어노테이션 추가.
    - 도메인 추가(ReadStatus, UserStatus, BinaryContent)
    - DTO추가에 따른 엔티티의 toString 부분 제거.

- DTO
    - 기존 서비스와 컨트롤러 사이의 매개체를 엔티티에서 DTO로 변경
    - 요구사항에 따른 createDTO, updateDTO, responseDTO등을 생성

- 레포지토리
    - find의 반환값을 optional로 변경.(null 처리 안정성 확보)
    - 단일 조회 메서드를 선언형으로 변경.
    - @Repository과 @ConditionalOnProperty 사용.(File* 계열만)
    - @ConditionalOnProperty을 통해 사용하는 repo 타입 지정.

- 서비스
    - 다른 객체를 참조하는 타입의 경우 해당 객체 존재 체크 추가.
    - find 계열 중 사용하지 않는 메소드 제거.
    - isUnique 계열은 noneMatch로 변경.
    - @Service와 @RequiredArgsConstructor 사용.(Basic* 계열만)
    - Service 패키지의 JCF와 File 제거(스프링 DI로 repo의 저장 방식 선택)
    - RequiredArgsConstructor을 통해 DI 자동 주입.
    - DTO 도입에 따른 파라미터 및 반환값 변경.
    - delete 시 연관 도메인까지 함께 정리하는 정책 적용.
    - DTO 검증과 service 검증을 분리.

- 프로젝트 마이그레이션을 하면서 리펙토링 및 가독성 확보
- 실행파일에 Spring context를 이용하여 service DI시행.
- String.equals를 Objects.equals로 null-safe 추가.

</details>

<details>
<summary style="font-size: 20px; font-weight: bold;">Sprint 4</summary>

### Sprint 4

- sprint 4의 요구 사항인 엔드포인트 구현.
- 멘토님의 리뷰를 참고하여 기존 코드 수정
    - RequestMapping을 이용하여 get과 post, put, delete 엔드포인트 구현
    - postman으로 요청값을 받기 위해 restController로 구현.
    - 첨부파일을 받는 경우를 위해 MultipartFile를 사용.(create만 사용.)
    - 전역 예외 처리를 위해 globalException 구현

---

- 수정 사항
    - DTO
        - 일관성을 위해 네이밍 수정
        - 심화 요구사항을 실행하기 위해 필드명 수정 및 필드 추가

    - 엔티티
        - 일관성을 위해 binaryContent의 필드명 수정.

    - 레포지토리
        - 경로를 하드코딩이 아닌 @Value를 통해 주입
            - 그에 따른 path를 생성자에서 초기화
        - 객체를 사용하지 않는 존재 체크의 경우 boolean으로 존재 체크로 변경.

    - 서비스
        - 변수나 필드명 축약하지 않고 기존 단어 유지
        - 내부 검증만 하는 메서드를 private로 전환
        - optional의 경우 조건문으로 예외 처리가 아닌 orElse로 수정.

</details>

<details>
<summary style="font-size: 20px; font-weight: bold;">Sprint 5</summary>

### Sprint 5

- 코드 컨벤션을 Google Java Style로 변경
- ReentrantLock를 통한 원자성 확보
- 주어진 API 스펙에 맞게 재구현
- 멘토님의 리뷰를 참고하여 코드 수정
    - 컨트롤러의 파라미터중 @Parameter -> @RequestParam으로 수정
    - Custom Exception 추가 및 httpStatus 세분화
    - GlobalException의 단순 출력부분을 @Slf4j를 사용하여 로깅 부분 수정
    - 컨트롤러에서 반환하는 status 수정
    - 패키지명 수정

- Interceptor/ArgumentResolver 관련
    - 기존에는 객체 삭제 시 권한 검증을 위해 owner 필드를 사용했으나
      현재 요구사항 및 API 스펙에서는 별도의 인증/권한 로직이 필요하지 않음.
    - 또한 login API는 단순히 User를 반환하는 구조이므로, session 기반 처리 역시 제거.
    - 이에 따라 Interceptor/ArgumentResolver는 적용하지 않았으며,
      추후 인증/권한 요구사항이 추가될 경우 해당 구조로 리팩토링 예정.

---

- 수정 사항
    - Dto
        - 응답 DTO를 API 요구 스펙에 맞게 변경
        - 쓰지 않는 Dto 제거
        - 요구하는 필드 추가 및 네이밍 변경

    - 엔티티
        - 요구하는 필드 추가 및 네이밍 변경

    - 예외
        - API 요청 검증 실패 시 400 Bad Request 반환하도록 예외 처리 수정
        - CustomException 추가(NotFound, Duplicate, Unauthorized)
        - status 코드에 따라 http status 세분화

    - 레포지토리
        - ReentrantLock를 사용

    - 서비스
        - 반환값을 API 요구 스펙에 따라 DTO에서 엔티티로 변경
        - API 스펙에 없는 사용하지 않는 메서드 삭제

</details>

<details>
<summary style="font-size: 20px; font-weight: bold;">Sprint 6</summary>

### Sprint 6

- PostgreSQL 연동을 위한 Spring Data JPA 기반 Repository 구성
- 기존 File/JCF Repository 기반 구조를 JPA 기반 DB 연동 구조로 변경
- 엔티티 중복 필드를 줄이기 위해 BaseEntity를 구성하고 상속 구조 적용
- JPA 연관관계 매핑을 통해 엔티티 간 관계 정의
- API 응답에서 Entity 직접 노출을 줄이기 위해 DTO 도입
- BinaryContent의 bytes 데이터를 DB에 저장하지 않고 별도 Storage로 분리
- 메시지 목록 조회에 페이징 적용

- 심화
    - N+1 완화를 위해 entitygraph 혹은 fetch join 적용
    - 페이징 방식에서 슬라이스방식으로 변경
    - transactional에 readonly 적용
    - MapStruct 적용

</details>
