
# ClientRegistration
```
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: xxx
            client-secret: yyy

        provider:
          google:
            authorization-uri: https://accounts.google.com/o/oauth2/v2/auth
            token-uri: https://oauth2.googleapis.com/token
```
자 첨에 보면 yml에 이렇게 세팅하잖아.

여기서 두 가지로 나뉘어. 1.  registration(이 클라이언트가 어느 OAuth 서버를 쓰는지) 2. provider(그 OAuth 서버가 제공하는 엔드포인트들)

이걸 읽어서 시큐리티가 ```ClientRegistration``` 클래스로 만드는 거야.

https://docs.spring.io/spring-security/reference/api/java/org/springframework/security/oauth2/client/registration/ClientRegistration.html

api야.

즉 시큐리티는 우리가 어떤 OAuth 서버를 쓰고, 그 서버가 제공하는 엔드포인트들을 다 알고 있어. 그러니까 시큐리티가 알아서 토큰 가져오고 하겄지.

```ClientRegistrationRepository``` 가 이 ```ClientRegistration```들을 다 가지고 있어.


이제 느낌 자체를 알려줄게. 

1. /oauth2/authorization/{registrationId} 로 요청.

2. ```OAuth2AuthorizationRequestRedirectFilter``` 가 ```authorizationRequestResolver``` 를 시켜서 ```OAuth2AuthorizationRequest``` 생성(```ClientRegistrationRepository``` 에서 registrationId로 해당 ```ClientRegistration``` 찾아서 만듬) 및 ```OAuth2AuthorizationRequestRedirectFilter``` 에 반환.

3. ```OAuth2AuthorizationRequestRedirectFilter``` 가 ```OAuth2AuthorizationRequest``` 에 있는 로그인 uri로 리다이렉트 시킴.

https://docs.spring.io/spring-security/reference/api/java/org/springframework/security/oauth2/core/endpoint/OAuth2AuthorizationRequest.html

```OAuth2AuthorizationRequest``` api야.  이건 계속 쓰여. 세션에 저장되고.
```OAuth2AuthorizationRequestRedirectFilter``` 가 ```OAuth2AuthorizationRequestRepository``` 를 통해 ```OAuth2AuthorizationRequest``` 를 저장하는 구조야.

왜 저장하냐면 이제 어차피 인증 코드 받고 다시 리다이렉트 해도 ```OAuth2AuthorizationRequest``` 에 있는 필드들이 또 필요하잖아. 인가 받아야지.

핵심은 보안이야 아래 api 문서 읽어봐.

https://ktseo41.github.io/blog/log/short/prevent-attacks-and-redirect-users-with-oauth-2_0-state-parameters.html

근데 이게 나중에 서버 이중화로 돌리면 또 곤란할 수가 있어. 그래서 스프링 시큐리티에서는 레디스를 사용해서 서버 간 세션을 공유할 수 있는 기능을 제공하고 있으니까 나중에 참고하고.

이제 로그인 성공했으면 인증 코드가 가지고 오겠지? 이 리다이렉트 이후를 책임지는 필터가 ```OAuth2LoginAuthenticationFilter``` 야.

뻔하잖아 다음은. 엑세스 토큰 받아와서 뭐 유저 정보 조회하고 시큐리티 컨텍스트에 넣겠지.
```
public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    // callBackUrl로 부터 온 queryParameter 추출
    MultiValueMap&lt;String, String&gt; params = OAuth2AuthorizationResponseUtils.toMultiMap(request.getParameterMap());

    // 정상적으로 api요청이 왔는지 확인작업
    if (!OAuth2AuthorizationResponseUtils.isAuthorizationResponse(params)) {
    }
    OAuth2AuthorizationRequest authorizationRequest = this.authorizationRequestRepository
            .removeAuthorizationRequest(request, response);
    if (authorizationRequest == null) {
    }

    //registrationId를 통해 Provider 가져오기
    String registrationId = authorizationRequest.getAttribute(OAuth2ParameterNames.REGISTRATION_ID);
    ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId(registrationId);

    // callBackUrl로부터 받은 Parameter 추출 후 OAuth2AuthorizationResponse객체로 변경
    String redirectUri = UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
            .replaceQuery(null)
            .build()
            .toUriString();
    OAuth2AuthorizationResponse authorizationResponse = OAuth2AuthorizationResponseUtils.convert(params,
            redirectUri);

    // 토큰 요청을 위한 OAuth2LoginAuthenticationToken객체 생성
    Object authenticationDetails = this.authenticationDetailsSource.buildDetails(request);

    OAuth2LoginAuthenticationToken authenticationRequest = new OAuth2LoginAuthenticationToken(clientRegistration,
            new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse));
    authenticationRequest.setDetails(authenticationDetails);

    // 중요!! providerManager에서 OAuth2LoginAuthenticationProvider.authenticate() 실행
    //
    OAuth2LoginAuthenticationToken authenticationResult = (OAuth2LoginAuthenticationToken) this
            .getAuthenticationManager().authenticate(authenticationRequest);

    // 받아온 token converting       
    OAuth2AuthenticationToken oauth2Authentication = this.authenticationResultConverter
            .convert(authenticationResult);
    oauth2Authentication.setDetails(authenticationDetails);

    OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(
            authenticationResult.getClientRegistration(), oauth2Authentication.getName(),
            authenticationResult.getAccessToken(), authenticationResult.getRefreshToken());

    this.authorizedClientRepository.saveAuthorizedClient(authorizedClient, oauth2Authentication, request, response);
    return oauth2Authentication;
}
```

여기서 파고들기 전에 시큐리티 전체 맥락을 알아야 해.

첫 부분은 OAuth2만의 코드라 쑥쑥 진행했어. 근데 이제부터는 아니야. 쑥쑥 설명할 수는 있어. 근데 그러면 활용이 안 돼. 물고기 잡는 법을 공부해보자고.

# 시큐리티의 시작 ```DelegatingFilterProxy``` 와 ```FilterChainProxy```

![poster](./filterchainproxy.png)

```DelegatingFilterProxy``` 는 ```DispatcherServlet``` 느낌이야. 톰켓 오리지널 필터 체인에 대표로 침투해서 스프링 시큐리티 필터체인이랑 연결하는 역할이야.

```FilterChainProxy``` 는 여러 필터 체인들 중 요청에 맞는 필터 체인을 선택하는 놈이야. ```HandlerMapping``` 느낌이지.

이게 단점이 스프링부트가 편하긴 해 진짜로. 근데 너무 자동화라 나처럼 쌩초보는 이해가 힘들어 마법 같아서. 그래서 예전 코드를 가져왔어. 순수 스프링 쓰던 때는 이렇게 했대. 지금은 자바로 코드로 하잖아.

```xml
  <bean id="myfilterChainProxy" class="org.springframework.security.util.FilterChainProxy">
      <constructor-arg>
          <util:list>
              <security:filter-chain pattern="/do/not/filter*" filters="none"/>
              <security:filter-chain pattern="/**" filters="filter1,filter2,filter3"/>
          </util:list>
      </constructor-arg>
  </bean>
```

바로 이해되지? ```FilterChainProxy``` 가 여러 필터 체인들을 가지고 있고 하나를 선택해서 돌리는 거야. 한 어플리케이션에서 로그인 방법이 여러 개 일 수도 있으니가 만들었겄지. 끝이야. 진짜 끝은 아니지. 내가 하고 싶은 말은 결국 시큐리티는 필터 덩어리야. 로그인 매커니즘에 맞게 미리 구조화 해둔 필터 덩어리.

자 그럼 로그인의 핵심은 뭐야. '인증'과 '인가'다. 스프링 시큐리티는 현재 사용되는 거의 모든 로그인 매커니즘을 지원해.

* Username and Password - how to authenticate with a username/password

* OAuth 2.0 Login - OAuth 2.0 Log In with OpenID Connect and non-standard OAuth 2.0 Login (i.e. GitHub)

이 두가지가 대표적이고.

내가 지금 말하고 싶은 건 이거. 당연히 매커니즘 마다 구현 방법이 달라. 근데 일맥상통하는 부분이 있어. 일단 들어봐. 일단 들어봐. 결국에는 스프링 시큐리티가 ```SecurityContext``` 에 ```Authentication``` 이란 객체를 저장하거든? 이럼 '인가' 된거야.

OAuth2 + jwt를 사용하나 Username and Password + 세션 or 쿠키 or jwt를 사용하나 결국 '애플리케이션 내에서의' 인가는 스프링 시큐리티가 ```SecurityContextHolder``` 로 ```SecurityContext``` 에 ```Authentication``` 을 담는 방식으로 처리해. 그니까 우리가 이런 중요한 것들을 좀 알면 뭔 로그인이고 다 글 몇 개 읽으면 슈슈슉 구현할 수 있어.

이번엔 인증에 대해서 이야기 해보자. 인증이란 게 에초에 db에 이 유저가 있냐 없냐 아니야. 그래서 OAuth2면 뭐 해당 리소스 서버에서 준 username이나 id로 db를 조회할 거고. Username and Password면 db에서 username으로 검색하고 비번 디코딩하고 뭐 이런 과정을 거쳐서 인증하겠지.

이 인증은 ```AuthenticationManager``` 가 ```AuthenticationProvider``` 시켜서 처리해. Username and Password 방식으로 인증을 처리하기 위한 ```AuthenticationProvider``` 가 이미 있고 Oauth2 방식으로 로그인을 위한 것도 이미 있어.

그 안의 처리 내용은 다 다르겠지. jwt 쓸거면 이거 관련 코드도 넣어야 하고. 근데 그 흐름은 꽤 비슷하단 거야. 이 추상화 된 시나리오를 알면 커스텀도 하고 고급 개발자가 될 수 있어.

1. 애플리케이션 내에서의 인가는 ```SecurityContextHolder``` 로 ```SecurityContext``` 에 ```Authentication``` 이 있냐 없냐다. 
2. 인증은 ```AuthenticationManager``` 가 ```AuthenticationProvider``` 시켜서 처리한다.
3. ```ExceptionTranslationFilter```, ```AuthenticationSuccessHandler```, ```AuthenticationFailureHandler``` 가 뭔가 예외나 성공 이후를 담당한다.

이제 다시 가보자.

# Authentication





