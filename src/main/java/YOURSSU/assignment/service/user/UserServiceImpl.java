package YOURSSU.assignment.service.user;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserSignUpResponse signUp(UserSignUpRequest request) {
        userRepository
                .findByEmail(request.getEmail())
                .ifPresent(
                        user -> {
                            throw new GlobalException(GlobalErrorCode.USER_ALREADY_EXISTS);
                        });
        // 비밀번호 암호화
        String password = passwordEncoder.encode(request.getPassword());
        User user = UserConverter.toUser(request, password);
        userRepository.save(user);
        return UserConverter.toUserSignUpResponse(user);
    }

    @Override
    public User authenticateUser(String email, String password) {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.USER_NOT_FOUND));
        // 암호화된 비밀번호 매칭
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new GlobalException(GlobalErrorCode.EMAIL_PASSWORD_MISMATCH);
        return user;
    }

    @Override
    public User getUser(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void withdraw(UserWithdrawRequest request) {
        User user = authenticateUser(request.getEmail(), request.getPassword());
        userRepository.delete(user);
    }
}
