# HandlerAdapter
```HandlerAdapter``` 는 핸들러를 실행하는 역할을 함. 말했던 것처럼 핸들러마다 생긴 게 달라서 ```HandlerMapping``` 이란 통을 만들어서 담아뒀음.

에초에 왜 담아뒀냐? 딱 스타일에 맞게 최적화해서 처리하려고 담았찌! 그 역할이 ```HandlerAdapter```

그니까 ```DispatcherSevlet``` 에서 요청에 대응하는 핸들러 통에서 꺼내면?? 그 핸들러 실행시킬 수 있는 ```HandlerAdapter```가 실행을 시키는 구조.

다른 건 거의 몰라도 ```RequestMappingHandlerAdapter``` 에 대해서는 알고 쓰자.

```RequestMappingHandlerAdapter``` 가 이제 핸들러 메서드 방식으로 핸들러 사용할 때 핸들러 실행을 담당하는 녀석임.

핸들러 메서드를 실행시킬려면 핸들러 메서드에서 인자로 받는 객체나 뭐 그냥 Primitive일 수도 있고. 어째튼 메서드가 필요한 인자들을 넘겨 줘야함. return 값도 다 다름.

메서드마다 요구사항이 다를텐데 이걸 맞추려고 ```RequestMappingHandlerAdapter``` 는 ```HandlerMethodArgumentResolver``` 에 의존함. 응답은 ```HandlerMethodReturnValueHandler``` 에 의존.

그냥 인터페이스고. 핸들러 메서드에서 받을 수 있는 모든 타입에 대응하는 리졸버들이 스프링 빈에 있으니 걱정말 것.

커스텀도 가능함.

