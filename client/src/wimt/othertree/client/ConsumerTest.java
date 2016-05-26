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
import java.util.UUID;
import java.util.concurrent.FutureTask;

/**
 * Created by TimTrewartha on 2016/05/16.
 */
public class ConsumerTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Consumer Test App");
        //Request Access Token
        ClientCredentialsOAuthProvider ia;
        ia = new ClientCredentialsOAuthProvider("https://identity.whereismytransport.com","https://identity.whereismytransport.com/connect/token","transitapi_prod_postman_client","wimt85!", "transitapi:all");
        OtherTreeClient treeClient;
        treeClient = new OtherTreeClient("https://live.whereismytransport.com:8080/", ia);

        //add listener
        //Class<T> java;
        //AVLDatum avlDatum;
        //treeClient.addOnThudListener(AVLDatum,);

        treeClient.connect();
        System.out.println("Test if client is connected");
        while(true)
        {
            System.out.println("Client connection status: " + treeClient.isConnected());
            Thread.sleep(3000);
        }


        /*String tokenAuthorizationUrl = "https://identity.whereismytransport.com/connect/token";
        String scopes = "transitapi:all";
        ArrayList<String> scopesList = new ArrayList<String>(Arrays.asList(scopes.split(" ")));
        String clientSecret = "wimt85!";
        String clientId = "transitapi_prod_postman_client";

        ClientCredentialsTokenRequest clientCredentialsTokenRequest = new ClientCredentialsTokenRequest(new NetHttpTransport(),
                new JacksonFactory(),
                new GenericUrl(tokenAuthorizationUrl))
                .setScopes(scopesList)
                .setClientAuthentication(new ClientParametersAuthentication(clientId, clientSecret));
        final TokenResponse response = clientCredentialsTokenRequest.execute();
        System.out.println("The access token is: " + response.getAccessToken());
        System.out.println("Expiry time (seconds): " + response.getExpiresInSeconds());

        String otherTreeUrl = "https://live.whereismytransport.com:8080/";
        HubConnection connection;
        HubProxy hub;
        boolean useDefaultUrl = true;
        boolean connected = false;
        Logger logger = new NullLogger();
        String query = "&session_guid=" + UUID.randomUUID().toString();
        System.out.println("Query string: " + query);

        //initialise the connection and set credentials
        connection = new HubConnection(otherTreeUrl, query, useDefaultUrl, logger);

        connection.setCredentials(new Credentials() {
            public void prepareRequest(Request request) {
                request.addHeader("Authorization", "Bearer "+ response.getAccessToken());
            }
        });
        hub = connection.createHubProxy("Bedrock");
        System.out.println("Hub ID: "+hub);

        //Add listener?
        hub.on("ReceivedResults", null);

        //Long polling transport times out after about 20 seconds
        LongPollingTransport lpt = new LongPollingTransport(logger);
        WebsocketTransport wst = new WebsocketTransport(new NullLogger());
        ClientTransport transport = new LongPollingTransport(new NullLogger());

        SignalRFuture<Void> futureConnectionTask = connection.start(lpt);
        System.out.println("Long Polling transport keep alive: " + lpt.supportKeepAlive());
        System.out.println("Web Socket keep alive: " + wst.supportKeepAlive());

        //connection.start();
        //System.in.read();
        while(true) {
            //connection.start(new LongPollingTransport(new NullLogger()));
            System.out.println("Hub Connection state: " + connection.getState());
            Thread.sleep(3000);
            //System.out.println("Connection id: " + connection.getConnectionId());
        }
        */

    }
}
