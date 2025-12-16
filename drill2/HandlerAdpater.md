# HandlerAdapter
```HandlerAdapter``` 는 핸들러를 실행하는 역할을 함. 말했던 것처럼 핸들러마다 생긴 게 달라서 ```HandlerMapping``` 이란 통을 만들어서 담아뒀음.

에초에 왜 담아뒀냐? 딱 스타일에 맞게 최적화해서 처리하려고 담았찌! 그 역할이 ```HandlerAdapter```

그니까 ```DispatcherSevlet``` 에서 요청에 대응하는 핸들러 통에서 꺼내면?? 그 핸들러 실행시킬 수 있는 ```HandlerAdapter```가 실행을 시키는 구조.


