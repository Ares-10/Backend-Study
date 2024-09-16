package YOURSSU.assignment.converter;

import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.UserRequest.UserSignUpRequest;
import YOURSSU.assignment.dto.response.UserResponse.UserSignUpResponse;

public class UserConverter {
    public static User toUser(UserSignUpRequest request, String password) {
        return User.builder()
                .email(request.getEmail())
                .password(password)
                .username(request.getUsername())
                .build();
    }

    public static UserSignUpResponse toUserSignUpResponse(User user) {
        return UserSignUpResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }
}
