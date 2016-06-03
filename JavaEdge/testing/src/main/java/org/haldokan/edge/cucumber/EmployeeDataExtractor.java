package org.haldokan.edge.cucumber;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Extract all employee data from ConnectION and ftp it to Everbridge
 * Created by haytham.aldokanji on 6/3/16.
 */
public class EmployeeDataExtractor {
    //TODO ADD support for running only one leg of the job: download the extract or upload it - may be useful for failures
    //TODO REPLACE all sout with the logging

    private static final String USERNAME_KEY = "uname";
    private static final String PASSWORD_KEY = "pwd";
    private static final String URL_KEY = "url";
    private static final String AUTH_ENDPOINT_KEY = "authEPoint";
    private static final String DATA_ENDPOINT_KEY = "dataEPoint";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String CONN_AUTH_HEADER_KEY = "x-connection-authorization";
    private static final String DEFAULT_AUTH_ENDPOINT = "/api/auth/token";
    private static final String DEFAULT_DATA_ENDPOINT = "/api/platform/employees";
    private static final String DEFAULT_TOKEN_PREFIX = "Bearer";

    private final RestTemplate restTemplate;
    private final Map<String, String> runConfigsTable;
    private final ObjectMapper jsonMapper;

    private EmployeeDataExtractor(String[] runConfigs) {
        restTemplate = new RestTemplate();
        runConfigsTable = parseRunConfigs(runConfigs);
        jsonMapper = new ObjectMapper();
        System.out.println("Run Configs: " + runConfigsTable);
    }

    public static void main(String[] runConfigs) {
        EmployeeDataExtractor extractor = new EmployeeDataExtractor(runConfigs);
        extractor.run();
    }

    public void run() {
        String extract = downloadExtract();
        uploadExtract(extract);
    }

    private String downloadExtract() {
        String authenticationToken = getAuthenticationToken();

        HttpHeaders headers = new HttpHeaders();
        headers.put(CONN_AUTH_HEADER_KEY, Arrays.asList(TOKEN_PREFIX + " " + authenticationToken));
        HttpEntity<String> request = new HttpEntity<>(headers);

        String dataUrl = runConfigsTable.get(URL_KEY) + runConfigsTable.get(DATA_ENDPOINT_KEY);
        ResponseEntity<String> response = restTemplate.exchange(dataUrl, HttpMethod.GET, request, String.class);

        return response.getBody();
    }

    public void uploadExtract(String extract) {
        System.out.printf("%s%n", extract);
    }

    Map<String, String> parseRunConfigs(String[] runConfigs) {
        Map<String, String> paramMap = new HashMap<>();

        for (String param : runConfigs) {
            String[] paramParts = param.split("=");
            String paramKey = paramParts[0].trim();
            String paramValue = paramParts.length == 2 ? paramParts[1].trim() : null;

            if (paramKey.isEmpty() || paramValue == null || paramValue.isEmpty()) {
                throw new IllegalArgumentException("Parameters are mal-formatted: " + param);
            }
            paramMap.put(paramKey, paramValue);
        }

        if (!paramMap.containsKey(URL_KEY) || !paramMap.containsKey(USERNAME_KEY) || !paramMap.containsKey(PASSWORD_KEY)) {
            throw new IllegalArgumentException("At minimum the 'url', 'username', and 'password' should be provided");
        }
        if (!paramMap.containsKey(AUTH_ENDPOINT_KEY)) {
            paramMap.put(AUTH_ENDPOINT_KEY, DEFAULT_AUTH_ENDPOINT);
        }
        if (!paramMap.containsKey(DATA_ENDPOINT_KEY)) {
            paramMap.put(DATA_ENDPOINT_KEY, DEFAULT_DATA_ENDPOINT);
        }
        if (!paramMap.containsKey(TOKEN_PREFIX)) {
            paramMap.put(TOKEN_PREFIX, DEFAULT_TOKEN_PREFIX);
        }

        return paramMap;
    }

    public String getAuthenticationToken() {
        String authenticationUrl = runConfigsTable.get(URL_KEY) + runConfigsTable.get(AUTH_ENDPOINT_KEY);
        System.out.println("authenticating using endpoint: " + authenticationUrl);

        String credentials = new Credentials(runConfigsTable.get(USERNAME_KEY),
                runConfigsTable.get(PASSWORD_KEY)).toJson(jsonMapper);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> authenticationRequest = new HttpEntity<>(credentials, headers);
        String authenticationResponse = restTemplate.postForObject(
                authenticationUrl,
                authenticationRequest,
                String.class);

        return AuthenticationToken.fromJson(jsonMapper, authenticationResponse).getAccessToken();
    }

    private static class AuthenticationToken {
        private long expires;
        @JsonProperty(value = "access_token")
        private String accessToken;

        private static AuthenticationToken fromJson(ObjectMapper jsonMapper, String token) {
            try {
                return jsonMapper.readValue(token, AuthenticationToken.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public long getExpires() {
            return expires;
        }

        public String getAccessToken() {
            return accessToken;
        }
    }

    private static class Credentials {
        private final String username;
        private final String password;

        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        private String toJson(ObjectMapper jsonMapper) {
            try {
                return jsonMapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
