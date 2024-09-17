package YOURSSU.assignment.service.user;

import org.springframework.stereotype.Service;

import YOURSSU.assignment.converter.UserConverter;
import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.UserRequest.UserSignUpRequest;
import YOURSSU.assignment.dto.request.UserRequest.UserWithdrawRequest;
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
        String password = request.getPassword(); // 암호화 필요
        User user = UserConverter.toUser(request, password);
        userRepository.save(user);
        return UserConverter.toUserSignUpResponse(user);
    }

    @Override
    public User getUser(String email, String password) {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.USER_NOT_FOUND));
        if (!user.getPassword().equals(password))
            throw new GlobalException(GlobalErrorCode.EMAIL_PASSWORD_MISMATCH);
        return user;
    }

    @Override
    public User getUser(Long id) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.USER_NOT_FOUND));
        return user;
    }

    @Override
    public void withdraw(UserWithdrawRequest request) {
        User user = getUser(request.getEmail(), request.getPassword());
        userRepository.delete(user);
    }
}
