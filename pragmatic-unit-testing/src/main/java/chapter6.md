<Chapter 6> Right-BICEP: 무엇을 테스트 할것인가?

Right-BICEP은 무엇을 테스트할지에 대해 쉽게 선별하게 한다.

- Right : 결과가 올바른가?
- B : 경계 조건(boundary conditions)은 맞는가?
- I : 역 관계(inverse relationship)을 검사할 수 있는가?
- C : 다른 수단을 활용해 교차 검사(cross-check) 할 수 있는가?
- E : 오류 조건(error conditions)을 강제로 일어나게 할 수 있는가?
- P : 성능 조건(performance characteristics)은 기준에 부합하는가?

1. Right: 결과가 올바른가?
- 테스트 코드는 무엇보다 먼저 기대한 결과를 산출하는지 검증할 수 있어야 한다.
  **(원하는 결과값이 나오는지 (정상적으로 동작하는지) 알 수 있어야 한다.)**
- 어떤 작은 부분의 코드에 대해 행복 경로 테스트를 할 수 없다면 그 내용을 완전히 이해하지 못한 것이다.
- **Happy path testing (행복 경로 테스트)** : input이 예상하는 결과값을 출력하는 것.
- 작성하는 단위 테스트는 선택을 문서화한다. 어떤 변경이 발생하면 적어도 현재까지 코드가 어떻게 동작했는지는 알게 된다.

1. B : 경계 조건은 맞는가?
- 마추치는 수많은 결함은 모서리 사례(cornor case)이므로 테스트로 이것을 처리해야 한다.
- 생각해야 하는 경계조건은 다음과 같다.
    - 특수문자같이 모호하고 일관성 없는 입력값이 포함된 파일 이름
    - 잘못된 양식의 데이터
    - 수치적 overflow
    - 비거나 null값
    - 이상적인 기댓값을 훨씬 벗어나는 값
    - 중복값 체크
    - 시간순이 맞지 않는 경우 등..

→ 따라서 long에서 int로 다운 캐스팅시 주의해야 한다.

- 클래스 설계시 잠재적인 정수 오버플로 등을 고려할지 여부는 전적으로 우리에게 달려있다.

2-1. 경계 조건에서는 CORRECT를 기억해라 (chapter 7)

- Conformance(준수) : 값이 기대한 양식을 준수하는지
- Ordering(순서) : 값의 집합이 적절하게 정렬되거나 정렬 되지 않았는지
- Range(범위) : 이성적인 최솟값과 최댓값 안에 있는지
- Reference(참조) : 코드 자체에서 통제할 수 없는 어떤 외부 참조를 포함 하는지
- Existence(존재) : 값이 존재하는지 (null/nonnull, 0인지, 집합에 존재하는지)
- Cardinality(기수) : 정확히 충분한 값들이 있는지
- Time(절대적/상대적 시간) : 모든 것이 순서대로 일어나는지

1. I : 역 관계를 검사할 수 있는가
- 때때로 논리적인 역관계를 적용해 행동을 검사할 수 있다.
- 종종 수학 계산에서 사용한다 (제곱근 → 제곱)
    - 하지만 주의할 점이 루틴 두개가 공통된 코드를 호출하면 프로덕션 코드와 역 행동 모두 같은 결함을 공유하게 된다. 검증을 위해 독립적인 방법 찾아보기
    - 예를들어 DB에 데이터 넣는다 → 테스트에서 직접적인 쿼리해서 꺼내보기.

1. C : 다른 수단을 활용하여 교차 검사할 수 있는가?
- 여러가지 방법으로 검사 할 수 있다.
    - 예를들어, 제곱근의 자바 라이브러리(Math.sqrt)를 사용해 동일한 결과 내는지 확인 할 수 있다.
    - 도서 대출 시스템 경우, 대출된 도서 + 선반에 있는 도서 = 총수량

      → 서로 다른 클래스의 조각 데이터를 통해 합산 되는지 확인해보기

1. E : 오류 조건을 강제로 일어나게 할 수 있는가?
- 테스트도 오류들을 강제로 발생시켜야 한다.
- 좋은 단위 테스트는 단지 코드에 존재하는 로직 전체에 대한 커버리지를 달성하는 것이 아니다. 때때로 창의력을 발휘하는 노력이 필요하다. 가장 끔찍한 결함은 예상하지 못한곳에서 나온다.
- 고려해야할 몇가지 시나리오
    - 메모리 가득 참
    - 디스크 공간 가득 참
    - 시간 차이로 생기는 문제들
    - 네트워크 가용성 및 오류들
    - 시스템 로드 등등..

1. P : 성능 조건은 기준에 부합하는가?
- 추측만으로 성능 문제에 바로 대응하기 보다, 단위 테스트를 설계해 진짜 문제가 어디 있으며 예상한 변경 사항으로 어떤 차이가 생겼는지 파악해야 한다.
- 모든 성능 최적화 시도는 실제 데이터로 해야지 추측을 기반으로 해서는 안된다.
- 느린 테스트들은 빠른것과 분리해라.
- 동일한 머신이라도 실행 시간은 잡다한 요소에 따라 달라질 수 있기에 성능을 테스트 할때는 사전 조건을 단단히 정의해 놓아야 반복성과 재현성을 보잘 할 수 있다.
  → 변경사할 만들 때 기준점으로 활용하는 것.