package com.backend.service.oauth;

import com.backend.common.exception.CustomException;
import com.backend.common.exception.ErrorCode;
import com.backend.dto.request.GoogleOauthLoginRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class OauthRequestProperties {

    private final String clientId;
    private final String clientSecret;
    private final String tokenRequestUri;
    private final String userInfoUri;
    private final String grantType;
    private final List<String> redirectUris;

    public OauthRequestProperties(
            @Value("${oauth.google.client_id}") String clientId,
            @Value("${oauth.google.client_secret}") String clientSecret,
            @Value("${oauth.google.token_request_uri}") String tokenRequestUri,
            @Value("${oauth.google.user_info_uri}") String userInfoUri,
            @Value("${oauth.google.grant_type}") String grantType,
            @Value("${oauth.google.redirect_uris}") String redirectUris) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenRequestUri = tokenRequestUri;
        this.userInfoUri = userInfoUri;
        this.grantType = grantType;
        this.redirectUris = convertStringToList(redirectUris);
    }

    private List<String> convertStringToList(String uris) {
        return Arrays.stream(uris.split(","))
                .map(String::trim)
                .toList();
    }

    public Map<String, String> createTokenRequestBody(GoogleOauthLoginRequest request) {
        String redirectUri = getMatchingRedirectUri(request.redirectUri());
        String code = request.code();

        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("client_id", clientId);
        map.put("client_secret", clientSecret);
        map.put("redirect_uri", redirectUri);
        map.put("grant_type", grantType);

        return map;
    }

    private String getMatchingRedirectUri(String redirectUri) {
        return redirectUris.stream()
                .filter(uri -> uri.equals(redirectUri))
                .findAny()
                .orElseThrow(() -> new CustomException(ErrorCode.OAUTH_REDIREC_URI_MISMATCH));
    }
}
