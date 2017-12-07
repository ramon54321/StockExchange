package com.ramonbrand.stockexchange.stockexchange.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ramonbrand.stockexchange.stockexchange.model.Matcher;
import com.ramonbrand.stockexchange.stockexchange.model.PasswordVerification;
import com.ramonbrand.stockexchange.stockexchange.model.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ApiIndividuals {

    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private ExchangeRepository exchangeRepository;
    @Autowired
    private TradeRequestRepository tradeRequestRepository;
    @Autowired
    private IndividualRepository individualRepository;

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/api/individuals/login", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = MediaType.APPLICATION_JSON_VALUE)
    public LoggedInPojo apiIndividualsLogin(
            HttpServletRequest request,
            @RequestBody SignInCombo signInCombo
    ) {
        // Check database to see if username and password are corre
        Individual individual = individualRepository.findByUsername(signInCombo.username);
        if(individual == null)
            return null;

        if(!PasswordVerification.getPasswordHash(signInCombo.password).equals(individual.passwordHash))
            return null;

        int userId = (int) individual.id;

        try {
            String token = JWT.create().withIssuer("auth0").withClaim("id", userId).sign(Algorithm.HMAC256("projectbox"));
            LoggedInPojo loggedInPojo = new LoggedInPojo();
            loggedInPojo.token = token;
            loggedInPojo.userId = userId;
            return loggedInPojo;
        }  catch (UnsupportedEncodingException exception){
            //UTF-8 encoding not supported
            return null;
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            return null;
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/api/individuals", produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<Individual> apiIndividualsLoginGetAll(
            HttpServletRequest request
    ) {
        List<Individual> individuals = new ArrayList<>();
        individualRepository.findAll().forEach(individuals::add);

        return individuals;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/api/individuals/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public Individual apiIndividualsLoginGetOne(
            HttpServletRequest request,
            @PathVariable("id") long id
    ) {
        return individualRepository.findOne(id);
    }

}
