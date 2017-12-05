package com.ramonbrand.stockexchange.stockexchange.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ramonbrand.stockexchange.stockexchange.model.Matcher;
import com.ramonbrand.stockexchange.stockexchange.model.data.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @CrossOrigin
    @RequestMapping(value = "/api/individuals/login", method = RequestMethod.POST)
    public String apiIndividualsLogin(
            HttpServletRequest request,
            @RequestBody SignInCombo signInCombo
    ) {
        // Check database to see if username and password are correct
        if(false) {
            return "invalid";
        }

        int userId = 45;


        try {
            String token = JWT.create().withIssuer("auth0").withClaim("id", userId).sign(Algorithm.HMAC256("projectbox"));
            return token;
        }  catch (UnsupportedEncodingException exception){
            //UTF-8 encoding not supported
            return "invalid";
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            return "invalid";
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/api/individuals")
    public List<Individual> apiIndividualsLogin(
            HttpServletRequest request
    ) {
        List<Individual> individuals = new ArrayList<>();
        individualRepository.findAll().forEach(individuals::add);

        return individuals;
    }

    @CrossOrigin
    @RequestMapping(value = "/api/individuals/{id}")
    public Individual apiIndividualsLogin(
            HttpServletRequest request,
            @PathVariable("id") long id
    ) {
        return individualRepository.findOne(id);
    }

}
