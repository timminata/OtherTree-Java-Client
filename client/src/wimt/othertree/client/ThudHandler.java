package wimt.othertree.client;

import com.google.protobuf.InvalidProtocolBufferException;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler3;

import java.util.List;

/**
 * Created by Nick Cuthbert on 29/03/2016.
 */
public class ThudHandler implements SubscriptionHandler3<String, Version, String> {

    private List<AnnotatedOnThudListener> onThudListeners;
    public ThudHandler(List<AnnotatedOnThudListener> onThudListeners){
        this.onThudListeners=onThudListeners;
    }



    @Override
    public void run(String typename, Version version, String payload) {
        byte[] bytes=OtherTreeTypeUtil.toByteArray(payload);
        for (AnnotatedOnThudListener handler: onThudListeners) {
            if(handler.applicable(typename,version)){
                try {
                    handler.onThud(bytes);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
