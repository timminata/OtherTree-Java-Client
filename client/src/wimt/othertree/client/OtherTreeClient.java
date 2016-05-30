package wimt.othertree.client;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.*;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler3;
import microsoft.aspnet.signalr.client.transport.LongPollingTransport;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by Nick Cuthbert on 05/01/2016.
 */


public class OtherTreeClient implements AutoCloseable {

    protected String url;
    private HubConnection connection;
    private HubProxy hub;
    private boolean connected=false;
    private List<AnnotatedOnThudListener> onThudListeners;
    private Logger logger;
    private AccessToken currentToken =null;
    private AuthorizationProvider provider;

    public OtherTreeClient(String url, AuthorizationProvider provider){
        this(url,  provider, true, new NullLogger());
    }

    public OtherTreeClient(String url, AuthorizationProvider provider, boolean useDefaultUrl){
        this(url,  provider, useDefaultUrl, new NullLogger());
    }

    public OtherTreeClient(String url, AuthorizationProvider provider, boolean useDefaultUrl, Logger logger){
        this.logger=logger;
        this.provider=provider;
        onThudListeners=new ArrayList<>();
        init(url,provider,useDefaultUrl);
    }

    protected OtherTreeClient(){
        this("",new NullAuthorizationProvider(),true,new NullLogger());
    }

    public microsoft.aspnet.signalr.client.ConnectionState isConnected()
    {
        return connection.getState();
    }


    protected void  init(String url, final AuthorizationProvider provider, boolean useDefaultUrl){
        this.url=url;
        HashMap<Object, Object> dict = new HashMap<Object, Object>();
        String query = "&session_guid="+ UUID.randomUUID().toString();
        connection= new HubConnection(url, query, useDefaultUrl, logger);

    }

    public Future<Void> futureConnection(){

        connection.setCredentials(new Credentials() {
            @Override
            public void prepareRequest(Request request) {
                try {
                    final AccessToken at = provider.call();
                    request.addHeader("Authorization", "Bearer " + at.token);
                }catch (Exception e){
                    logger.log(e.getMessage(),LogLevel.Critical);
                }
            }
        });

        hub = connection.createHubProxy("Bedrock");

        hub.on("Thud", new ThudHandler(onThudListeners), String.class, Version.class, String.class);

        SignalRFuture<Void> futureConnectionTask = connection.start(new LongPollingTransport(connection.getLogger()));

        futureConnectionTask = futureConnectionTask.done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {
                synchronized (this) {
                    connected = true;
                }
            }
        });

        return futureConnectionTask;
    }

    byte[] toByteArray(String hexString){
        return Base64.decode(hexString, Base64.DEFAULT);
    }

    public void connect() throws ExecutionException, InterruptedException, IllegalMonitorStateException {
        futureConnection();
    }

    private static int[] toIntArray(byte[] bytes){
        final int[] ints = new int[bytes.length];
        for(int i=0;i<ints.length;i++){
            ints[i] = bytes[i]& 0xFF;
        }
        return ints;
    }



    public <T extends com.google.protobuf.GeneratedMessage> SignalRFuture<Void> futureWater(T water, Version version){
        int[] ints= toIntArray(water.toByteArray());
        return hub.invoke("Water",toPascalCase(water.getDescriptorForType().getFullName()), version,ints);
    }

    public <T extends com.google.protobuf.GeneratedMessage> void water (T water,Version version) throws ExecutionException, InterruptedException {
        futureWater(water, version).get();
    }



            public <T extends com.google.protobuf.GeneratedMessage> SignalRFuture<ChargeReference> futureCharge(T charge, Version version){
                int[] ints= toIntArray(charge.toByteArray());
                return hub.invoke(ChargeReference.class, "Charge", toPascalCase(charge.getDescriptorForType().getFullName()), version, ints);
            }
            public <T extends com.google.protobuf.GeneratedMessage> SignalRFuture<ChargeReference> futureCharge(T charge){
                return futureCharge(charge, new Version());
            }

            public SignalRFuture<Void> futureDischarge(ChargeReference reference){
                return hub.invoke("Discharge", reference);
            }


        public <T extends com.google.protobuf.GeneratedMessage> ChargeReference charge (T charge,Version version) throws ExecutionException, InterruptedException {
            return futureCharge(charge,version).get();
        }
        public <T extends com.google.protobuf.GeneratedMessage> ChargeReference charge (T charge) throws ExecutionException, InterruptedException {
            return charge(charge, new Version());
        }

        public  void discharge(ChargeReference reference) throws ExecutionException, InterruptedException {
            futureDischarge(reference).get();
        }


        public <T extends GeneratedMessage> void addOnThudListener(Class<T> expectedType,ThudListener<T> listener,Version version){
            AnnotatedOnThudListener<T> handler = new AnnotatedOnThudListener<>(expectedType,listener, version);
            onThudListeners.add(handler);
        }

        public <T extends GeneratedMessage> void addOnThudListener(Class<T> expectedType,ThudListener<T> listener){
            addOnThudListener(expectedType, listener, new Version());
        }

        static String toPascalCase(String camelCase){
            StringBuilder builder=new StringBuilder();
            String[] strings=camelCase.split("\\.");
            for(int i=0; i<strings.length; ++i){

            if(strings[i].length()>0){
                builder.append(Character.toUpperCase(strings[i].charAt(0)));
                builder.append(strings[i].substring(1));
                if(i!=strings.length-1){
                    builder.append('.');
                }
            }
        }
        return builder.toString();
    }

    public <T extends GeneratedMessage>void removeOnThudListener(ThudListener<T> thudListener){
        for(int i=onThudListeners.size()-1; i>=0; --i){
            if(onThudListeners.get(i).thudListener==thudListener){
                onThudListeners.remove(i);
            }
        }
    }


    public synchronized void disconnect(){
        if(connected){
            connection.disconnect();
            connected=false;
        }
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }
}
