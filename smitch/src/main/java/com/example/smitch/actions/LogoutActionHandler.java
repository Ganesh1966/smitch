package com.example.smitch.actions;

import com.example.smitch.actions.builder.Action;
import com.example.smitch.actions.builder.ActionHandler;
import com.example.smitch.exceptions.DataValidationException;
import com.example.smitch.services.JWTService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class LogoutActionHandler extends ActionHandler<Map> {

@Autowired
private JWTService jwtService;

@Override
public Action handlingFor() {
        return Action.LOGOUT;
        }

@Override
public Map executeAction(Map operateOn) {
        if (operateOn.isEmpty())
        throw new DataValidationException("Login credentials missing for login  ");

        Map credentials = mapper.convertValue(operateOn.get("credentials"), Map.class);
        String userName = String.valueOf(credentials.get("username"));
        jwtService.deregisterLogin(userName);

        return Map.of("logout", "logout successfully");
        }
        }
