package YOURSSU.assignment.global.security.test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.global.exception.GlobalErrorCode;
import YOURSSU.assignment.global.exception.GlobalException;
import YOURSSU.assignment.global.security.provider.JwtTokenProvider;
import YOURSSU.assignment.global.security.test.dto.TestAuthResponse;
import YOURSSU.assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestAuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TestAuthResponse login(String email) {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.USER_NOT_FOUND));

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());

        return TestAuthResponse.builder().accessToken(accessToken).build();
    }
}
