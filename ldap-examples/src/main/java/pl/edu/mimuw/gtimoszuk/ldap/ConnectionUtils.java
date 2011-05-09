package pl.edu.mimuw.gtimoszuk.ldap;

import javax.naming.Context;
import java.util.Hashtable;

//temporary
public class ConnectionUtils {
    public static Hashtable<String, Object> prepareAnonymousSignUpEnvironment() {
        Hashtable<String, Object> env = new Hashtable<String, Object>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:1389/o=JNDITutorial");
        return env;
    }

    public static Hashtable<String, Object> prepareDirectoryManagerSignUpEnvironment() {
        Hashtable<String, Object> env = new Hashtable<String, Object>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:1389/o=JNDITutorial");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "cn=Directory Manager");
        env.put(Context.SECURITY_CREDENTIALS, "manager");
        return env;
    }
}
