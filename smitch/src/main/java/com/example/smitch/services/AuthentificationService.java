package com.example.smitch.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.http.HttpRequest;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class AuthentificationService {
    private final Logger logger = Logger.getLogger("AuthorizationService");

    @Autowired
    private JWTService jwtService;

    public void authorize(Optional<String> authorization){
        if(authorization.isPresent()){
            String token = authorization.get();
            boolean tokenStatus = jwtService.validateToken(token);
            logger.info(String.format("FOR TOKEN: %s | TOKEN STATUS: %s", token, tokenStatus));

            if(tokenStatus){
                jwtService.updateToken(token);
            }
        }
    }


}