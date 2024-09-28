package blog.service.user;

import blog.domain.User;
import blog.dto.request.UserRequest.*;
import blog.dto.response.UserResponse.UserSignUpResponse;

public interface UserService {
    UserSignUpResponse signUp(UserSignUpRequest request);

    User getUserByEmail(String email);

    void withdraw(User user);
}
