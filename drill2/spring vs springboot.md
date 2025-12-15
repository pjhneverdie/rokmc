# spring은 was가 아니다
spring은 boot처럼 내장 tomcat이 없음. spring은 '자바 어플래케이션 개발을 위한 의존성 주입 프레임워크' 

밑을 보자. 원래 spring은 톰켓이 없으니까 톰켓을 따로 띄우고 톰켓에 서블릿 등록할 때 org.springframework.web.servlet.DispatcherServlet로 스프링의 DispatcherServlet을 등록해서 연결치는 구조임.

```xml
<servlet>
    <servlet-name>dispatcher</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring-servlet.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>dispatcher</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>

```

갑자기 시큐리티 이야기지만. 필터도 똑같음. 

필터도 시큐리티가 서블릿 필터 api 구현해서 톰켓 필터 목록에 넣어서 스프링시큐리티 필터체인이랑 연결 시키는 거임 

```xml
<filter>
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

이 말을 하는 이유가. spring은 내장 톰켓이 없어서 수동으로 할 작업이 많다는 거임.

boot는 프레임워크 자체가 톰켓을 내장하니까 환경설정, 배포 이런 게 훨씬 쉬움. 즉 스프링 편의성 버전이라고 보면 됨.