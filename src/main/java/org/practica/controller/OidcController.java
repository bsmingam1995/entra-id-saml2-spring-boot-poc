package org.practica.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oidc")
public class OidcController {

    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile(OAuth2AuthenticationToken authentication) {
        return ResponseEntity.ok("Usuario autenticado vía OIDC: " + authentication.getPrincipal().getAttributes());
    }
}

