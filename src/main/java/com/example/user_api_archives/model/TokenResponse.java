package com.example.user_api_archives.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
    private String token_type;
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private int refresh_expires_in;

}