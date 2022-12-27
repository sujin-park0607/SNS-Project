# SNS 만들기

---


### ERD - 데이터베이스
![image](/uploads/32a5a229a6402d623ed3e8e1a34b0e10/image.png)

<br>
<br>

### Architecture - 아키텍처
![image](/uploads/9986fcccd9de094a5f8a771757d327d0/image.png)
<br>
<br>

### Technical Stec - 기술 스택
<img src="https://img.shields.io/badge/Springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/AWS ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black">
<img src="https://img.shields.io/badge/gitLab-FC6D26?style=for-the-badge&logo=gitLab&logoColor=white">
<br>
<br>

### Check List - 체크 리스트
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

### End Point - 엔드 포인트
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

### 결과물
http://ec2-13-125-151-215.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/index.html
