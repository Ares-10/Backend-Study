package YOURSSU.blog.service.user;

import YOURSSU.blog.domain.User;
import YOURSSU.blog.dto.request.UserRequest.*;
import YOURSSU.blog.dto.response.UserResponse.UserSignUpResponse;

public interface UserService {
    UserSignUpResponse signUp(UserSignUpRequest request);

    User getUserByEmail(String email);

    void withdraw(User user);
}
