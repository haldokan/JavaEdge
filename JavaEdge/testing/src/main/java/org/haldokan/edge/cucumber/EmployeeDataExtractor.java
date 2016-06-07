package org.haldokan.edge.cucumber;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.local.LocalFile;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Extract all employee data from ConnectION and ftp it to Everbridge
 * Created by haytham.aldokanji on 6/3/16.
 */

//TODO REPLACE all sout with the logging
public class EmployeeDataExtractor {
    private static final String USERNAME_KEY = "uname";
    private static final String PASSWORD_KEY = "pwd";
    private static final String URL_KEY = "url";
    private static final String FTP_HOST_KEY = "ftpHost";
    private static final String FTP_PORT_KEY = "ftpPort";
    private static final String FTP_KEY_FILE_KEY = "ftpKeyFile";
    private static final String FTP_USER_KEY = "ftpUser";
    private static final String AUTH_ENDPOINT_KEY = "authEPoint";
    private static final String DATA_ENDPOINT_KEY = "dataEPoint";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String CONN_AUTH_HEADER_KEY = "x-connection-authorization";
    private static final String DEFAULT_AUTH_ENDPOINT = "/api/auth/token";
    private static final String DEFAULT_DATA_ENDPOINT = "/api/platform/employees";
    private static final String DEFAULT_TOKEN_PREFIX = "Bearer";
    private static final int DEFAULT_FTP_PORT = 22;
    private static final String DEFAULT_FTP_KEY_FILE = "IONTrading-ossh.ppk";

    private final RestTemplate restTemplate;
    private final Map<String, String> runConfigsTable;
    private final ObjectMapper jsonMapper;

    private EmployeeDataExtractor(String[] runConfigs) {
        runConfigsTable = parseRunConfigs(runConfigs);
        restTemplate = new RestTemplate();
        jsonMapper = new ObjectMapper();
        System.out.println("Run Configs: " + runConfigsTable);
    }

    public static void main(String[] runConfigs) {
        try {
            EmployeeDataExtractor extractor = new EmployeeDataExtractor(runConfigs);
            extractor.run();
        } catch (Throwable throwable) {
            System.out.println(Throwables.getStackTraceAsString(throwable));
            System.exit(1);
        }
    }

    public void run() {
        String authenticationToken = getAuthenticationToken();
        String extract = downloadExtract(authenticationToken);
        uploadExtract(extract);
    }

    private String downloadExtract(String authenticationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(CONN_AUTH_HEADER_KEY, Arrays.asList(TOKEN_PREFIX + " " + authenticationToken));
        HttpEntity<String> request = new HttpEntity<>(headers);

        String dataUrl = runConfigsTable.get(URL_KEY) + runConfigsTable.get(DATA_ENDPOINT_KEY);
        ResponseEntity<String> response = restTemplate.exchange(dataUrl, HttpMethod.GET, request, String.class);

        return response.getBody();
    }


    public void uploadExtract(String extract) {
        // do transformation and create file
        try {
            ftp(runConfigsTable.get(FTP_HOST_KEY),
                    runConfigsTable.get(FTP_USER_KEY), "/Users/haytham.aldokanji/misc/IONTrading-ossh.ppk",
                    "replace", "Testfile3", "/Users/haytham.aldokanji/misc/", "everbridge");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String getAuthenticationToken() {
        String authenticationUrl = runConfigsTable.get(URL_KEY) + runConfigsTable.get(AUTH_ENDPOINT_KEY);
        System.out.println("authenticating using endpoint: " + authenticationUrl);

        String credentials = new CredentialsConverter(runConfigsTable.get(USERNAME_KEY),
                runConfigsTable.get(PASSWORD_KEY)).toJson(jsonMapper);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> authenticationRequest = new HttpEntity<>(credentials, headers);
        String authenticationResponse = restTemplate.postForObject(
                authenticationUrl,
                authenticationRequest,
                String.class);

        return AuthenticationTokenConverter.fromJson(jsonMapper, authenticationResponse).getAccessToken();
    }

    private Map<String, String> parseRunConfigs(String[] runConfigs) {
        Map<String, String> paramMap = new LinkedHashMap<>();

        for (String param : runConfigs) {
            String[] paramParts = param.split("=");
            String paramKey = paramParts[0].trim();
            String paramValue = paramParts.length == 2 ? paramParts[1].trim() : null;

            if (paramKey.isEmpty() || paramValue == null || paramValue.isEmpty()) {
                throw new IllegalArgumentException("Parameters are mal-formatted: " + param);
            }
            paramMap.put(paramKey, paramValue);
        }

        if (!paramMap.containsKey(URL_KEY) || !paramMap.containsKey(USERNAME_KEY)
                || !paramMap.containsKey(PASSWORD_KEY) || !paramMap.containsKey(FTP_HOST_KEY)
                || !paramMap.containsKey(FTP_USER_KEY)) {

            throw new IllegalArgumentException("At minimum the ConnectION 'url', 'username', and 'password'; " +
                    "and Everbridge 'ftpHost', 'ftpUser' should be provided");
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
        if (!paramMap.containsKey(FTP_PORT_KEY)) {
            paramMap.put(FTP_PORT_KEY, String.valueOf(DEFAULT_FTP_PORT));
        }
        if (!paramMap.containsKey(FTP_KEY_FILE_KEY)) {
            paramMap.put(FTP_KEY_FILE_KEY, DEFAULT_FTP_KEY_FILE);
        }

        return paramMap;
    }

    public void ftp(String server, String userName, String openSSHPrivateKey,
                    String remoteDir, String remoteFile, String localDir, String localFileName) throws IOException {

        FileSystemOptions fsOptions = new FileSystemOptions();
        FileSystemManager fsManager = null;
        String remoteURL = "sftp://" + userName + "@" + server + "/" + remoteDir + "/" + remoteFile;
        String localURL = "file://" + localDir + "/" + localFileName;

        try {
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(fsOptions, "no");
            SftpFileSystemConfigBuilder.getInstance().setIdentities(fsOptions, new File(openSSHPrivateKey));
            fsManager = VFS.getManager();

            FileObject remoteFileObject = fsManager.resolveFile(remoteURL, fsOptions);
            LocalFile localFile = (LocalFile) fsManager.resolveFile(localURL);
            remoteFileObject.copyFrom(localFile, Selectors.SELECT_SELF);
            System.out.println("copied " + localFile + " -> " + remoteFileObject);
        } catch (FileSystemException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    private static class AuthenticationTokenConverter {
        private long expires;
        @JsonProperty(value = "access_token")
        private String accessToken;

        private static AuthenticationTokenConverter fromJson(ObjectMapper jsonMapper, String token) {
            try {
                return jsonMapper.readValue(token, AuthenticationTokenConverter.class);
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

    private static class CredentialsConverter {
        private final String username;
        private final String password;

        public CredentialsConverter(String username, String password) {
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
