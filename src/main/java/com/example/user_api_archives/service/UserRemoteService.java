package com.example.user_api_archives.service;

import com.example.user_api_archives.failure.UnauthorizedException;
import com.example.user_api_archives.model.TokenResponse;
import com.example.user_api_archives.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRemoteService {
    @Value("${postgres.t_url}")
    String api;
   // final ObjectMapper mapper;

    @Value("${postgres.t_client_id}")
    String clientId;

    @Value("${postgres.t_user_name}")
    String userName;

    @Value("${postgres.t_password}")
    String password;

    @Value("${postgres.t_grant_type}")
    String grantType;

    String token;

    String testMessage;

    final MessageSource ms;

    private final HttpServletRequest request;
    final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    public UserRemoteService(
            HttpServletRequest request,
            HttpServletResponse response,
            MessageSource ms,
            @Qualifier("testString")
            String testMessage) {
//        this.mapper = mapper;
        this.ms = ms;
        this.request = request;
        this.testMessage = testMessage;
    }

    public void auth() throws IOException {
      var reqJson = prepareJsonForAuthRequest();
      var respJson = authHttpResponse(api, reqJson);
      var tokenResponse = parseJsonToTokenResponse(respJson);
      token = "Bearer "+ tokenResponse.getRefresh_token();
      log.info(token);
      log.info("TEST MESS:" + testMessage);
    }

    public List<User> doGet() throws IOException {
        String response = getHttpResponse(api, token);
        var users = parseJsonToUsersList(response);
        log.info("RES:" + response);
        log.info("REQ:" + request);
        return users;
    }

    private String authHttpResponse(String url, String reqJson) {
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .uri(URI.create(url + "/rpc/token"))
                .headers(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8]")
                .POST(HttpRequest.BodyPublishers.ofString(reqJson, StandardCharsets.UTF_8)).build();

        HttpClient httpClient = HttpClient
                .newBuilder()
                .build();
        HttpResponse<String>  resp;
        try {
            resp = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(ms.getMessage("error.http.client", null, locale));
        }
        return resp.body();
    }

    private String getHttpResponse(String url, String token) throws IOException {
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .uri(URI.create(url + "/user_"))
                .headers(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8]")
                .headers(HttpHeaders.AUTHORIZATION, token)
                .GET().build();

        HttpClient httpClient = HttpClient
                .newBuilder()
                .build();

        HttpResponse<String>  resp;
        try {
            resp = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if(resp.statusCode() == 401) {
             throw new UnauthorizedException();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(ms.getMessage("error.http.client", null, locale));
        }
        return resp.body();
    }

    private List<User> parseJsonToUsersList(String jsonBody) {
        Gson gson = new Gson();
        Type userListType = new TypeToken<List<User>>() {}.getType();
        return gson.fromJson(jsonBody, userListType);
    }

    private TokenResponse parseJsonToTokenResponse(String jsonBody) {
        Gson gson = new Gson();
        return gson.fromJson(jsonBody, TokenResponse.class);
    }

    private String prepareJsonForAuthRequest() {
        Gson gson = new GsonBuilder().create();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("client_id", clientId);
        jsonMap.put("user_name", userName);
        jsonMap.put("password", password);
        jsonMap.put("grant_type", grantType);
        //jsonMap.put("request", gson.fromJson(body, JsonObject.class));
        return gson.toJson(jsonMap);
    }
}
