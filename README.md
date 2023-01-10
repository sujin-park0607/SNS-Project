# SNS 만들기



## ERD - 데이터베이스
![image](/uploads/32a5a229a6402d623ed3e8e1a34b0e10/image.png){: width="300" height="500"}

<br>
<br>

---

## Architecture - 아키텍처
![image](/uploads/9986fcccd9de094a5f8a771757d327d0/image.png)
<br>
<br>

---

## Technical Stec - 기술 스택
<img src="https://img.shields.io/badge/Springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/AWS ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black">
<img src="https://img.shields.io/badge/gitLab-FC6D26?style=for-the-badge&logo=gitLab&logoColor=white">
<br>
<br>

---

## Check List - 체크 리스트
### 1주차
- [x] gitLab CI / CD 자동화
- [x] AWS EC2 배포
- [x] 리눅스 배포 스크립트 및 crontab 적용
- [x] Swagger를 이용하여 API 문서 자동화
- [x] 회원가입
- [x] 로그인 - JWT Token 사용
- [x] 포스트 작성, 수정, 삭제, 단건조회, 전체조회 구현
- [x] controller 테스트 코드 구현
- [x] service 테스트 코드 구현
- [x] Admin 권한 및 승격 로직 구현
- 리펙토링
  - [x] BaseEntity 추가
  - [x] 공통로직 클래스화 - validate 클래스
### 2주차
- 댓글
  - [x] 댓글 목록 조회
  - [x] 댓글 작성
  - [x] 댓글 수정/ 삭제
  - [x] 댓글 controller 테스트
  - [ ] 댓글 service 테스트
- 좋아요
  - [x] 좋아요 누르기
  - [x] 좋아요 조회
  - [x] 좋아요 controller 테스트
  - [ ] 좋아요 service 테스트
- 마이피드
  - [x] 마이피드 조회
  - [x] 마이피드 controller 테스트
  - [ ] 마이피드 service 테스트
- 알람
  - [x] 알람 리스트 조회
  - [x] 알람 삭제
- 기타
  - [x] soft delete 적용
  - [ ] soft delete 테스트
  - [x] Post 삭제되었을 때 처리
<br>
<br>

---

## End Point - 엔드 포인트
| Http   | URL                                      | 설명      |
|--------|------------------------------------------|---------|
| POST   | /api/v1/users/join                       | 회원가입    |
| POST   | /api/v1/users/login                      | 로그인     |
| GET    | /api/v1/posts                            | 전체 조회   |
| GET    | /api/v1/posts/{postId}                   | 상세 조회   |
| POST   | /api/v1/posts                            | 게시물 등록  |
| PUT    | /api/v1/posts/{postId}                   | 게시물 수정  |
| DELETE | /api/v1/posts/{postId}                   | 게시물 삭제  |
| POST   | /api/v1/users/{userId}/role/change       | 권한 승격   |
| GET    | /api/v1/posts/{postId}/comments[?page=0] | 댓글 조회   |
| POST   | /api/v1/posts/{postsId}/comments         | 댓글 작성   |
| PUT    | /api/v1/posts/{postId}/comments/{id}     | 댓글 수정   |
| DELETE | /api/v1/posts/{postsId}/comments/{id}    | 댓글 삭제   |
| POST   | /api/v1/posts/{postId}/likes             | 좋아요 누르기 |
| GET    | /api/v1/posts/{postsId}/likes            | 좋아요 개수  |
| GET    | /api/v1/posts/my                         | 마이피드    |
| GET    | /api/v1/users//alarms                    | 알람      |
<br>
<br>

---

## Swagger
http://ec2-13-125-151-215.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/index.html

<br>
<br>

---

## 1주차 미션 요약
[접근 방법]
- 처음에 User과 Security에 대한 코드는 수업 로그와 유튜브를 기반으로 복습하는 형태로 구현했다.
- Put, Delete는 수업에서 진행하지 않았던 부분이였기에 공식문서와 블로그를 기반으로 공부하여 구현했다.
- 당일날의 미션들을 모두 완수하는 것이 목표였으며 1주차는 성공적으로 완수하였다.
- 테스트코드는 공부가 더욱 필요하였기에 Mock, Mockito에 관련된 유튜브 및 블로그들을 통해 기본부터 공부하며 구현하였다.
- 최종적으로 도전과제인 Admin까지 구현할 수 있었고, 주어진 기본 요구사항들은 모두 구현할 수 있었다.

[특이 사항]
- 구현 과정에서 테스트코드를 작성하는 부분에서 매우 어려움을 겪었으며, Mock이라는 객체는 어떠할때 사용해야하는지 왜 사용하는지에 대한 의문이 많이 들었다.
- 추후 리팩토링시 우선 테스트코드를 단위테스트로 잘 짜였는가를 확인하고싶으며, 효율적이게 리팩토링 하고싶다.
- Dto파일 분리에 대한 리펙토링도 진행하고싶다.

## 2주차 미션 요약
[접근 방법]
- 간단한 스프린트 계획서를 작성하여서 기록하며 진행하였다.(https://programming-with-sujin.notion.site/SNS-4f406de47d4443e4882ed0e8abf2a265)
- 기존의 배웠던 내용으로 MVC 패턴을 나누어 진행하였다.
- soft delete를 위해 @SQLDelete, @Where을 적용하여 구현하였다.

[특이 사항]
- user와 post를 확인하는 공통 로직이 존재했기에 따로 클래스 분리를 통해 메서드를 재활용할 수 있도록 리펙토링해주었다.(https://alcoholble.tistory.com/6)
- post삭제 시 comment와 like 삭제를 진행할 때 두가지의 방법으로 구현할 수 있었는데 casCade를 사용하여 삭제해주었고, soft delete와 함께 사용이 가능하다는것을 배웠다.(https://alcoholble.tistory.com/12)
- 좋아요 취소 기능을 구현하기 위해서 로직을 짰고 리펙토링이 판단하다고 생각하여 메서드분리를 통해 클린코드를 실천하도록 노력하였다.(https://alcoholble.tistory.com/14)
- 가장 중요한 로직을 확인하는 service테스트를 진행하지 못해서 아쉬움이 남는다