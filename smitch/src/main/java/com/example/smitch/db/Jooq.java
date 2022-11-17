package com.example.smitch.db;

import com.example.demo.db.jooqs.Tables;
import com.example.demo.db.jooqs.tables.AuthorizationToken;
import com.example.demo.db.jooqs.tables.records.AuthorizationTokenRecord;
import com.example.demo.db.jooqs.tables.records.PowerRecord;
import com.example.demo.db.jooqs.tables.records.UserRecord;
import com.example.smitch.models.AuthentificationToken;
import com.example.smitch.models.Power;
import com.example.smitch.models.PowerListView;
import com.example.smitch.models.User;
import com.google.common.hash.Hashing;
import io.github.cdimascio.dotenv.Dotenv;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class Jooq implements PersistanceStore{

    private DSLContext jooqContext;

    @PostConstruct
    private void createJooqContext() {
        try {

            Dotenv dotenv = Dotenv
                    .configure()
                    .ignoreIfMissing()
                    .load();

            this.jooqContext = DSL.using(
                    dotenv.get("POSTGRES_URL"),
                    dotenv.get("POSTGRES_USERNAME"),
                    dotenv.get("POSTGRES_PASSWORD")
            );
        } catch (Exception e) {

            throw e;
        }
    }

    private DSLContext liveContext(){
        this.jooqContext.connection((connection -> {
            if (connection.isClosed()){
                createJooqContext();
            }
        }));

        return this.jooqContext;
    }


    @Override
    public String adduser(User user) {

        UserRecord userRecord=liveContext().newRecord(Tables.USER);

        userRecord.setDisplayName(user.getName());
        userRecord.setName(user.getUserName());
        userRecord.setMailid(user.getEmailId());
        userRecord.setTimestamp(OffsetDateTime.now());
        userRecord.setPassword(String.format("p:%s", generateHashedPassword(user.getPassword())));
        userRecord.store();

        return "user record stored successfully";
    }

    private String generateHashedPassword(String originalPassword) {
        return Hashing.sha256()
                .hashString(originalPassword + "wsedrftgyhjvhgc", StandardCharsets.UTF_8)
                .toString();
    }

    @Override
    public void saveAuthorizationToken(String jws, String secretString) {

        AuthorizationTokenRecord record = liveContext().newRecord(Tables.AUTHORIZATION_TOKEN);
        record.setJwtToken(jws);
        record.setKey(secretString);
        record.setUpdatedAt(OffsetDateTime.now(Clock.systemUTC()));
        record.setCreatedAt(OffsetDateTime.now(Clock.systemUTC()));
        record.store();
    }

    @Override
    public void updateAuthorizationToken(String jwtToken) {

        liveContext().update(Tables.AUTHORIZATION_TOKEN)
                .set(Tables.AUTHORIZATION_TOKEN.UPDATED_AT, OffsetDateTime.now())
                .where(Tables.AUTHORIZATION_TOKEN.JWT_TOKEN.eq(jwtToken))
                .execute();
    }

    @Override
    public void deleteAuthorizationToken(String jwtToken) {

        liveContext().deleteFrom(Tables.AUTHORIZATION_TOKEN)
                .where(Tables.AUTHORIZATION_TOKEN.JWT_TOKEN.eq(jwtToken))
                .execute();
    }

    @Override
    public Optional<AuthentificationToken> findAuthorizationToken(String jwtToken) {
        return liveContext().selectFrom(Tables.AUTHORIZATION_TOKEN)
                .where(Tables.AUTHORIZATION_TOKEN.JWT_TOKEN.eq(jwtToken))
                .stream()
                .map(record -> {
                    AuthentificationToken token = new AuthentificationToken();
                    token.setJwtToken(record.getJwtToken());
                    token.setKey(record.getKey());
                    token.setCreatedAt(record.getCreatedAt().toLocalDateTime());
                    token.setUpdatedAt(record.getUpdatedAt().toLocalDateTime());
                    return token;
                })
                .findFirst();
    }

    @Override
    public Optional<User> findUserWithCredentials(String userName, String password) {
        String hashedPassword = generateHashedPassword(password);
        return liveContext().selectFrom(Tables.USER)
                .where(Tables.USER.NAME.eq(userName))
                .and(Tables.USER.PASSWORD.eq(String.format("p:%s", hashedPassword)))
                .stream()
                .map(userRecord -> {
                    User user = new User();
                    user.setId(userRecord.getId());
                    user.setEmailId(userRecord.getMailid());
                    user.setName(userRecord.getName());
                    user.setMobileNumber(userRecord.getPhonenumber());

                    return user;
                })
                .findFirst();
    }

    @Override
    public String addpower(Power power) {

        PowerRecord powerRecord=liveContext().newRecord(Tables.POWER);

        powerRecord.setApplication(power.getApplicaionType());
        powerRecord.setFromTime(power.getFromTime());
        powerRecord.setToTime(power.getToTime());
        powerRecord.setUnitConsumed(power.getUnitConsumed());
        long x= power.getToTime().toEpochSecond()-power.getFromTime().toEpochSecond();
        powerRecord.setDuration(String.valueOf(OffsetDateTime.ofInstant(Instant.ofEpochMilli(x), ZoneId.systemDefault())));
        powerRecord.setUserid(power.getUserId());
        powerRecord.store();

        return "Power addedd successfully";
    }

    @Override
    public List<PowerListView> fetchAllPower(Power power) {
        return liveContext().select()
                .from(Tables.POWER)
                .leftOuterJoin(Tables.USER)
                .on(Tables.POWER.USERID.eq(Tables.USER.ID))
                .where(Tables.POWER.USERID.eq(power.getUserId()))
                .fetch()
                .stream()
                .map(record -> {
                    PowerRecord powerRecord = record.into(Tables.POWER);
                    UserRecord userRecord = record.into(Tables.USER);

                    PowerListView powerListView=new PowerListView();
                    powerListView.setId(powerRecord.getId());
                    powerListView.setApplicaionType(powerRecord.getApplication());
                    powerListView.setName(userRecord.getName());
                    powerListView.setUnitConsumed(powerRecord.getUnitConsumed());
                    powerListView.setFromTime(powerRecord.getFromTime());
                    powerListView.setToTime(powerRecord.getToTime());
                    return powerListView;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PowerListView> fetchAllPowerDay(Power power) {
        return liveContext().select()
                .from(Tables.POWER)
                .leftOuterJoin(Tables.USER)
                .on(Tables.POWER.USERID.eq(Tables.USER.ID))
                .where(Tables.POWER.USERID.eq(power.getUserId()))
                .and(Tables.POWER.TO_TIME.greaterOrEqual(power.getToTime()))
                .and(Tables.POWER.FROM_TIME.lessOrEqual(power.getToTime()))
                .fetch()
                .stream()
                .map(record -> {
                    PowerRecord powerRecord = record.into(Tables.POWER);
                    UserRecord userRecord = record.into(Tables.USER);

                    PowerListView powerListView=new PowerListView();
                    powerListView.setId(powerRecord.getId());
                    powerListView.setApplicaionType(powerRecord.getApplication());
                    powerListView.setName(userRecord.getName());
                    powerListView.setUnitConsumed(powerRecord.getUnitConsumed());
                    powerListView.setFromTime(powerRecord.getFromTime());
                    powerListView.setToTime(powerRecord.getToTime());
                    return powerListView;
                })
                .collect(Collectors.toList());
    }


}
