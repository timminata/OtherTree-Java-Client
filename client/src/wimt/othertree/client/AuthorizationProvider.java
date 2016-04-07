package wimt.othertree.client;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;

/**
 * Created by Nick Cuthbert on 28/03/2016.
 */
public interface AuthorizationProvider extends Callable<AccessToken>{
    AuthorizationProvider clone();
}
