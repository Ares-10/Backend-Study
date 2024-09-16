package YOURSSU.assignment.service.user;

import YOURSSU.assignment.dto.request.UserRequest.UserSignUpRequest;
import YOURSSU.assignment.dto.response.UserResponse.UserSignUpResponse;

public interface UserService {
    UserSignUpResponse signUp(UserSignUpRequest request);
}
