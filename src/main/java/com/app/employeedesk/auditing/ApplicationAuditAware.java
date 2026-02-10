package com.app.employeedesk.auditing;

import com.app.employeedesk.entity.UserDetails;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<String> {
    @Override
    @NonNull



    public Optional<String> getCurrentAuditor() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null|| !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken){
            return Optional.of("unauthorized");
        }
        UserDetails userPrincipal=(UserDetails) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getName());
    }
}
