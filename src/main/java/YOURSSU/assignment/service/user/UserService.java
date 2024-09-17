package YOURSSU.assignment.service.user;

import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.UserRequest.*;
import YOURSSU.assignment.dto.response.UserResponse.UserSignUpResponse;

public interface UserService {
    UserSignUpResponse signUp(UserSignUpRequest request);

    User authenticateUser(String email, String password);

    User getUser(String email);

    void withdraw(UserWithdrawRequest request);
}
