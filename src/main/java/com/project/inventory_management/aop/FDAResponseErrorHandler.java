package com.project.inventory_management.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.inventory_management.dto.FDAErrorResponseDTO;
import com.project.inventory_management.exception.FDAApiException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.io.InputStream;

public class FDAResponseErrorHandler extends DefaultResponseErrorHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        InputStream body = response.getBody();
        FDAErrorResponseDTO errorResponse = objectMapper.readValue(body, FDAErrorResponseDTO.class);
        throw new FDAApiException(errorResponse.getError().getMessage());
    }
}
