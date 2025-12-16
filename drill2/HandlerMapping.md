# HandlerMapping
스프링mvc는 프론트 컨트롤러 패턴처럼 요청 url 하나에 컨트롤러 하나로 처리하는 게 아니라

보통 뭐 도메인이나 주제에 대한 컨트롤러 클래스를 하나 만들고 거기에 요청을 처리할 메서드를 여러 개 만들어서 사용하는 구조임.

그 각각의 메서드를 핸들러라고 하는 거고. 근데 메서드만 있는 건 아님 다른 방식도 많음. 

핸들러를 만드는 방법이 여러 개이기 때문에 바로 공통 처리가 불가능. 핸들러를 만든 방법에 따라 핸들러를 분류하고 각 핸들러의 방식대로 처리해야 함.

그 분류를 위한 통이 ```HandlerMapping``` ! 그래서 ```HandlerMapping``` 종류도 여러 가지.

애플리케이션이 시작되면 ```HandlerMapping```이 리플렉션으로 클래스, 메서드 돌면서 "어? 이거 내 통에 담아야 하네" 하면서 핸들러들 정보를 모아둠.

그럼 ```DispathcerServlet``` 이 ```HandlerMapping``` 에, 정확히는 각 ```HandlerMapping``` 의 ```getHandler``` 메서드 돌리면서 ```HttpRequest``` 넘기면, ```HandlerMapping``` 들이 ```HttpRequest``` 에서 url, HTTP Method 이런 거 읽어서 자기가 가진 핸들러 중 일치하는 걸 찾아서 그 핸들러를 반환함.




