package com.example.smitch.services;

import com.example.smitch.db.PersistanceStore;
import com.example.smitch.models.AuthentificationToken;
import com.example.smitch.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class JWTService {

    private final Logger logger = Logger.getLogger("JWTService");

    @Autowired
    private PersistanceStore persistenceStore;

    @Autowired
    protected ObjectMapper mapper;


    public String registerLogin(User user) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        Map claims = mapper.convertValue(user, Map.class);
        String jws = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getName())
                .signWith(key).compact();
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        persistenceStore.saveAuthorizationToken(jws, secretString);

        return jws;
    }

    public boolean validateToken(String jwtToken){
        Optional<AuthentificationToken> tokenOptional =  persistenceStore.findAuthorizationToken(jwtToken);

        if(tokenOptional.isEmpty())
            return false;

        AuthentificationToken token = tokenOptional.get();
        if(Duration.between(token.getUpdatedAt(), LocalDateTime.now(Clock.systemUTC())).toMinutes()>15)
            return false;

        if(Duration.between(token.getCreatedAt(), LocalDateTime.now(Clock.systemUTC())).toDays()>=1)
            return false;

        Key secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(token.getKey()));

        Jwt parsedToken = Jwts.parserBuilder().setSigningKey(secretKey).build().parse(token.getJwtToken());
        logger.info(String.format("PARSED TOKEN : %s", parsedToken));

        return true;
    }

    public void updateToken(String jwtToken) {
        persistenceStore.updateAuthorizationToken(jwtToken);
    }

    public void deregisterLogin(String jwtToken) {
        persistenceStore.deleteAuthorizationToken(jwtToken);
    }
}