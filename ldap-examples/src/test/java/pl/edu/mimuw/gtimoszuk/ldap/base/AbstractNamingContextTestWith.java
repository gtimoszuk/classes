package pl.edu.mimuw.gtimoszuk.ldap.base;

import org.junit.After;
import org.junit.Before;
import pl.edu.mimuw.gtimoszuk.ldap.Fixture;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import java.util.Hashtable;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

abstract public class AbstractNamingContextTestWith<T extends Context> {

    private T context;

    protected T getContext() {
        return context;
    }

    protected abstract T newContext(Hashtable<String,Object> contextEnvironment) throws NamingException;

    @Before
    public final void setUp() throws NamingException {
        createContextUsing(Fixture.directoryManagerConnectionParameters);
        augumentGivens();
        closeContext();
    }

    @After
    public final void tearDown() throws NamingException {
        closeContext();
        createContextUsing(Fixture.directoryManagerConnectionParameters);
        cleanUpPossibleFixtureChanges();
        closeContext();
    }

    protected void augumentGivens() throws NamingException {
    }

    protected void cleanUpPossibleFixtureChanges() throws NamingException {
    }

    protected void createContextUsing(Hashtable<String, Object> connectionParameters) throws NamingException {
        context = newContext(connectionParameters);
    }

    private void closeContext() throws NamingException {
        if (context != null) {
            context.close();
        }
    }

}
