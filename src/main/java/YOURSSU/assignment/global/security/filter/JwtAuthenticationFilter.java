package YOURSSU.assignment.global.security.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import YOURSSU.assignment.global.exception.GlobalErrorCode;
import YOURSSU.assignment.global.exception.GlobalException;
import YOURSSU.assignment.global.security.principal.PrincipalDetailsService;
import YOURSSU.assignment.global.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final PrincipalDetailsService principalDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            if (jwtTokenProvider.isValidToken(token)) {
                String email = jwtTokenProvider.getEmail(token);
                UserDetails userDetails = principalDetailsService.loadUserByUsername(email);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, "", userDetails.getAuthorities());
                    SecurityContextHolder.getContext()
                            .setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    throw new GlobalException(GlobalErrorCode.USER_NOT_FOUND);
                }
            } else {
                throw new GlobalException(GlobalErrorCode.AUTH_INVALID_TOKEN);
            }
        }
        filterChain.doFilter(request, response);
    }
}
