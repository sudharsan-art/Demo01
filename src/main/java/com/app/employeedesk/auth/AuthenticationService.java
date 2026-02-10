package com.app.employeedesk.auth;


import com.app.employeedesk.security.JwtService;
import com.app.employeedesk.repo.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserDetailsRepository repository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public String authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserName(),
                            request.getPassword()));
        } catch (AuthenticationException e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
        var user = repository.findByUserName(request.getUserName()).orElseThrow();
        return jwtService.generateToken(user);
    }


}
