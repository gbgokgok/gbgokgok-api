package com.backend.common.exception;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class OauthClientExceptionHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        try {
            return response.getStatusCode().is4xxClientError();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.OAUTH_TOKEN_INTERNAL_EXCEPTION);
        }
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        throw new OauthException(getResponseBody(response));
    }

    private OauthExceptionResponse getResponseBody(ClientHttpResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(response.getBody().readAllBytes(), OauthExceptionResponse.class);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.OAUTH_TOKEN_INTERNAL_EXCEPTION);
        }
    }
}
