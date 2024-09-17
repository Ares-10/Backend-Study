package YOURSSU.assignment.global.auth.handler.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import YOURSSU.assignment.global.auth.handler.annotation.ExtractToken;
import YOURSSU.assignment.global.auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExtractTokenArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(String.class)
                && parameter.hasParameterAnnotation(ExtractToken.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {

        String refreshToken = webRequest.getHeader("Authorization");
        jwtTokenProvider.isValidToken(refreshToken);
        return refreshToken;
    }
}
