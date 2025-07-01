package org.practica.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(1)
public class SamlSecurityConfig {


    @Bean
    public RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() {
        RelyingPartyRegistration registration = RelyingPartyRegistrations
                .fromMetadataLocation("classpath:saml/idp-metadata.xml")
                .registrationId("entra-id")
                .entityId("https://samltoolkit.azurewebsites.net")
                .assertionConsumerServiceLocation("https://entra-id-saml2-spring-boot-poc.onrender.com/login/saml2/sso/ssocircle")
                .build();

        return new InMemoryRelyingPartyRegistrationRepository(registration);
    }

    @Bean
    public SecurityFilterChain samlSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/saml/**", "/saml2/**", "/login/**")
//                .authorizeHttpRequests(authz -> authz
//                        .anyRequest().authenticated()
//                )
//                .saml2Login(Customizer.withDefaults());

        http
                .securityMatcher("/saml/**", "/saml2/**", "/login/**", "/logout")
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().authenticated()
                )
                .saml2Login(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/saml/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("Sesión cerrada correctamente.");
                        })
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}

