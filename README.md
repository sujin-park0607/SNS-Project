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
- [ ] UI 구현
<br>
<br>

---

## End Point - 엔드 포인트
| Http   | URL                                | 설명     |
|--------|------------------------------------|--------|
| POST   | /api/v1/users/join                 | 회원가입   |
| POST   | /api/v1/users/login                | 로그인    |
| GET    | /api/v1/posts                      | 전체 조회  |
| GET    | /api/v1/posts/{postId}             | 상세 조회  |
| POST   | /api/v1/posts                      | 게시물 등록 |
| PUT    | /api/v1/posts/{postId}             | 게시물 수정 |
| DELETE | /api/v1/posts/{postId}             | 게시물 삭제 |
| POST   | /api/v1/users/{userId}/role/change | 권한 승격  |
<br>
<br>

---

## Swagger
http://ec2-13-125-151-215.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/index.html

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