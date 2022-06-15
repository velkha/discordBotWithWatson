package sparkles.watson.models;

import java.util.ResourceBundle;

public class AssistantConfig {
    private static final String STANDARD_SETTINGS_PATH = "watson";

    private String id;
    private String apiKey;
    private String url;
    private String name;

    public AssistantConfig() {
        this(STANDARD_SETTINGS_PATH);
    }
    public AssistantConfig(String resourcePath){
        this.generateConfig(resourcePath);
    }

    private void generateConfig(String resourcePath) {
        ResourceBundle watson_settings = ResourceBundle.getBundle(resourcePath);
        this.id = watson_settings.getString("watson.id");
        this.apiKey = watson_settings.getString("watson.apiKey");
        this.url  = watson_settings.getString("watson.url");
        this.name  = watson_settings.getString("watson.name");
    }

    public String getId() {
        return id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}
