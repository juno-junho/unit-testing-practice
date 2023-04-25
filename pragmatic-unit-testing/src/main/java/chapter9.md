**<Chapter 9> 더 큰 설계 문제 ** 이때까지 가장 유용한 장!**

- 시스템 설계는 테스트를 작성하는 능력에 영향을 미치고, 테스트를 작성하는 능력 또한 시스템 설계에 영향을 미친다.
- 단일 책임 원칙 (SRP)는 좀 더 작은 클래스를 만들어 무엇보다 유연성과 테스트 용이성을 높여준다.

```java
public class Profile {
   private Map<String,Answer> answers = new HashMap<>();

   private int score;
   private String name;

   public Profile(String name) {
      this.name = name;
   }
   
   public String getName() {
      return name;
   }

   public void add(Answer answer) {
      answers.put(answer.getQuestionText(), answer);
   }

   public boolean matches(Criteria criteria) {
      calculateScore(criteria);
      if (doesNotMeetAnyMustMatchCriterion(criteria))
         return false;
      return anyMatches(criteria);
   }

   private boolean doesNotMeetAnyMustMatchCriterion(Criteria criteria) {
      for (Criterion criterion: criteria) {
         boolean match = criterion.matches(answerMatching(criterion));
         if (!match && criterion.getWeight() == Weight.MustMatch) 
            return true;
      }
      return false;
   }

   private void calculateScore(Criteria criteria) {
      score = 0;
      for (Criterion criterion: criteria) 
         if (criterion.matches(answerMatching(criterion))) 
            score += criterion.getWeight().getValue();
   }

   private boolean anyMatches(Criteria criteria) {
      boolean anyMatches = false;
      for (Criterion criterion: criteria) 
         anyMatches |= criterion.matches(answerMatching(criterion));
      return anyMatches;
   }

   private Answer answerMatching(Criterion criterion) {
      return answers.get(criterion.getAnswer().getQuestionText());
   }

   public int score() {
      return score;
   }

   public List<Answer> classicFind(Predicate<Answer> pred) {
      List<Answer> results = new ArrayList<Answer>();
      for (Answer answer: answers.values())
         if (pred.test(answer))
            results.add(answer);
      return results;
   }
   
   @Override
   public String toString() {
     return name;
   }

   public List<Answer> find(Predicate<Answer> pred) {
      return answers.values().stream()
            .filter(pred)
            .collect(Collectors.toList());
   }
```

위 `Profile` 클래스는 두가지 일을 하고 있다.

1. 회사 / 인물의 정보 추적 및 관리 (name과 answer Map)
2. 조건의 집합이 profile과 매칭되는 여부 혹은 그 정도를 알려주는 점수 계산 (score)

**클래스가 왜 하나의 책임만 가져야 하는가?에 대한 질문에 대한 답은**

**유지보수성과 관련이 있다.** 크기가 커질 수록, 책임이 많아 질 수록 변경에 대한 리스크가 커진다.

단일 책임을 강조하면 변경으로 인한 리스크가 줄어든다.

더 많은 책임이 존재할수록, 클래스에 있는 코드 변경 시 기존 다른 동작을 깨기 쉽고 더 작고 집중화된 클래스는 재활용 가능하다.

**(추후 변경으로 인한 리스크 + 재활용)**

첫 번째 책임과 관련되어, Profile 클래스가 포착하는 이러한 정보 집합들은 시간이 지나면 바뀔 수 있다.

하지만 두 번째 책임은 Profile 클래스가 변경 되는 이유와 맞다.

따라서 점수를 계산하는 로직은 MatchSet 클래스를 생성해 이동시킨다.

중복되는 코드가 있으면 한 곳에 위치시킬 방법을 고민해야 한다.

- 명령-질의 분리(Command-Query Separation principle) CQS

위 코드에서 아래와 같이 MatchSet으로 score의 계산 기능을 분리하였다.

```java
public boolean matches(Criteria criteria) {
//        calculateScore(criteria);
        score = new MatchSet(answers, criteria).getScore(); // refactoring
        if (doesNotMeetAnyMustMatchCriterion(criteria))
            return false;
        return anyMatches(criteria);
    }
```

하지만 score 변수를 저렇게 두는 것이 맞을까

점수를 원한다면 matches()를 호출해야 한다,.

이것은 직관에 어긋난다 (점수를 원하는데 matches 함수 통해서 boolean 결과를 리턴하는 함수 호출..)

**명령-질의 원칙 : 어떤 메서드는 명령을 실행하거나 질의에 대답 할 수 있으며, 두 작업을 모두하면 안된다.**

**왜? 클라이언트 코드에 잠재적인 고통을 줄 수 있다. (질의 메서드가 상태 바꾸면 그 메서드 두번 호출하는것이 불가능 or 두 번째 호출하면 객체 변질)**

e.g ) `Iterator` 인터페이스의 `next()`

하지만 마틴파울러에 따르면 Stack의 `pop()` 처럼 유용한 경우 trade off 따져 사용 가능

**메서드를 호출 했을 때 내부에서 변경 (사이드 이펙트)가 일어나는 메서드 인지, 아니면 내부에서 변경이 전혀 일어나지 않는 메서드인지 명확히 분리하는것이 중요하다.**

어떤 값을 반환하고 부작용을 발생시키는 (시스템에 있는 어떤 클래스 혹은 엔티티의 상태 변경) 메서드는 CQS 위반.

→ Profile 클래스 score 필드 제거 하고 아래 메서드 도입.

```java
public MatchSet getMatchSet(Criteria criteria) {
        return new MatchSet(answers, criteria);
    }
```

**** 단위테스트의 유지보수 비용**

보통 돌아오는 가치가 훨씬 크기에 깨진 테스트 코드를 고치는 비용을 받아 들인다.

결함이 거의 없는 코드를 갖는 이점과 다른 코드가 깨질 것을 걱정하지 않으면서 코드 변경할  수 있는 이점, 코드가 정확히 어떻게 동작하는지 알 수 있는 이점.

**핵심 포인트**

- 코드 중복은 가장 큰 설계 문제이다.
1. 테스트를 따르기가 어려워 진다.
2. 작은 코드조각→단일메서드 추출하면 코드 조각 변경해야할 때 미치는 영향 최소화
3. 단위 테스트 설정시 코드가 몇 줄, 수십 줄 필요하면 그것은 시스템 설계에 문제가 있다는 것.
- SRP위반 → 클래스 점점 커지고, 의존성 커짐.
- PRIVATE 메서드 테스트 하고 싶다 → 클래스가 필요 이상으로 커졌다는 힌트.
    - 내부동작 새 클래스로 옮기고 public으로 만들기
- 단위 테스트가 어려워 보이면 좋은 힌트. 설계 개선해 단위 테스트를 쉽게 만들어라.

새로운 클래스로 분리시 이점 :

**작성한 테스트가 좀 더 직관적이고 작성하기 쉬워진다.**

(MatchSet 코드 테스트시 더이상 Profile 객체 생성하지 않아도된다.

**********************************************************************다른 설계에 관한 생각들**********************************************************************

- 생성자에서 실제적인 작업을 피해라. 다른 요청 받았을 때 점수 계산하도록 코드 변경해라

```java
public MatchSet(Map<String, Answer> answers, Criteria criteria) {
        this.answers = answers;
        this.criteria = criteria;
        calculateScore(criteria);
    }
```

매번 생성할 때마다 점수 계산한다.

→ `getScore()`로 점수 계산하는거 빼기

`getScore()` 메서드 호출마다 매번 점수 계산하는 것이 성능 저하로 이어진다면 **지연 초기화(Lazy initializtion)** 사용 가능

- Map인 answers를 데이터베이스 테이블로 교체한다면 결국 여러 군데를 고쳐야한다.

  또한 여러 군데에 있으면 데이터 상태에도 혼란이 온다.

  → 일급 컬렉션 도입해 클래스 분리

- 시스템 설계에 대해 비판적인 눈을 유지하고 최상의 설계는 없다는 것을 명심하기.
- 시스템을 깨끗하게 하는 책임은 결코 끝이 없다.

**정리**

- **SRP와 CQS 처럼 커다란 설계 원칙 기반으로 설계 개선해라**
- **작은 개념, 작은 코드 리펙토링이 커다란 차이를 만들어 낸다.**
- **기꺼이 새롭고 작은 클래스들과 메서드를 만들어라.**
- **유연한 설계는 더 작고 잘 조직된 구성 요소로 시작된다.**