package tacos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import tacos.entity.User;
import tacos.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity //Позволяет проверять на уровне методов, есть ли у пользователя
                //указанные привилегии, но на самом деле можно выполнить любой SpEL
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo){
        return username -> {
            User user = userRepo.findByUsername(username);
            if(user != null) return user;

            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    /*
    Методы конфигурации, определяющие правила защиты полей:
    access(String)              Разрешает доступ, если выражение на языке Spring Expression
                                Language (SpEL) дает в результате true

    anonymous()                 Разрешает доступ анонимным пользователям

    authenticated()             Разрешает доступ аутентифицированным пользователям

    denyAll()                   Запрещает доступ без всяких исключений

    fullyAuthenticated()        Разрешает доступ, если пользователь полностью аутентифицирован
                                (не был запомнен с помощью функции «запомнить меня»)

    hasAnyAuthority(String...)  Разрешает доступ, если пользователь обладает любой из перечисленных
                                привилегий

    hasAnyRole(String...)       Разрешает доступ, если пользователь обладает любой из перечисленных
                                ролей

    hasAuthority(String)        Разрешает доступ, если пользователь обладает указанной привилегией

    hasIpAddress(String)        Разрешает доступ, если запрос получен с указанного IP-адреса

    hasRole(String)             Разрешает доступ, если пользователь обладает указанной ролью

    not()                       Инвертирует значение, возвращаемое предыдущим методом в цепочке

    permitAll()                 Разрешает доступ всем без всяких условий

    rememberMe()                Разрешает доступ пользователям, аутентифицированным с помощью
                                функции «запомнить меня»
     */

    /*
    Метод access(String) принимает SpEL, вот методы для SpEL:

    authentication                  Объект аутентификации пользователя

    denyAll                         Всегда возвращает false

    hasAnyAuthority(String...
    authorities)                    Возвращает true, если пользователь обладает любой
                                    из перечисленных привилегий

    hasAnyRole(String... roles)     Возвращает true, если пользователь обладает любой
                                    из перечисленных ролей

    hasAuthority(String authority)  Возвращает true, если пользователь обладает указанной
                                    привилегией

    hasPermission(Object target,    Возвращает true, если пользователь имеет разрешение
    Object permission)              на доступ к объекту targetId

    hasPermission(Serializable      Возвращает true, если пользователь имеет разрешение
    targetId, String targetType,    на доступ к объекту targetId типа targetType
    Object permission)

    hasRole(String role)            Возвращает true, если пользователь обладает указанной ролью

    hasIpAddress(String
    ipAddress)                      Возвращает true, если запрос получен с указанного IP-адреса

    isAnonymous()                   Возвращает true, если запрос получен от анонимного
                                    пользователя

    isAuthenticated()               Возвращает true, если запрос получен от аутентифицированного
                                    пользователя

    isFullyAuthenticated()          Возвращает true, если пользователь полностью
                                    аутентифицирован (кроме пользователей, аутентифицированных
                                    с помощью функции «запомнить меня»)

    isRememberMe()                  Возвращает true, если пользователь аутентифицирован с по
                                    мощью функции «запомнить меня»

    permitAll                       Всегда возвращает true

    principal                       Основной объект, представляющий пользователя
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/design", "/orders").authenticated()
                        .requestMatchers("/", "/**").access(new WebExpressionAuthorizationManager("permitAll()")))
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true))//второй параметр - всегда переходим
                                                            //на этот url, если false, то переходим только тогда
                                                            //когда пользователь изначально открыл страницу логина
                                                            //если false и пользователь открыл другую страницу, то после
                                                            // логина будет перенаправлен на нее, лучше всегда ставить true
                .oauth2Login(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/login")
                        .permitAll())
                .build();
    }
}
