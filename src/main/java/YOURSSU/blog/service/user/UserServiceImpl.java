package YOURSSU.blog.service.user;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import YOURSSU.blog.converter.UserConverter;
import YOURSSU.blog.domain.User;
import YOURSSU.blog.dto.request.UserRequest.UserSignUpRequest;
import YOURSSU.blog.dto.response.UserResponse.UserSignUpResponse;
import YOURSSU.blog.global.exception.GlobalErrorCode;
import YOURSSU.blog.global.exception.GlobalException;
import YOURSSU.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserSignUpResponse signUp(UserSignUpRequest request) {
        if (userRepository.existsByEmailOrUsername(request.getEmail(), request.getUsername()))
            throw new GlobalException(GlobalErrorCode.USER_ALREADY_EXISTS);

        // 비밀번호 암호화
        String password = passwordEncoder.encode(request.getPassword());
        User user = UserConverter.toUser(request, password);
        userRepository.save(user);
        return UserConverter.toUserSignUpResponse(user);
    }

    @Override
    public User getUserByEmail(String email) {
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
