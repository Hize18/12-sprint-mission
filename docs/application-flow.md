## Application 실행 목적

메뉴 기반 콘솔 프로그램이 아니라, 서비스 구현체의 동작을 단순 실행으로 검증하는 테스트용 클래스.

구현한 서비스로 CRUD 실행.

## 테스트 항목

### 등록
- User 3명 생성
- Channel 3개 생성
- 각 User가 3개의 Channel에 Message를 1개씩 생성

### 조회
- 조회는 전체 -> 단건 순서
- 순서는 User -> Channel -> Message

### 수정
- User nickname을 중복된 값으로 수정
- User username을 중복된 값으로 수정하여 예외 발생 확인
- Channel name 수정
- Message content 수정
- 수정 직후 해당 객체 및 전체 목록 출력

### 삭제
- 삭제 후 삭제한 id로 `findById()`를 호출하여 `null` 여부 확인
- User, Channel, Message 순 또는 참조 관계를 고려한 순서로 삭제 후 전체 목록 출력

## 예외 처리
- 예외 처리는 `IllegalArgumentException`과 메시지 출력 위주로 단순하게 처리
- 현재 단계에서는 디버깅과 검증 목적에 집중

## 참조 관계 메모
- 현재 객체 간 참조를 UUID가 아니라 객체 자체로 가지고 있으므로,
  저장소(Map)에서 remove를 하더라도 다른 객체가 이미 참조 중인 객체는 메모리상에서 계속 참조될 수 있다.
- 따라서 삭제 이후에도 참조 중인 객체 출력 결과가 남아 있을 수 있다.
- 수정의 경우 참조중인 객체와 수정한 객체가 같은 주소를 가리키고 있다.
- 전과 후의 차이를 보기위해서는 수정전에 복사(생성자 복사로 안하고 toString을 저장)를 했다.
- 삭제의 경우 service의 data에서 제거되어서 호출할 경우 null을 나타내지만 참조중인 객체는 그대로 값을 나타냄.


Fileio 추가
fileio와 직렬화 및 역직렬화를 적용하는 부분.
javaapplication에서는 jcf*service로 동작하던 부분을 file*service로 변경시켜서 기존 코드를 기대로 활용한다.
fileio를 통해 데이터를 사용함에 따라 main이 끝나도 map의 데이터가 data/*.ser로 남게됨.
그렇기 때문에 기존에 initData로 초기화하던 데이터들은 중복처리가 된다.
그래서 saveMethod를 통해 중복되는 데이터를 추가 등록하지 않게처리함.

아래는 메소드들의 변경사항 및 추가사항 간단 정리.
initData
중복 및 매번 실행마다 추가 등록을 방지 하기 위해 saveMethod를 추가 정의
또한 메시지의 경우 중복이 있을 수 없기 때문에 초기 데이터는 isEmpty()일 경우 등록하고
message의 delete 메소드 실행 후 다시 출력시 메시지가 조회시 발생하는 에러 해결을 위해 size==8로 하나 삭제한 경우
추가 등록으로 방지.(물론 size==8같은 조건은 테스트 일때만 사용.)

saveMethod
이전에 정의한 updateMethod나 deleteMethod처럼 오버로딩을 통해 통일성을 주기 위해 사용
도메인에 따라 중복 처리를 먼저 실행후 try-catch로 에러 검출.

---
repo 및 basic을 추가 정의함에 따라 코드도 변경 사항이 있지만, basic에서 기존 *service를 구현하기 때문에 service 선언부만 diff가 있음.