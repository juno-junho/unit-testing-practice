<Chapter 10> 목 객체 사용

- 이 챕터에서는 Mock 객체를 도입해 고통을 주는 협력자에 대한 의존성을 끊는 방법과 항상 존재하는 장애물을 넘을 수 있게 도와주는 도구 활용법을 다룬다.

```java
public class AddressRetriever {
   public Address retrieve(double latitude, double longitude)
         throws IOException, ParseException {
      String parms = String.format("lat=%.6flon=%.6f", latitude, longitude);
      String response = new HttpImpl().get(
        "http://open.mapquestapi.com/nominatim/v1/reverse?format=json&"
        + parms);

      JSONObject obj = (JSONObject)new JSONParser().parse(response);

      JSONObject address = (JSONObject)obj.get("address");
      String country = (String)address.get("country_code");
      if (!country.equals("us"))
         throw new UnsupportedOperationException(
            "cannot support non-US addresses at this time");

      String houseNumber = (String)address.get("house_number");
      String road = (String)address.get("road");
      String city = (String)address.get("city");
      String state = (String)address.get("state");
      String zip = (String)address.get("postcode");
      return new Address(houseNumber, road, city, state, zip);
   }
}
```

위에서 `retrieve()` 메서드의 테스트를 작성하려고 하는데,

위의 `HttpImpl` 클래스는 아파치의 `HttpComponents` 클라이언트와 상호 작용해 REST 호출을 실행한다.

위 클래스의 `retreive()` 에 대한 테스트는 실제 HTTP 호출을 실행하기에 두가지 문제점이 있다.

1. 실제 호출에 대한 테스트는 다른 테스트들에 비해 상대적으로 느리다 (FAST X)
2. 외부 API (위 경우 Nominatim)가 항상 가용한지 보장할 수 없다.

→ 따라서 의존성이 있는 다른 코드와 분리해 `retrieve()` 메서드의 로직에 관한 단위 테스트를 원한다.

→ `HttpImpl` 클래스를 신뢰할 수 있다면 남은 것은 1.**HTTP 호출을 준비하는 로직**과 호출에 대한

2.**HTTP 응답에서 생성되는 Address 객체를 생성하는 로직을 테스트하는 것**

먼저 두 번째 **HTTP 응답에서 생성되는 Address 객체를 생성하는 로직을 테스트하는 것**에 집중을 해보자.

- **Stub(스텁)** : 테스트 용도로 하드 코딩한 값을 반환하는 구현체
- `HttpImpl` 을 끊어낼 필요가 있다. → `get()` 메서드 동작을 변경할 필요가 있다.

Http 인터페이스를 만들어 `HttpImpl` 이 implement하게 한다.

그리고 아래와 같이 stub을 만들어 `AddressRetriever` 클래스에 DI (의존성 주입)을 통해서 알려준다.

```java
Http http = (String url) -> 
         "{\"address\":{"
         + "\"house_number\":\"324\","
         + "\"road\":\"North Tejon Street\","
         + "\"city\":\"Colorado Springs\","
         + "\"state\":\"Colorado\","
         + "\"postcode\":\"80903\","
         + "\"country_code\":\"us\"}"
         + "}";
```

```java
AddressRetriever retriever = new AddressRetriever(http);

      Address address = retriever.retrieve(38.0,-104.0);
      
      assertThat(address.houseNumber, equalTo("324"));
      assertThat(address.road, equalTo("North Tejon Street"));
      assertThat(address.city, equalTo("Colorado Springs"));
      assertThat(address.state, equalTo("Colorado"));
      assertThat(address.zip, equalTo("80903"));
```

그래서 위와 같이 생성자를 통해서 넘기고 그 값을 사용해 Address 객체를 생성해 테스트 한다.

이제 실제 사용시 생성자로 `new HttpImpl()` 를 넘기면 된다.

이렇게 인터페이스를 통해 결합도를 조금 느슨하게 만들 수 있다.

- 생성자 주입이 아는 setter 메서드, 팩토리 메서드, 추상 팩토리, 스프링 등등을 사용 가능.

그럼 1.**HTTP 호출을 준비하는 로직**은 어떻게 처리해야 할까

위도와 경도를 메서드로 잘 넘기지 않으면 어떻게 될까

`HttpImpl` 코드와 `retrieve()` 메서드의 코드가 올바르게 상호 작용하는지 검증해야 한다.

`Http` 의 `get()` 메서드에 전달되는 url을 검증하는 보호절을 추가한다.

기대하는 argument 문자열을 포함하지 않으면 테스트 처리를 실패한다.

```java
Http http = (String url) -> 
         { 
           if (!url.contains("lat=38.000000&lon=-104.000000")) 
              fail("url " + url + " does not contain correct parms");
           return "{\"address\":{"
                 + "\"house_number\":\"324\","
                 + "\"road\":\"North Tejon Street\","
                 + "\"city\":\"Colorado Springs\","
                 + "\"state\":\"Colorado\","
                 + "\"postcode\":\"80903\","
                 + "\"country_code\":\"us\"}"
                 + "}";
         };
```

- **Mock**: 의도적으로 흉내 낸 동작을 제공하고 수신한 인자가 모두 정상인지 여부를 검증하는 일을 하는 테스트 구조물
- 위 stub은 Mock이라고 알려진 것에 가깝다.

다음 단계로 stub을 mock으로 변환하는 과정이다. 그러기 위해서는 다음 일들이 필요하다.

- 테스트에 어떤 argument 기대하는지 명시하기
- `get()` 메서드에 넘겨진 인자들을 잡아 저장하기
- `get()` 메서드에 저장된 인자들이 기대하는 인자들인지 테스트가 완료될 때 검증하는 능력 지원하기



번거로운 의존성에 대해 더 많은 Mock을 구현하면 이들 사이에 중복을 제거하는 방법을 찾을 수도 있다.

직접 구현하기 보다 Mockito를 사용하자.

→ Mockito 학습 후 다시 읽어보기

Mock을 올바르게 사용할 때 중요한 것

- Mock을 사용한 테스트는 진행하길 원하는 내용을 분명하게 기술해야 한다. (연관성 때문_
    - 코드를 깊이 파지 않아도 이러한 연관성을 쉽게 파악할수록 코드는 더 좋아진다.
- Mock이 실제 동작을 대신한다. 그것들을 안전하게 사용하고 있는지 확인하기 위해서 몇가지 질문을 자신에게 던져야 한다.
    - Mock이 프로덕션 코드의 동작을 올바르게 묘사하고 있는가
    - 프로덕션 코드는 생각하지 못한 다른 형식으로 반환하는가
    - 프로덕션 코드는 예외를 던지는가
    - 프로덕션 코드는 null을 반환하는가
- 테스트가 Mock을 진짜로 사용하는지 프로덕션 코드를 실행하는지 판단하기 위해서는

  Mock을 끄고 `HttpImpl` 프로덕션 코드와 상호작용 한다면 테스트가 느려진다.

  아니면 임시로 프로덕션 코드에서 `Runtime Exception`을 던져보기. 예외가 보이면 프로덕션 코드가 동작하고 있는 것.

- 프로덕션 코드를 직접 테스트하고 있지 않다는 것을 기억해라

  Mock을 도입하면 테스트 커버리지에서 간극을 형성할 수 있음을 인지해야 한다.


**Stub은 테스트용으로 하드코딩한 값 반환하는 구현체인데
Mock은 정상인지 검증 하는 것 까지 포함**