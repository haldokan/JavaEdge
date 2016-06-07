package org.haldokan.edge.cucumber;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.sftp.IdentityInfo;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
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
    private static final String FTP_PRIVATE_KEY_FILE_KEY = "ftpKeyFile";
    private static final String FTP_USER_KEY = "ftpUser";
    private static final String FTP_LOCAL_DIR_KEY = "ftpLocalDir";
    private static final String FTP_REMOTE_DIR_KEY = "ftpRemoteDir";
    private static final String FTP_REMOTE_FILE_KEY = "ftpRemoteFile";
    private static final String AUTH_ENDPOINT_KEY = "authEPoint";
    private static final String DATA_ENDPOINT_KEY = "dataEPoint";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String CONN_AUTH_HEADER_KEY = "x-connection-authorization";
    private static final String DEFAULT_AUTH_ENDPOINT = "/api/auth/token";
    private static final String DEFAULT_DATA_ENDPOINT = "/api/platform/employees";
    private static final String DEFAULT_TOKEN_PREFIX = "Bearer";
    private static final String DEFAULT_FTP_LOCAL_DIR = System.getProperty("java.io.tmpdir");
    private static final String DEFAULT_FTP_REMOTEL_DIR = "replace";
    private static final String DEFAULT_FTP_REMOTEL_File = "employee-contacts";
    private static final int DEFAULT_FTP_PORT = 22;
    //Note: the one Evrebridge provides does not work: has first to be converted to ossh using puttyGen
    private static final String DEFAULT_FTP_PRIVATE_KEY_FILE = "/Users/haytham.aldokanji/misc/IONTrading-ossh.ppk";

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
        headers.put(CONN_AUTH_HEADER_KEY, Lists.newArrayList(TOKEN_PREFIX + " " + authenticationToken));
        HttpEntity<String> request = new HttpEntity<>(headers);

        String dataUrl = runConfigsTable.get(URL_KEY) + runConfigsTable.get(DATA_ENDPOINT_KEY);
        ResponseEntity<String> response = restTemplate.exchange(dataUrl, HttpMethod.GET, request, String.class);

        return response.getBody();
    }

    private void uploadExtract(String extract) {
        // do transformation and create file
        try {
            ftp("everbridge");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getAuthenticationToken() {
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

    // TODO: this will go away once we used Springboot
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
        if (!paramMap.containsKey(FTP_PRIVATE_KEY_FILE_KEY)) {
            paramMap.put(FTP_PRIVATE_KEY_FILE_KEY, DEFAULT_FTP_PRIVATE_KEY_FILE);
        }
        if (!paramMap.containsKey(FTP_LOCAL_DIR_KEY)) {
            paramMap.put(FTP_LOCAL_DIR_KEY, DEFAULT_FTP_LOCAL_DIR);
        }
        if (!paramMap.containsKey(FTP_REMOTE_DIR_KEY)) {
            paramMap.put(FTP_REMOTE_DIR_KEY, DEFAULT_FTP_REMOTEL_DIR);
        }
        if (!paramMap.containsKey(FTP_REMOTE_FILE_KEY)) {
            paramMap.put(FTP_REMOTE_FILE_KEY, DEFAULT_FTP_REMOTEL_File);
        }
        return paramMap;
    }

    private void ftp(String fileName) {
        FileSystemOptions fsOptions = new FileSystemOptions();
        FileSystemManager fsManager;
        //TODO: constructing the urls should be done once (at least for the remote url)
        String remoteURL = "sftp://" + runConfigsTable.get(FTP_USER_KEY) + "@" + runConfigsTable.get(FTP_HOST_KEY)
                + "/" + runConfigsTable.get(FTP_REMOTE_DIR_KEY) + "/" + runConfigsTable.get(FTP_REMOTE_FILE_KEY);
        String localURL = "file://" + runConfigsTable.get(FTP_LOCAL_DIR_KEY) + "/" + fileName;

        try {
            SftpFileSystemConfigBuilder sftpConfigs = SftpFileSystemConfigBuilder.getInstance();

            sftpConfigs.setStrictHostKeyChecking(fsOptions, "no");
            IdentityInfo privateKey = new IdentityInfo(new File(runConfigsTable.get(FTP_PRIVATE_KEY_FILE_KEY)));
            sftpConfigs.setIdentityInfo(fsOptions, privateKey);

            fsManager = VFS.getManager();
            FileObject remoteFileObject = fsManager.resolveFile(remoteURL, fsOptions);
            FileObject localFile = fsManager.resolveFile(localURL);
            remoteFileObject.copyFrom(localFile, Selectors.SELECT_SELF);

            System.out.println("copied " + localFile + " -> " + remoteFileObject);
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
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
