package pl.edu.mimuw.gtimoszuk.ldap.base;

import com.google.common.base.Preconditions;
import org.junit.After;
import org.junit.Before;

import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Hashtable;

import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.directoryManagerConnectionParameters;

abstract public class AbstractNamingContextTestWith<T extends Context> {

    private T context;

    protected abstract T newContext(Hashtable<String,Object> contextEnvironment) throws NamingException;

    protected void augumentGivens() throws NamingException {
    }

    protected void cleanUpPossibleFixtureChanges() throws NamingException {
    }

    protected T getContext() {
        Preconditions.checkState(context != null, "Could not get context - no open context present");
        return context;
    }

    @Before
    public final void setUp() throws NamingException {
        createContextUsing(directoryManagerConnectionParameters);
        augumentGivens();
        closeContext();
    }

    @After
    public final void tearDown() throws NamingException {
        closeContext();
        createContextUsing(directoryManagerConnectionParameters);
        cleanUpPossibleFixtureChanges();
        closeContext();
    }

    protected void createContextUsing(Hashtable<String, Object> connectionParameters) throws NamingException {
        context = newContext(connectionParameters);
    }

    private void closeContext() throws NamingException {
        if (context != null) {
            context.close();
        }
        context = null;
    }

}
