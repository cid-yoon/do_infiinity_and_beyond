# 웹 서버 시작 및 테스트

webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.

사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.

WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리

구현 단계에서는 각 요구사항을 구현하는데 집중한다.

구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.

각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다.

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답

무엇을 느끼고 학습했는가?
```markdown
최소한의 코드 & 테스트로 동작 만들기
요구사항에 따라서 만들어 보기

InputStream의 제어, 버퍼스트림을 통해 한줄씩 읽기
- http 프로토콜의 메시지는 문자열로 온다
- 첫번째 줄 내용은 메소드 이름, 경로, 프로토콜 형식이 온다
- 첫번째 줄을 통해 어떤 일을 할지 판단 할 수 있다

앱 환경의 파일을 읽을 수 있다
- 파일 객체를 통해 Files 유틸을 사용하여 byte로 변환할 수 있다
- 바이트로 변환된 데이터는 소켓을 통해 전달 될 수 있다
```

### 요구사항 2 - get 방식으로 회원가입
회원가입 메뉴를 클릭하면 /user/form.html으로 이동하면서 회원가입을 할 수 있다
회원 가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.
```http request
/user/create?userId=javajigi&password=password&name=Jaesung&email=javagigi%40slipp.net
```
HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱(문자열을 원하는 형태로 분리하거나 조작하는 것을 의미)해 model.User 클래스에 저장한다

```markdown
Http 요청의 첫번째 라인에서 요청 URL을 추출한다
요청 Url에서 접근경로와 이름=값으로 전달되는 데이터를 추출해 User 클래스에 담는다
구현은 가능하면 Junit을 활용해 단위 테스트를 진행하면서 하면 좀더 효과적으로 개발 가능하다
이름=값 파싱은 util.HttpRequestUtils 클래스의 parseQueryString 메소드를 활용한다
요청 Url과 이름을 각각 분리해야 한다
* string url = "/?data=234"
* int index = uri.indexOf("?")
* string reuqestPath = url.substring(0,index)
* String params = url.substring(index+1)
```
무엇을 느끼고 학습했는가?
```markdown
테스트 코드 먼저 만들어 보기
요구사항이 발생할 때마다 테스트 & 기능 늘리기
쿼리 스트링은  ? 문자로 스트링을 전달하며 해당 데이터의 값은 key=value 형태로 전달된다
- key=value는 여러 값이 중첩되어 올 수 있으며 해당 값은 & 연산자로 구분할 수 있다

split 메소드의 경우 특수 문자를 분해하기 위해서는 [?] 또는 //? 같은 처리를 해 주어야 한다
성능적으로 분리될때마다 객체가 생성되기에 indexOf를 통한 subString이 미묘하게 좋을 듯?


아주 단순한 내용이지만 프로토콜을 하나씩 훓어가는 재미가 있다
- 커밋을 좀 더 자주 했어야 하는데 아쉬웠다
```

### 요구사항 3 - post 방식으로 회원가입
form 파일의 form 태그 method를 get에서 post로 수정한 후 회원 가입이 정상적으로 동작하도록 구현
```markdown
POST /user/create HTTP1.1
Host: localhost:8080
Connection: keep-aliver
Content-Length: 59
Content-Type: application/x-www-form-urlencoded
Accept: */*


```

### 요구사항 4 - redirect 방식으로 이동

### 요구사항 5 - cookie

### 요구사항 6 - stylesheet 적용

### heroku 서버에 배포 후