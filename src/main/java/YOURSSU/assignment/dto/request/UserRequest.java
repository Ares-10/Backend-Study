package YOURSSU.assignment.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.*;

public class UserRequest {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserSignUpRequest {
        @NotBlank
        @Email(message = "이메일 형식이 잘못되었습니다")
        private String email;

        @NotBlank private String password;
        @NotBlank private String username;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserWithdrawRequest {
        @NotBlank
        @Email(message = "이메일 형식이 잘못되었습니다")
        private String email;

        @NotBlank private String password;
    }
}
