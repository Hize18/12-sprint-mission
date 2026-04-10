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
  - RequiredArgsConstructor을 통해 DI 자동 주입.

- 프로젝트 마이그레이션을 하면서 리펙토링 및 가독성 확보
- 실행파일에 Spring context를 이용하여 service DI시행.
- String.equals를 Objects.equals로 null-safe 추가.