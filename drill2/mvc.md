# mvc의 등장
only jsp 방식은 결국 html 파일에 비즈니스 로직 넣어야 함.

그래서 1. 비즈니스 로직은 서블릿에서 처리하고 html에서 써야 하는 속성들을 request.setAttribute로 세이브, 2. jsp에서 이걸 꺼내 쓰는 구조를 만들어 냈음.

글고 이렇게 비즈니스 로직을 분리하면. 프론트에서 오타 나거나 실수 했을 때 서버를 재시작하지 않고 jsp만 고치면 서버 안 끄고 유지보수가 가능. (only jsp 시절에는 비즈니스 로직이랑 html이 같이 있으니까 뭐 하나라도 고치려면 서버를 껐다 켜야 했으)

```java
public class UserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Model에서 데이터 가져오기 (간단하게 예시)
        String username = "ChatGPT";
        int age = 5;

        // request 객체에 데이터 담기
        request.setAttribute("username", username);
        request.setAttribute("age", age);

        // View(JSP)로 포워딩
        RequestDispatcher dispatcher = request.getRequestDispatcher("user.jsp");
        dispatcher.forward(request, response);
    }
}

```

```jsp
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>사용자 정보</title>
    <link rel="stylesheet" href="style.css">
    <script src="script.js"></script>
</head>
<body>
    <h1>사용자 정보</h1>
    <p>사용자 이름: ${username}</p>
    <p>나이: ${age}</p>
</body>
</html>
```

# 안 씁니다..
결국 서블릿에서 하는 게 너무 많다 보니까 단위 테스트가 힘들고, jsp 자체가 너무 복잡함 가독성도 별로고.

mvc에서 m==모델은 그냥 순수하게 도메인이라기 보다는 그냥 db에서 조회하고 그런 비즈니스 로직을 의미

v는 view==jsp고

c는 controller==서블릿

그니까 cm이 너무 붙어 있다는 거임.

그리고 요청에 대한 공통 부분 처리가 여전히 안 되고 있음. 로깅, 예외처리 이런 것들을 서블릿 하나하나 다 또 작성해야 함 ㅠ
