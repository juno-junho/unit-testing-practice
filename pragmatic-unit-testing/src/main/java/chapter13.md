# 13장. 까다로운 테스트

이 장에서는 스레드와 영속성을 테스트 하는 방법을 다룬다.

`설계 다시 하기` 와 `스텁과 목을 사용하여 의존성 끊기` 에 기반을 둔다.

1. **멀티스레드 코드 테스트**

```java
public void findMatchingProfiles(
            Criteria criteria, MatchListener listener) {
        ExecutorService executor =
                Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);

        List<MatchSet> matchSets = profiles.values().stream()
                .map(profile -> profile.getMatchSet(criteria))
                .collect(Collectors.toList());

        for (MatchSet set: matchSets) {
            Runnable runnable = () -> {
                if (set.matches())
                    listener.foundMatch(profiles.get(set.getProfileId()), set);
            };
            executor.execute(runnable);
        }
        executor.shutdown();
    }
```

애플리케이션을 빠르게 반응하도록 만들고 싶어서

메서드를 각각 별도의 스레드 맥락에서 매칭을 계산하도록 설계했다.

설명하자면, `findMatchingProfiles()` 메서드는 각 프로파일에 대해서 `MatchSet` 인스턴스의 리스트를 모은다. 각 `MatchSet` 에 대해 메서드는 **별도의 스레드를 생성해** `MatchSet` 객체의 `matches()` 값이 true이면 프로파일과 그에 맞는 `MatchSet` 객체를 `MatchListener` 로 보낸다.

```java
public void findMatchingProfiles(
         Criteria criteria, MatchListener listener) {
      ExecutorService executor = 
            Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
      for (MatchSet set: collectMatchSets(criteria)) {
         Runnable runnable = () -> {
            if (set.matches())
               listener.foundMatch(profiles.get(set.getProfileId()), set);
         };
         executor.execute(runnable);
      }
      executor.shutdown();
   }

   List<MatchSet> collectMatchSets(Criteria criteria) {
      List<MatchSet> matchSets = profiles.values().stream()
            .map(profile -> profile.getMatchSet(criteria))
            .collect(Collectors.toList());
      return matchSets;
   }
```

하지만 이렇게 애플리케이션 로직과 스레드 로직을 둘 다 사용하기에

**첫 번째 과제는 둘을 분리시킨다.**

위 처럼 분리하여 `collectMatchSets()` 처럼 작은 로직을 위한 테스트 코드를 작성한다.

또 다시 메서드의 스레드 로직을 아래와 같이 **재 설계한다.**

```java
public void findMatchingProfiles(
         Criteria criteria, MatchListener listener) {
      ExecutorService executor = 
            Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);

      for (MatchSet set: collectMatchSets(criteria)) {
         Runnable runnable = () -> process(listener, set);
         executor.execute(runnable);
      }
      executor.shutdown();
   }

   void process(MatchListener listener, MatchSet set) {
      if (set.matches())
         listener.foundMatch(profiles.get(set.getProfileId()), set);
   }
```

위와 같이 프로파일 정보를 listener로 넘기는 애플리케이션 로직도 추출한다.

→ 그 이후 `MatchListener` 인터페이스 Mocking하여 테스트 진행한다.

```java
private ExecutorService executor = 
         Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
   
   ExecutorService getExecutor() {
      return executor;
   }
   
   public void findMatchingProfiles( 
         Criteria criteria, 
         MatchListener listener, 
         List<MatchSet> matchSets,
         BiConsumer<MatchListener, MatchSet> processFunction) {
      for (MatchSet set: matchSets) {
         Runnable runnable = () -> processFunction.accept(listener, set); 
         executor.execute(runnable);
      }
      executor.shutdown();
   }
 
   public void findMatchingProfiles( 
         Criteria criteria, MatchListener listener) { 
      findMatchingProfiles(
            criteria, listener, collectMatchSets(criteria), this::process);
   }
```

테스트코드에서 `ExecutorService` 인스턴스에 접근할 필요가 있기에 초기화를 필드 수준으로 추출하고 getter를 만든다.

`process()` 는 테스트 했으므로 **stub으로 처리하여 테스트** 할 수 있다.

이렇게 **애플리케이션 로직과 스레드 로직의 관심사를 분리**해 테스트를 몇 개 작성할 수 있었다.

스레드 중심 테스트를 처리하는 데 도임이 되는 유틸리티 메서드들을 만들면서 이어지는 스레드 관련 테스트도 쉬워 졌다

**→ 정리 : 애플리케이션 로직과 스레드 로직의 분리한 설계**

1. **데이터베이스 테스트**
- 자바 영속성 API인 JPA를 사용하는 포트스그레 데이터베이스와 통신하는 변수의 경우 테스트를 작성하기가 어렵다.

JPA 인터페이스를 구현하는 코드에 대한 단순한 위임인 Repository 클래스의 대부분의 로직들은 JPA에 대한 의존성을 고립시켰기 때문에 좋은 설계이나 테스트 관점에서는 의문이 든다.

JPA 관련 인터페이스를 모두 스텁으로 만들어 단위테스트 할 수도 있지만 노력이 많이 들고 테스트도 어렵다.

그 많은 것을 증명하고 싶지 않다.

대신 DB와 성공적으로 상호작용하는 Repository에 대한 클래스를 작성한다.

모든 것이 올바르게 연결 되었음을 증명하는 것이다. 자바 코드 - 매핑 설정 - DB, 각 서로 다른 세 가지 조각들이 함께 동작하는지를 검증한다.

JUnit의 대다수는 속도가 빠르길 원한다.

영속적인 모든 상호 작용을 한 곳에 고립시킬 수 있다면 통합 테스트의 대상은 결국 상당히 소규모로 줄어들 것이다.

진짜 데이터베이스와 상호 작용하는 통합 테스트를 작성할 때 DB의 데이터와 그것을 어떻게 가져올 지는 매우 중요한 고려 사항이다.

**테스트 안에서 데이터를 생성하고 관리해라.**

깨끗한 데이터베이스로 시작해 테스트 간 의존성 문제를 최소화 할 수 있다.

테스트 간 의존성 문제는 다른 테스트에서 남아 있던 데이터 때문에 어떤 테스트가 망가지는 것을 의미한다.

**테스트를 위해 공유된 DB에만 접근할 수 있따면 트랜젝션을 사용해 테스트 마다 트렌젝션 초기화 하고 테스트 끝나면 롤백해라.**

**통합 테스트는 작성과 유지보수가 어렵다. 잘 깨지고 디버깅하기도 어렵지만 여전히 테스트 전략의 필수적인 부분이다.**

→ 이렇게 테스트를 진행하고 **Repository를 mocking해서 repository를 사용하는 메서드를 테스트** 해라.

**정리 :**

- **관심사를 분리해 스레드 / 애플리케이션 로직 / 데이터베이스 or 문제를 일으킬 수 있는 다른 의존성과 분리해라.**
- **의존적인 코드는 고립시켜 코드 베이스에 만연하지 않도록 해라.**
- **느리거나 휘발적인 코드를 Mock으로 대체하여 단위테스트의 의존성을 끊어라.**
- **필요한 경우에는 통합테스트를 작성하되, 단순하고 집중적으로 만들어라.**