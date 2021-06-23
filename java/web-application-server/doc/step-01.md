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

# 요구사항 2
회원가입 메뉴를 클릭하면 http://localhost:8080/user/form.html로 이동하면서 회원가입할 수 있다

# 요구사항 3
post 메소드 처리하여 회원가입 진행
```text
userId=xxx&password=xxx&name=xxx&mail=xxx
```
* 본문에 담긴 내용 처리
* post는 http 헤더 이후 빈공백을 지난 다음 줄부터 시작(line)
* 회원가입시 입력한 모든 데이터를 추출해서 User 객체 생성하기

# 요구사항 4
회원 가입 완료 시 페이지 이동 하기
* 302 status
* 브라우저의 url도 /user/create -> /index.html로 변경해야 한다
```java
// 302를 통해 redirect 시켜준다. 브라우저가 알아서 이동시켜줌
dos.writeBytes("HTTP/1.1 302 Found \r\n");
dos.writeBytes("Location: " + "http://localhost:8888/index.html" + "\r\n");
dos.writeBytes("\r\n"); "\r\n");
```
* 테스트 코드를 작성하며 다시 한 스텝씩 가보자
