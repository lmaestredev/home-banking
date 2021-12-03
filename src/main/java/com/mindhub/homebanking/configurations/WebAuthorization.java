package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/web/index.html").permitAll()
                .antMatchers("/web/register.html").permitAll()
                .antMatchers("/web/styles/customs.css").permitAll()
                .antMatchers("/web/js/index.js").permitAll()
                .antMatchers("/web/js/register.js").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients").permitAll()

                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/h2-console/**").hasAuthority("ADMIN")
                .antMatchers("/manager.html").hasAuthority("ADMIN")
                .antMatchers("/manager.js").hasAuthority("ADMIN")
                .antMatchers("/api/clients").hasAuthority("ADMIN")
                .antMatchers("/api/accounts").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/admin/newLoan").hasAuthority("ADMIN")


                .antMatchers("/web/**").hasAnyAuthority("CLIENT, ADMIN")
                .antMatchers(HttpMethod.GET,"/api/clients/current").hasAnyAuthority("CLIENT, ADMIN")
                .antMatchers(HttpMethod.GET,"/api/accounts/{id}").hasAnyAuthority("CLIENT, ADMIN")
                .antMatchers(HttpMethod.POST,"/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/transaction").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/api/loan").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/loan").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.DELETE,"/api/deleteContact").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.DELETE,"/api/deleteCard/{cardNumber}").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.DELETE,"/api/deleteAccount/{accountNumber}").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/transactions/pdf").hasAuthority("CLIENT")

        ;

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout()
                .logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

    }

}
