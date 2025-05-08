package ru.kata.spring.boot_security.demo.configs;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.OurUserDetailsService;

import java.io.IOException;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    private final HttpServletResponse httpServletResponse;
    private final OurUserDetailsService userDetailsService;

    @Autowired
    public SuccessUserHandler(HttpServletResponse httpServletResponse, OurUserDetailsService userDetailsService) {
        this.httpServletResponse = httpServletResponse;
        this.userDetailsService = userDetailsService;
    }

    // Spring Security использует объект Authentication, пользователя авторизованной сессии.
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
//        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
//        if (roles.contains("ROLE_USER")) {
//            httpServletResponse.sendRedirect("/user");
//        } else {
//            httpServletResponse.sendRedirect("/");
//        }
//    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());


        // Добыча Id
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();
        User user  = (User) userDetailsService.loadUserByUsername(userName);
        Long userId = user.getId();


        if(roles.contains("ROLE_ADMIN")) {
            httpServletResponse.sendRedirect("/admin");
        } else if(roles.contains("ROLE_USER")) {
            httpServletResponse.sendRedirect("/user/" + userId);
        } else {
            httpServletResponse.sendRedirect("/");
        }
    }
}