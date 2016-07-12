package org.haldokan.edge.cucumber;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.Lists;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.sftp.IdentityInfo;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Extract all employee data from ConnectION and sftp it to Everbridge
 */
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
    private static final String DEFAULT_FTP_REMOTE_DIR = "replace";
    private static final String DEFAULT_FTP_FILE_NAME = "employee-contacts";
    private static final int DEFAULT_FTP_PORT = 22;
    //Note: the one Evrebridge provides does not work: has first to be converted to ossh using puttyGen
    private static final String DEFAULT_FTP_PRIVATE_KEY_FILE = "/Users/haytham.aldokanji/misc/IONTrading-ossh.ppk";

    private static final Logger logger = LoggerFactory.getLogger(EmployeeDataExtractor.class);

    private final RestTemplate restTemplate;
    private final Map<String, String> runConfigsTable;
    private final ObjectMapper jsonMapper;

    private EmployeeDataExtractor(String[] runConfigs) {
        runConfigsTable = parseRunConfigs(runConfigs);
        restTemplate = new RestTemplate();
        jsonMapper = new ObjectMapper();
        logger.info("Run Configs: " + runConfigsTable);
    }

    public static void main(String[] runConfigs) {
        try {
            EmployeeDataExtractor extractor = new EmployeeDataExtractor(runConfigs);
            extractor.run();
        } catch (Throwable throwable) {
            logger.error("JOB FAILED!", throwable);
            System.exit(1);
        }
    }

    public void run() throws IOException {
        String authenticationToken = getAuthenticationToken();
        List<Contact> contactsExtract = downloadContactsExtract(authenticationToken);
        uploadContactsExtract(contactsExtract);
    }

    private String getAuthenticationToken() throws IOException {
        String authenticationUrl = runConfigsTable.get(URL_KEY) + runConfigsTable.get(AUTH_ENDPOINT_KEY);
        logger.info("Authenticating using endpoint: " + authenticationUrl);

        String credentials = new CredentialsConverter(runConfigsTable.get(USERNAME_KEY),
                runConfigsTable.get(PASSWORD_KEY)).toJson(jsonMapper);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> authenticationRequest = new HttpEntity<>(credentials, headers);
        String authenticationResponse = restTemplate.postForObject(
                authenticationUrl,
                authenticationRequest,
                String.class);

        String token = AuthenticationTokenConverter.fromJson(jsonMapper, authenticationResponse).getAccessToken();
        logger.info("Authentication succeeded - Token obtained");
        return token;
    }

    private List<Contact> downloadContactsExtract(String authenticationToken) throws IOException {
        logger.info("Start downloading contacts...");
        HttpHeaders headers = new HttpHeaders();
        headers.put(CONN_AUTH_HEADER_KEY, Lists.newArrayList(TOKEN_PREFIX + " " + authenticationToken));
        HttpEntity<String> request = new HttpEntity<>(headers);

        String dataUrl = runConfigsTable.get(URL_KEY) + runConfigsTable.get(DATA_ENDPOINT_KEY);
        ResponseEntity<String> response = restTemplate.exchange(dataUrl, HttpMethod.GET, request, String.class);
        List<Contact> contacts = jsonMapper.readValue(response.getBody(), new TypeReference<List<Contact>>() {
        });

        logger.info("Downloading contacts succeeded - " + contacts.size() + " contacts downloaded");
        return contacts;
    }

    private void uploadContactsExtract(List<Contact> contacts) throws IOException {
        logger.info("Start uploading contacts...");
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(Contact.class);

        String fileName = DEFAULT_FTP_FILE_NAME + LocalDate.now();
        Path ftpLocalPath = Paths.get(runConfigsTable.get(FTP_LOCAL_DIR_KEY), fileName);

        Writer writer = Files.newBufferedWriter(ftpLocalPath);
        writer.write(Contact.getCsvHeader());
        mapper.writer(schema).writeValue(writer, contacts);

        logger.info("Local CSV file is created: " + ftpLocalPath);
        ftpContacts(ftpLocalPath);
        logger.info("Uploading contacts succeeded");
    }

    private void ftpContacts(Path ftpLocalPath) throws IOException {
        logger.info("Start FTP'ing contacts...");
        FileSystemOptions fsOptions = new FileSystemOptions();
        FileSystemManager fsManager;

        //TODO: constructing the urls should be done once (at least for the remote url)
        String remoteURL = "sftp://" + runConfigsTable.get(FTP_USER_KEY) + "@" + runConfigsTable.get(FTP_HOST_KEY)
                + "/" + runConfigsTable.get(FTP_REMOTE_DIR_KEY) + "/" + runConfigsTable.get(FTP_REMOTE_FILE_KEY);
        logger.info("FTP remote location: " + remoteURL);
        String localURL = "file://" + ftpLocalPath;

        SftpFileSystemConfigBuilder sftpConfigs = SftpFileSystemConfigBuilder.getInstance();

        sftpConfigs.setStrictHostKeyChecking(fsOptions, "no");
        IdentityInfo privateKey = new IdentityInfo(new File(runConfigsTable.get(FTP_PRIVATE_KEY_FILE_KEY)));
        sftpConfigs.setIdentityInfo(fsOptions, privateKey);

        fsManager = VFS.getManager();
        FileObject remoteFileObject = fsManager.resolveFile(remoteURL, fsOptions);
        FileObject localFile = fsManager.resolveFile(localURL);
        remoteFileObject.copyFrom(localFile, Selectors.SELECT_SELF);

        logger.info("FTP succeeded");
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
            paramMap.put(FTP_REMOTE_DIR_KEY, DEFAULT_FTP_REMOTE_DIR);
        }
        if (!paramMap.containsKey(FTP_REMOTE_FILE_KEY)) {
            paramMap.put(FTP_REMOTE_FILE_KEY, DEFAULT_FTP_FILE_NAME);
        }
        return paramMap;
    }

    private static class AuthenticationTokenConverter {
        private long expires;
        @JsonProperty(value = "access_token")
        private String accessToken;

        private static AuthenticationTokenConverter fromJson(ObjectMapper jsonMapper, String token) throws IOException {
            return jsonMapper.readValue(token, AuthenticationTokenConverter.class);
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
