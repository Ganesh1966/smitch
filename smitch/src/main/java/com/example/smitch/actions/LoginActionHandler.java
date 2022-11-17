package com.example.smitch.actions;

import com.example.smitch.actions.builder.Action;
import com.example.smitch.actions.builder.ActionHandler;
import com.example.smitch.exceptions.DataValidationException;
import com.example.smitch.exceptions.UnauthorizedException;
import com.example.smitch.models.LoggedInDetails;
import com.example.smitch.models.User;
import com.example.smitch.services.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class LoginActionHandler extends ActionHandler<Map<String, LoggedInDetails>> {

    @Autowired
    private JWTService jwtService;

    @Override
    public Action handlingFor() {
        return Action.LOGIN;
    }

    @Override
    public Map<String, LoggedInDetails> executeAction(Map operateOn) {
        if (operateOn.isEmpty())
            throw new DataValidationException("Login credentials missing for login  ");

        Map credentials = mapper.convertValue(operateOn.get("credentials"), Map.class);
        String userName = String.valueOf(credentials.get("username"));
        String password = String.valueOf(credentials.get("password"));

        Optional<User> userOptional = persistanceStore.findUserWithCredentials(userName, password);
        if (userOptional.isEmpty()) {
            throw new UnauthorizedException("Credentials unmatched");
        }

        User user = userOptional.get();
        String token = jwtService.registerLogin(user);

        return Map.of("loggedInDetails", new LoggedInDetails(user, token));
    }
}