# 요구사항 1

http://localhost:8080/index.html로 접속시 응답

어떤 URL로 접속하더라도 "Hello World" 문자열만 출력
위 주소로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다

```text
GET /index.html HTTP/1.1
HOST: localhost:8080
Connection: keep-alive
Accept: */*
```

### 힌트 1단계

