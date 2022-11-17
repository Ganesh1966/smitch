package com.example.smitch.db;

import com.example.smitch.models.AuthentificationToken;
import com.example.smitch.models.Power;
import com.example.smitch.models.PowerListView;
import com.example.smitch.models.User;
import com.example.smitch.services.AuthentificationService;

import java.util.List;
import java.util.Optional;

public interface PersistanceStore {


    String adduser(User user);

    void saveAuthorizationToken(String jws, String secretString);

    void updateAuthorizationToken(String jwtToken);

    Optional<AuthentificationToken> findAuthorizationToken(String jwtToken);

    Optional<User> findUserWithCredentials(String userName, String password);

    String addpower(Power power);

    List<PowerListView> fetchAllPower(Power power);

    List<PowerListView> fetchAllPowerDay(Power power);

    void deleteAuthorizationToken(String jwtToken);

    void validateUser(User user);
}
