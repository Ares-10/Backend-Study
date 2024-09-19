package YOURSSU.assignment.service.user;

import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.UserRequest.*;
import YOURSSU.assignment.dto.response.UserResponse.UserSignUpResponse;

public interface UserService {
    UserSignUpResponse signUp(UserSignUpRequest request);

    User getUserByEmail(String email);

    void withdraw(User user);
}
