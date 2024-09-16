package YOURSSU.assignment.service.user;

import org.springframework.stereotype.Service;

import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.UserRequest.UserSignUpRequest;
import YOURSSU.assignment.dto.response.UserResponse.UserSignUpResponse;
import YOURSSU.assignment.global.exception.GlobalErrorCode;
import YOURSSU.assignment.global.exception.GlobalException;
import YOURSSU.assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserSignUpResponse signUp(UserSignUpRequest request) {
        userRepository
                .findByEmail(request.getEmail())
                .ifPresent(
                        user -> {
                            throw new GlobalException(GlobalErrorCode.USER_ALREADY_EXISTS);
                        });
        User user =
                User.builder()
                        .email(request.getEmail())
                        .password(request.getPassword()) // 암호화 필요
                        .username(request.getUsername())
                        .build();
        userRepository.save(user);
        return UserSignUpResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }
}
