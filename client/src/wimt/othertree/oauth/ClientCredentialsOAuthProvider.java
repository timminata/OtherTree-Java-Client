package wimt.othertree.oauth;
import com.google.api.client.auth.oauth2.ClientCredentialsTokenRequest;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import wimt.othertree.client.AccessToken;
import wimt.othertree.client.AuthorizationProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Created by Nick Cuthbert on 28/03/2016.
 */
public class ClientCredentialsOAuthProvider implements AuthorizationProvider{

    private final String identityUrl;
    private final String tokenAuthorizationUrl;
    private final String clientId;
    private final String clientSecret;
    private final String scopes;
    private ExecutionException _exception=null;
    private String _accessToken;

    public ClientCredentialsOAuthProvider(String identityUrl, String tokenAuthorizationUrl, String clientId, String clientSecret, String scopes){
        this.identityUrl = identityUrl;
        this.tokenAuthorizationUrl = tokenAuthorizationUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scopes = scopes;
    }


    @Override
    public AuthorizationProvider clone() {
        return new ClientCredentialsOAuthProvider(identityUrl, tokenAuthorizationUrl, clientId, clientSecret, scopes);
    }

    @Override
    public AccessToken call() throws Exception {
        ArrayList<String> scopesList=new ArrayList<String>(Arrays.asList(scopes.split(" ")));

        ClientCredentialsTokenRequest clientCredentialsTokenRequest= new ClientCredentialsTokenRequest(new NetHttpTransport(),
                new JacksonFactory(),
                new GenericUrl(tokenAuthorizationUrl))
                .setScopes(scopesList)
                .setClientAuthentication(new ClientParametersAuthentication(clientId, clientSecret));

        TokenResponse response=clientCredentialsTokenRequest.execute();
        response.getExpiresInSeconds();
        return new AccessToken(response.getAccessToken(),response.getExpiresInSeconds()*1000000000);

    }
}
