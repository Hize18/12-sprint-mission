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