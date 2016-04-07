package wimt.othertree.client;

/**
 * Created by Nick Cuthbert on 28/03/2016.
 */
public class NullAuthorizationProvider implements AuthorizationProvider{

    public NullAuthorizationProvider(){
    }

    @Override
    public AccessToken call() throws Exception {
        return null;
    }

    @Override
    public AuthorizationProvider clone() {
        return new NullAuthorizationProvider();
    }
}
