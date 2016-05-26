package wimt.othertree.client.android;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.http.android.*;
import wimt.othertree.client.OtherTreeClient;
import wimt.othertree.client.AuthorizationProvider;

/**
 * Created by Nick Cuthbert on 05/01/2016.
 */
public class OtherTreeAndroidClient extends OtherTreeClient{

        public OtherTreeAndroidClient(String url, AuthorizationProvider provider){
           this(url,provider,true);
        }

        public OtherTreeAndroidClient(String url, AuthorizationProvider provider, boolean useDefaultUrl){
            super();
            Platform.loadPlatformComponent(new AndroidPlatformComponent());
            super.init(url, provider, useDefaultUrl);
        }
}