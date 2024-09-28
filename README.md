# Blog

## 기술 스택
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&lohttps://github.com/Ares-10/Backend-Study/blob/main/README.mdgo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)  
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white)  
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![MockMvc](https://img.shields.io/badge/MockMvc-02569B?style=for-the-badge)

## 프로젝트 구조
<img width="422" alt="image" src="https://github.com/user-attachments/assets/8053c962-922a-495b-b883-f70c6c28054b">


## API Endpoints

| 동작      | URI                                      | Method |
| --------- | ------------------------------------------ | ------ |
| 회원 가입 | `/api/users/signup`                      | POST   |
| 회원 탈퇴 | `/api/users/withdraw`                   | DELETE |
| 로그인    | `/api/auth/login`                      | POST   |
| 토큰 리프레시 | `/api/auth/refresh`              | POST   |
| 글 작성   | `/api/articles/`                                | POST   |
| 글 수정   | `/api/articles/{id}`                           | PUT    |
| 글 삭제   | `/api/articles/{id}`                           | DELETE |
| 댓글 작성 | `/api/articles/{articleId}/comments/`           | POST   |
| 댓글 수정 | `/api/articles/{articleId}/comments/{commentId}` | PUT    |
| 댓글 삭제 | `/api/articles/{articleId}/comments/{commentId}` | DELETE |


## 설치 및 실행 방법
1. 레포지토리를 클론합니다.
    ```bash
    git clone https://github.com/yourusername/yourproject.git
    cd YOURSSU-Recruitment-Task
    ```

2. Gradle 빌드 도구를 사용하여 종속성을 설치합니다.

3. `application.yml`에서 프로젝트 설정을 구성합니다.  
   - {DATASOURCE_URL}
   - {DATASOURCE_USERNAME}
   - {DATASOURCE_PASSWORD}
   - {JWT_SECRET_KEY}
   - {JWT_ACCESS_TOKEN_TIME}
   - {JWT_REFRESH_TOKEN_TIME}

5. 프로젝트를 실행합니다.

실행 오류 시 다음을 참고해주세요.  
https://campus-coder.tistory.com/162
