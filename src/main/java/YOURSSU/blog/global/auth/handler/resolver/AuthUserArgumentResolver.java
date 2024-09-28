package YOURSSU.blog.global.auth.handler.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import YOURSSU.blog.domain.User;
import YOURSSU.blog.global.auth.handler.annotation.AuthUser;
import YOURSSU.blog.global.exception.GlobalErrorCode;
import YOURSSU.blog.global.exception.GlobalException;
import YOURSSU.blog.service.user.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class)
                && parameter.hasParameterAnnotation(AuthUser.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        // 현재 SecurityContext에서 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = null;
        // 인증 정보가 있을 경우 처리
        if (authentication != null) {
            // 익명 사용자일 경우 예외 발생 (로그인되지 않은 사용자)
            if (authentication.getName().equals("anonymousUser"))
                throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
            // principal 객체에 인증된 사용자 정보 할당
            principal = authentication.getPrincipal();
        }
        // 인증된 사용자가 없거나, principal이 String 타입(익명 사용자)인 경우 예외 발생
        if (principal == null || principal.getClass() == String.class) {
            throw new GlobalException(GlobalErrorCode.USER_NOT_FOUND);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) authentication;
        String email = authenticationToken.getName();
        return userService.getUserByEmail(email);
    }
}
