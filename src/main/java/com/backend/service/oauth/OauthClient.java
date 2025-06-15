package com.backend.service.oauth;

import com.backend.dto.request.GoogleOauthLoginRequest;
import com.backend.dto.response.GoogleOauthTokenResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class OauthClient {

    private final RestClient restClient;
    private final OauthRequestProperties oauthRequestProperties;

    public GoogleOauthTokenResponse requestToken(GoogleOauthLoginRequest request) {
        String tokenRequestUri = oauthRequestProperties.getTokenRequestUri();
        Map<String, String> requestBody = oauthRequestProperties.createTokenRequestBody(request);

        return restClient.post()
                .uri(tokenRequestUri)
                .body(requestBody)
                .retrieve()
                .body(GoogleOauthTokenResponse.class);
    }
}
