package com.developerhelperhub.klight.apigateway.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenAuthenticationConverter;


import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class KecloakJwtConverter {

    @Value("${keycloak.client-id}")
    private String clientId;

    @Bean
    public OpaqueTokenAuthenticationConverter apaqueTokenAuthenticationConverter() {

        return new OpaqueTokenAuthenticationConverter() {
            @Override
            public Authentication convert(String introspectedToken, OAuth2AuthenticatedPrincipal authenticatedPrincipal) {

                Collection<GrantedAuthority> authorities = findGrantedAuthority(authenticatedPrincipal.getAttributes());

                Instant issuedAt = findTime(authenticatedPrincipal.getAttributes(), "iat");
                Instant expiresAt = findTime(authenticatedPrincipal.getAttributes(), "exp");

                log.debug("issuedAt : {}", issuedAt);
                log.debug("expiresAt : {}", expiresAt);

                OAuth2AccessToken accessToken = new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        introspectedToken,
                        issuedAt, // You can set the issuedAt date here
                        expiresAt  // You can set the expiresAt date here
                );

                // Return a BearerTokenAuthentication with the custom authorities
                return new BearerTokenAuthentication(authenticatedPrincipal, accessToken, authorities);
            }
        };
    }

    private Instant findTime(Map<String, Object> attributes, String key) {
        return (Instant) attributes.get(key);
    }

    private Collection<GrantedAuthority> findGrantedAuthority(Map<String, Object> attributes) {

        List<String> roles = new ArrayList<>();

        addRolesFromResourceLevel(attributes, roles);
        addRolesFromReleamLevel(attributes, roles);

        log.debug("roles : {}", roles);

        return
                roles.stream()
                        .map(role -> (GrantedAuthority) role::toUpperCase)
                        .collect(Collectors.toList());
    }


    private void addRolesFromResourceLevel(Map<String, Object> attributes, List<String> roles) {

        Map<String, Map<String, List<String>>> resourceAccess = (Map<String, Map<String, List<String>>>) attributes.get("resource_access");

        log.debug("resource_access : {}", resourceAccess);

        if (resourceAccess != null) {
            roles.addAll(resourceAccess.getOrDefault(this.clientId, Collections.emptyMap()).getOrDefault("roles", Collections.emptyList()));
        }

    }

    private void addRolesFromReleamLevel(Map<String, Object> attributes, List<String> roles) {

        Map<String, List<String>> releamAccess = (Map<String, List<String>>) attributes.get("realm_access");

        log.debug("releam_access : {}", releamAccess);

        if (releamAccess != null) {
            roles.addAll(releamAccess.getOrDefault("roles", Collections.emptyList()));
        }
    }

}
