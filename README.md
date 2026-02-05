스프링부트와 리액트 통합 테스트를 위한 템플릿 프로젝트
데이터베이스는 메모리 기반 h2 데이터베이스를 사용

인증/인가 기능 추가 (구글 OAuth2 인증 포함)

테스트 방법
Download ZIP으로 전체 코드을 다운로드한 뒤 압축 해제

스프링부트

. STS4를 실행해서 압축을 푼 폴더를 작업공간으로 설정

. 서버 실행 (OAuth2 인증 테스트를 하려면 src/main/resources/oauth2.properties 설정 수정한 뒤 서버 실행)

리액트

. VSCode를 실행 [파일]-[폴더 열기]에서 myprojectfront 선택

. 터미널을 열어서 (Ctrl + `) 'npm install' 실행해서 필요한 라이브러리 다운로드

. 'npm start'로 실행
