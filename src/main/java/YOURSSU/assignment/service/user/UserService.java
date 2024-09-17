package YOURSSU.assignment.service.user;

import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.UserRequest.*;
import YOURSSU.assignment.dto.response.UserResponse.UserSignUpResponse;

public interface UserService {
    UserSignUpResponse signUp(UserSignUpRequest request);

    User getUser(String email, String password);

    User getUser(Long userId);

    void withdraw(UserWithdrawRequest request);
}
