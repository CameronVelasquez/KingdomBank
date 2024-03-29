package com.mindhub.Homebanking.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers("/web/index.html").permitAll()
                .antMatchers("/rest/**", "/h2-console/", "/rest/h2-console").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/clients", "/api/login", "/api/cards/transactions").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts", "/api/clients/current/cards", "/api/transactions", "/api/loans", "/api/cards/transactions").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH, "/api/clients/current/cards", "/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current", "/api/clients/current/accounts", "/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers("/web/accounts.html","/web/account.html", "/web/cards.html", "/web/createCards.html", "/web/transfers.html", "/web/loanApplication.html").hasAuthority("CLIENT")
                .antMatchers("/web/assets/**", "/web/js/**").permitAll()
                .antMatchers("/web/manager/**").hasAuthority("ADMIN")
                .antMatchers("/api/logout", "/api/loans").hasAnyAuthority("CLIENT","ADMIN")

                .antMatchers(HttpMethod.DELETE, "/rest/clients", "/api/clients").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/admin/loans").hasAuthority("ADMIN")
                .antMatchers("/api/accounts", "/api/clients").hasAuthority("ADMIN")
                .anyRequest().denyAll();





        http.formLogin()

                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        // turn off checking for CSRF tokens

        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed

        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)); //401

        // if login is successful, just clear the flags asking for authentication

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();

    }
    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

    }



}