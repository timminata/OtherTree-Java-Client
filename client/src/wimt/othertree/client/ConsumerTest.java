package wimt.othertree.client;

import com.google.api.client.auth.oauth2.ClientCredentialsTokenRequest;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.NullLogger;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.HttpClientTransport;
import microsoft.aspnet.signalr.client.transport.LongPollingTransport;
import microsoft.aspnet.signalr.client.transport.WebsocketTransport;
import wimt.othertree.oauth.ClientCredentialsOAuthProvider;
import wimt.routethink.AVLDatum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.FutureTask;

/**
 * Created by TimTrewartha on 2016/05/16.
 */
public class ConsumerTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Consumer Test App");

        //Token request details
        String identityUrl="https://identity.whereismytransport.com";
        String scopes = "transitapi:all";
        String clientId = "transitapi_prod_postman_client";

        //Read client secret
        String clientSecret;
        System.out.println("Please enter client secret: ");
        Scanner scanner = new Scanner(System.in);
        clientSecret = scanner.nextLine();

        //Request Access Token
        ClientCredentialsOAuthProvider ia;
        ia = new ClientCredentialsOAuthProvider(identityUrl, identityUrl+"/connect/token",clientId,clientSecret, scopes);
        OtherTreeClient treeClient;
        treeClient = new OtherTreeClient("https://live.whereismytransport.com:8080/", ia);

        treeClient.connect();
        System.out.println("Test if client is connected");
        while(true)
        {
            System.out.println("Client connection status: " + treeClient.isConnected());
            Thread.sleep(3000);
        }
    }
}
