package org.practica.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/saml")
public class SamlController {

    @GetMapping(value = "/profile", produces = "application/json")
    public ResponseEntity<Object> getUserProfile(Authentication authentication) {
        Saml2AuthenticatedPrincipal principal = (Saml2AuthenticatedPrincipal) authentication.getPrincipal();

        var data = new java.util.HashMap<String, Object>();
        data.put("name", authentication.getName());

        var detail = new java.util.HashMap<String, Object>();
        principal.getAttributes().forEach((key, value) -> {
            detail.put(key, value.stream().map(Object::toString).toList());
        });
        data.put("attributes", detail);
        return ResponseEntity.ok(data);
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws Exception {
        request.logout();
        return ResponseEntity.ok("Sesión cerrada correctamente.");
    }
}
