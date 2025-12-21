
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

여기서 이제 ```AuthenticationProvider``` 랑 ```UserDetailService``` 이런 것들 알고 가야 해. 

이거랑 시큐리티 컨텍스트가 이제 OAuth2로 로그인을 만드나 아이디 패스워드로 로그인을 만드나 시큐리티 쓰면 일맥상통하는 부분이지.

처음만 다르지 결국 마지막은 얘네가 하더라고. ```AuthenticationSuccessHandler```, ```AuthenticationFailureHandler ``` 까지 해서 공부하면 시큐리티 그래도 잘 응용할 수 있을 거야.

나중에 jwt 써도 결국 얘네니까.