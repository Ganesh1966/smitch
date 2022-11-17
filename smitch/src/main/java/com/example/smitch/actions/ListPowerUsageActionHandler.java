package com.example.smitch.actions;

import com.example.smitch.actions.builder.Action;
import com.example.smitch.actions.builder.ActionHandler;
import com.example.smitch.models.Power;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ListPowerUsageActionHandler extends ActionHandler<Map> {

    @Override
    public Action handlingFor() {
        return Action.POWER_LIST;
    }

    @Override
    public Map executeAction(Map operateOn) {

        Power power=mapper.convertValue(operateOn.get("power"),Power.class);

        return Map.of("powerList", persistanceStore.fetchAllPower(power));
    }


}
