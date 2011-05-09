package pl.edu.mimuw.gtimoszuk.ldap.base;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public abstract class AbstractNamingContextTestWithInitialDirContext extends AbstractNamingContextTestWith<DirContext> {

    @Override
    protected DirContext newContext(Hashtable<String, Object> contextEnvironment) throws NamingException {
        return new InitialDirContext(contextEnvironment);
    }
}
