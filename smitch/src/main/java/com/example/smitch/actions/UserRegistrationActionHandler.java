package com.example.smitch.actions;

import com.example.smitch.actions.builder.Action;
import com.example.smitch.actions.builder.ActionHandler;
import com.example.smitch.exceptions.DataValidationException;
import com.example.smitch.models.User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserRegistrationActionHandler extends ActionHandler<String> {

    @Override
    public Action handlingFor(){
        return Action.ADD_USER;
    }

    @Override
    public String executeAction(Map operateOn){
        if(operateOn.isEmpty())
            throw new DataValidationException("User cannot be empty");



        User user=mapper.convertValue(operateOn.get("user"),User.class);

        persistanceStore.validateUser(user);


        return persistanceStore.adduser(user);

    }
}
