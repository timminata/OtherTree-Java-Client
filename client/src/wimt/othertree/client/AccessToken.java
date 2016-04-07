package wimt.othertree.client;

/**
 * Created by Nick Cuthbert on 28/03/2016.
 */
public class AccessToken {
    public final String token;
    public final Long expiryTime;
    public AccessToken(String token, Long timeoutInNanos){
        this.token=token;
        this.expiryTime=System.nanoTime()+timeoutInNanos;
    }

    public boolean isExpired(){
        return (System.nanoTime()>expiryTime);
    }
}
