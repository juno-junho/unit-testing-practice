<Chapter 11> 테스트 리펙토링

- 비용 증가로 이어지는 테스트 문제들을 리펙토링하고 이해도를 최대화 하여 유지보수 비용을 최소화 할 수 있다.

**[Test Smell에 대해서]**

1. 불필요한 테스트 코드
    - 테스트 메서드 실행시 예외가 발생한다면 `try-catch` 사용하지 말고 예외를 던져라.
    - 여러개의 중복조건이 있는 assert → 하나의 조건으로 합쳐보기. (불필요한 테스트코드 제거)

2. 추상화 누락
- **좋은 테스트는 클라이언트가 시스템과 어떻게 상호 작용하는지 추상화 한다.**
- 단일 개념을 구현하는 2줄 혹은 3줄이 넘는 코드를 발견했다면, 테스트에 그것을 깔끔한 문장 한 줄로 추출할 수 있는지 고민해 봐라.
- `isEqualTo(0)` → `isEmpty()`

3. 부적절한 정보
- 잘 추상화된 테스트는 고드를 이해하는 데 중요한 것을 부각시켜 주고 그렇지 않은 것은 보이지 않게 해준다.
- 의미가 불분명한 매직 리터럴 → 상수화를 통해 의미부여

4. 부푼 생성
- 정신 산란한 세부 사항을 숨기는 것은 테스트의 가독성을 훨씬 더 높혀준다.
- 한 객체를 두 군데서 만드는 경우, 객체 생성 부분을 메서드로 분리하여 하나의 객체를 사용하도록 리펙토링

5. 다수의 단언(assert)
- 테스트마다 단언 한 개로 가는 방향은 좋은 방법이다.
- 여러 개의 단언이 있다는 것은 테스트 케이스를 두 개 포함하고 있다는 증거이다.

  → 따라서 여러개의 단언이 있다면 메서드를 나눠봐라.

- 또한 테스트마다 단언 한 개로 가면 테스트 이름을 깔끔하게 만들기 쉽다.

단일 목적의 테스트는 주석이 없어도 더 나은 테스트 이름으로 대신할 수 있다.

6. 테스트와 무관한 세부 사항들
- 테스트를 실행할 때는 로그를 끄지만 로그 기능들은 테스트의 정수를 이해하는데 방해 될 수 있다.
- 스트림을 닫는 것 또한 테스트에서는 군더더기가 될 수 있다.
- 이러한 군더더기들을 `@Before` 와 `@After` 메서드로 이동해라.
- 하지만 세부 내용를  `@Before` 와 `@After` , 도우미 메서드로 이동 할 때는 주의해야 한다. 테스트를 이해하는데 필요한 유용한 정보는 제거하지 않았는지 확인해야 한다.
- **좋은 테스트는 독자가 테스트를 이해하는 데 다른 함수를 파헤치지 않도록 한다.**

7. 잘못된 조직
- AAA (준비, 실행, 단언) 에서 빈 줄을 통해서 구분해라.
- AAA를 활용해 의도를 분명하게 해라.

8. 암시적 의미
- 독자는 테스트 준비와 단언 부분을 상호 연관 지을 수 있어야 한다.
- 테스트를 독자가 더 쉽게 이해 할 수 있도록 좀 더 나은 테스트 데이터를 골라서 명시적으로 바꾸어 보아라. 검색어를 포함하면서 슬쩍 보아도 이해할 수 있는 내용으로 입력 스트림을 변경해라.
- 주변 맥락 정보도 명시적으로 개수를 세지 않아도 되게 만들어라.

```java
@Test
   public void returnsMatchesShowingContextWhenSearchStringInContent() {
      stream = streamOn("There are certain queer times and occasions "
            + "in this strange mixed affair we call life when a man "
            + "takes this whole universe for a vast practical joke, "
            + "though the wit thereof he but dimly discerns, and more "
            + "than suspects that the joke is at nobody's expense but "
            + "his own.");
      Search search = new Search(stream, "practical joke", A_TITLE);
      search.setSurroundingCharacterCount(10);
      search.execute();
      assertThat(search.getMatches(), containsMatches(new Match[]
         { new Match(A_TITLE, "practical joke", 
                              "or a vast practical joke, though t") }));
   }

@Test
   public void returnsMatchesShowingContextWhenSearchStringInContent() {
      stream = streamOn("rest of text here"
            + "1234567890search term1234567890"
            + "more rest of text");
      Search search = new Search(stream, "search term", A_TITLE);
      search.setSurroundingCharacterCount(10);

      search.execute();

      assertThat(search.getMatches(), containsMatches(new Match[]
         { new Match(A_TITLE, 
                    "search term", 
                    "1234567890search term1234567890") }));
   }
```

- 위 코드를 비교 해 보아라.
- 테스트를 걸쳐 상호 관련성을 향상시키는 방법은 무한하다. 의미있는 상수, 더 좋은 변수 이름, 더 좋은 데이터와 때때로 테스트에서 계산을 적게 만드는 것이 도움이 된다.

**[새로운 테스트 추가]**

- 반대의 테스트 또한 추가해라.

**테스트로 시스템을 이해하고자 한다면 테스트를 깔끔하게 유지하는 것이 좋다.**

**설계란 프로덕션 코드 깔끔하고 간결히 리펙토링 → 프로덕션 코드 설계 할때 더 많은 유연성 제공하도록 리펙토링 → 시스템 의존성을 Mock으로 해결 → 유지보수 비용 최소화, 이해도 최대화하도록 테스트 리펙토링.**