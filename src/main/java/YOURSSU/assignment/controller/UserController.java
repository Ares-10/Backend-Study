package YOURSSU.assignment.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.dto.request.UserRequest.*;
import YOURSSU.assignment.dto.response.UserResponse.*;
import YOURSSU.assignment.global.auth.handler.annotation.AuthUser;
import YOURSSU.assignment.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "유저 관련 API")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponse> signup(
            @Valid @RequestBody UserSignUpRequest request) {
        UserSignUpResponse response = userService.signUp(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "회원탈퇴")
    @DeleteMapping("/withdraw")
    public void withdraw(@Parameter(name = "user", hidden = true) @AuthUser User user) {
        userService.withdraw(user);
    }
}
