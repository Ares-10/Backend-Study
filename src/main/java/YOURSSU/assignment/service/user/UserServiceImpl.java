package YOURSSU.assignment.service.user;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import YOURSSU.assignment.converter.UserConverter;
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
    public User getUser(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void withdraw(User user) {
        userRepository.delete(user);
    }
}
