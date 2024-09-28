package blog.dto.response;

import lombok.*;

public class UserResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserSignUpResponse {
        private String email;
        private String username;
    }
}
