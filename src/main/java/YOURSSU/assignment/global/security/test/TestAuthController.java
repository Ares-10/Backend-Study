package YOURSSU.assignment.global.security.test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import YOURSSU.assignment.global.security.test.dto.TestAuthResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Test Auth", description = "인증/인가 테스트 용")
public class TestAuthController {

    private final TestAuthService testAuthService;

    @PostMapping("/login")
    public ResponseEntity<TestAuthResponse> login(@RequestParam(value = "email") String email) {
        return new ResponseEntity<>(testAuthService.login(email), HttpStatus.OK);
    }
}
