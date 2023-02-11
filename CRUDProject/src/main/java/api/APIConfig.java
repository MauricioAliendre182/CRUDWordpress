package api;

import utils.LoggerManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class APIConfig {
    private Properties properties;
    private static final LoggerManager log = LoggerManager.getInstance();
    private static final String apiFilePath = System.getProperty("user.dir") + File.separator + "api.properties";
    private static APIConfig instance;

    private APIConfig() {
        initialize();
    }

    private void initialize() {
        log.info("Reading API service config");
        properties = new Properties();
        Properties apiProperties = new Properties();
        try {
            apiProperties.load(new FileInputStream(apiFilePath));
        } catch (IOException e) {
            log.error("unable to load properties file");
        }
        properties.putAll(apiProperties);
    }

    public static APIConfig getInstance() {
        if (instance == null) {
            instance = new APIConfig();
        }
        return instance;
    }

    private String getAPISetting(String setting) {
        return (String) getInstance().properties.get(setting);
    }

    public String getBasePath() {
        return getAPISetting("api.basePath");
    }

    public int getAPIServicePort() {
        return Integer.parseInt(getAPISetting("api.service.port"));
    }

    public String getTokenEndpoint() {
        return  getAPISetting("api.endpoint.token");
    }

    public String getPostsEndpoint() {
        return getAPISetting("api.endpoint.users");
    }

    public String getPostsByIdEndpoint() {
        return getAPISetting("api.endpoint.postsById");
    }
}
