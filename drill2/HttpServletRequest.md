# HttpServletRequest
http 요청에 대한 모든 정보를 여기에 담는 거임. HttpServletRequest는 서블릿 api고, 톰켓이 http 요청 읽어서 HttpServletRequest를 생성해서 서블릿에 넘겨 줌.

즉 톰켓이 서블릿 api를 구현했다 보면 댐.

톰켓 직접적으로 안 만져봤으나 딱봐도 서블릿 싱글톤으로 생성하고 스레드 생성해서 서블릿 인스턴스.service(톰켓이 만든 HttpServletRequest) 이렇게 처리하는 구조일 듯.

https://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html <- 서블릿 api

여기서 찾아서 쓰면 됨. 그냥 개발하면 자연스럽게 자주 쓰는 거 외우게 됨.