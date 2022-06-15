package sparkles.watson.models;

import com.ibm.cloud.sdk.core.http.Headers;
import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.SessionResponse;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.ResourceBundle;

public class WatsonAssistant {
    private String urlServer = "https://api.us-east.assistant.watson.cloud.ibm.com";
    private Assistant service;
    private AssistantConfig assistentConfig;

    public WatsonAssistant(){
        this.assistentConfig = new AssistantConfig();
        this.generateAssistant();
    }
    public WatsonAssistant(String otherWatsonConfig){
        this.assistentConfig = new AssistantConfig(otherWatsonConfig);
        this.generateAssistant();
    }
    private void generateAssistant(){
        Authenticator authenticator = new IamAuthenticator.Builder()
                .apikey(assistentConfig.getApiKey())
                .build();
        service = new Assistant("2022-06-02", authenticator);
        service.setServiceUrl(urlServer);
        this.test();
    }
    private void test(){

        CreateSessionOptions options = new CreateSessionOptions.Builder(assistentConfig.getId()).build();

        SessionResponse response = service.createSession(options).execute().getResult();

        System.out.println(response);
    }

}
