package pl.edu.mimuw.gtimoszuk.ldap.base;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public abstract class AbstractNamingContextTestWithInitialContext extends AbstractNamingContextTestWith<Context> {

    @Override
    protected Context newContext(Hashtable<String, Object> contextEnvironment) throws NamingException {
        return new InitialContext(contextEnvironment);
    }
}
