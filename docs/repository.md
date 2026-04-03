## Repository 설계 정리

Repository는 저장소에 접근하기 위한 인터페이스이다.  
주된 역할은 도메인 객체의 저장, 조회, 삭제 등 데이터 접근을 추상화하는 것이다.

현재 Repository 구현은 두 가지 방식으로 나뉜다.

- JCF Repository
    - 메모리에서 데이터를 관리
    - 구현이 단순하고 테스트가 쉽지만, 프로그램이 종료되면 데이터가 사라진다.

- File Repository
    - 객체를 직렬화하여 파일에 저장하고 다시 읽어오는 방식이다.
    - 영속성

null 체크, 중복 검사, 권한 확인, 연관 관계 검증 등의 로직은
별도의 Basic Service 계층에서 처리한다.

즉 역할은 다음과 같이 나뉜다.

- Repository
    - 데이터 저장소 접근 담당
    - save, find 계열, delete 등의 기능 제공
    - 구현체에 따라 JCF / File 방식으로 동작

- Basic Service
    - 비즈니스 로직 담당
    - 입력 검증, 중복 검사, 권한 확인, 도메인 규칙 처리
    - Repository를 호출하여 실제 저장/조회 수행

---
현재 file*repo에서 map을 정의했고, 생성자에서 file을 load하여 map에 저장한다.
모든 메서드에서는 map의 데이터로 처리를 한다.
그리고 save나 delete를 할 경우 map을 업데이트 후 저장을 하여 백업을 만든다.
