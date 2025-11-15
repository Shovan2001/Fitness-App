package com.fitness.APIGateway.filters;

import com.fitness.APIGateway.dto.UserRequestDTO;
import com.fitness.APIGateway.services.InterService.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class KeyCloakUSerSyncFilter implements WebFilter {
//
//    private final UserService userService;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//
//        // If we want to pass teh userId instead of keyCloakID //optional step
//        String userId = exchange.getRequest().getHeaders().getFirst("X-userId");
//
//        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
//
//
//        UserRequestDTO userRequestDTO = getUserDetails(token);
//
//        // making keyCLoak Id And UserId Same
//        String keyCloakId;
//        if (userId == null) {
//            keyCloakId = userRequestDTO.getKeyCloakId();
//            userId=keyCloakId;
//        }
//
//        if (userId != null && token != null) {
//
//            String finalUserId = userId;
//
//            return userService.validateUserByUserId(userId).flatMap(exist -> {
//                if (!exist) {
//                    if (userRequestDTO != null) {
//                        return userService.registerUser(userRequestDTO).then(Mono.empty());
//                    } else {
//                        return Mono.empty();
//                    }
//                } else {
//                    log.info("user already exist !!");
//                    return Mono.empty();
//                }
//            }).then(Mono.defer(() -> {
//                ServerHttpRequest mutatedRequest = exchange
//                        .getRequest()
//                        .mutate()
//                        .header("X-userId", finalUserId)
//                        .build();
//
//                return chain.filter(exchange.mutate().request(mutatedRequest).build());
//            }));
//        }
//        return chain.filter(exchange);
//    }
//
//    private UserRequestDTO getUserDetails(String token) {
//
//        UserRequestDTO userRequestDTO = new UserRequestDTO();
//
//        try {
//
//            String tokenWithoutBearer = token.replace("Bearer ", "").trim();
//            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
//
//            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
//
//            userRequestDTO.setKeyCloakId(jwtClaimsSet.getClaimAsString("sub"));
//            userRequestDTO.setEmail(jwtClaimsSet.getStringClaim("email"));
//            userRequestDTO.setPassword("testuser");
//            userRequestDTO.setFirstName(jwtClaimsSet.getStringClaim("given_name"));
//            userRequestDTO.setLastName(jwtClaimsSet.getStringClaim("family_name"));
//
//        } catch (Exception e) {
//            log.error("Error while getting user details from token");
//        }
//
//        return userRequestDTO;
//    }
//}

@Slf4j
@Component
@RequiredArgsConstructor
public class KeyCloakUserSyncFilter implements WebFilter {

    private final UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (token == null) {
            return chain.filter(exchange);  // No token → skip filter
        }

        UserRequestDTO userRequestDTO = extractUser(token);

        String headerUserId = exchange.getRequest().getHeaders().getFirst("X-userId");
        String finalUserId = (headerUserId != null) ?
                headerUserId :
                userRequestDTO.getKeyCloakId();

        return userService.validateUserByUserId(finalUserId)
                .flatMap(exists -> {
                    if (!exists) {
                        return userService.registerUser(userRequestDTO);
                    }
                    log.info("User already exists: {}", finalUserId);
                    return Mono.empty();
                })
                .then(Mono.defer(() -> {
                    ServerHttpRequest updatedRequest = exchange.getRequest()
                            .mutate()
                            .header("X-userId", finalUserId)
                            .build();

                    return chain.filter(exchange.mutate().request(updatedRequest).build());
                }));
    }

    private UserRequestDTO extractUser(String token) {
        UserRequestDTO dto = new UserRequestDTO();
        try {
            String jwt = token.replace("Bearer ", "").trim();
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            dto.setKeyCloakId(claims.getStringClaim("sub"));
            dto.setEmail(claims.getStringClaim("email"));
            dto.setPassword("testuser");
            dto.setFirstName(claims.getStringClaim("given_name"));
            dto.setLastName(claims.getStringClaim("family_name"));

        } catch (Exception e) {
            log.error("Error parsing JWT", e);
        }

        return dto;
    }
}
