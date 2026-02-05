# 스프링부트와 리액트 통합 테스트를 위한 템플릿 프로젝트

- 데이터베이스는 메모리 기반 h2 데이터베이스를 사용

- 인증/인가 기능 추가 (구글 OAuth2 인증 포함)

- Download ZIP으로 전체 코드을 다운로드한 뒤 압축 해제

# 스프링부트

  . STS4를 실행해서 압축을 푼 폴더를 작업공간으로 설정

  . src/main/resources 폴더에 oauth2.properties 파일 생성

      # myaccountid ==> 자신의 github 아이디로 변경
      frontend.base.uri=https://myaccountid.github.io

      # Google OAuth 설정
      spring.security.oauth2.client.registration.google.client-id=****
      spring.security.oauth2.client.registration.google.client-secret=****
      spring.security.oauth2.client.registration.google.scope=email,profile

      # Redis를 사용한다면
      spring.data.redis.host=localhost
      spring.data.redis.port=6379

      # https 설정
      server.port=8443
      server.ssl.key-store=classpath:keystore.p12
      server.ssl.key-store-password=****
      server.ssl.key-store-type=PKCS12
      server.ssl.key-alias=myserver

# 리액트

  . VSCode를 실행 [파일]-[폴더 열기]에서 myprojectfront 선택

  . 터미널을 열어서 (Ctrl + `) 'npm install' 실행해서 필요한 라이브러리 다운로드

  . 'npm start'로 실행
