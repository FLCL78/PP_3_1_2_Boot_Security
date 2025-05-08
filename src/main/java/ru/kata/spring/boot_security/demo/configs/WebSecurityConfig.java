package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.UserService;

@EnableWebSecurity
@Configuration

public class  WebSecurityConfig {

    private final SuccessUserHandler successUserHandler;
    private final UserService userService;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserService userService) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http.csrf(csrf -> csrf.disable()) // пока своей страницы аутентификации
                        .authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").hasRole("ADMIN") // админка только админам
                                .requestMatchers("/user/{id}").access((authentication, object) -> { // как итог доступ только на свою страницу
                                                String nameAuth = authentication.get().getName(); // имя аутентифицированного пользователя
                                                String path = object.getRequest().getRequestURI(); // путь запроса
                                                Long pathId = Long.valueOf(path.substring(path.lastIndexOf("/") + 1)); // id из запроса

                                                User user = userService.show(pathId); // достаем юзера по Id из запроса
                                                boolean isOwner = user.getUsername().equals(nameAuth); // определяем равенство юзера из запроса и нашего

                                                return new AuthorizationDecision(isOwner); // решение вопроса(true - хорошо, false - нет доступа) Т_Т хочется плакать
                                })
                                .anyRequest().authenticated())
                .formLogin(form -> form.successHandler(successUserHandler).permitAll())
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());
                return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance() ;
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/", "/index").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().successHandler(successUserHandler)
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll();
//    }
//
//    // аутентификация inMemory
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("user")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
}