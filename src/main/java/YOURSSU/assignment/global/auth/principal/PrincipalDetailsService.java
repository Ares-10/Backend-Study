package YOURSSU.assignment.global.auth.principal;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import YOURSSU.assignment.domain.User;
import YOURSSU.assignment.global.exception.GlobalErrorCode;
import YOURSSU.assignment.global.exception.GlobalException;
import YOURSSU.assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.USER_NOT_FOUND));
        return new PrincipalDetails(user);
    }
}
