package pl.edu.mimuw.gtimoszuk.ldap;

import com.sun.jndi.ldap.LdapCtxFactory;

import javax.naming.Context;
import java.util.Hashtable;

public class Fixture {
    public static final String ldapServerHostname = "localhost";
    public static final int ldapServerPort = 1389;

    public static final Hashtable<String, Object> anonymousConnectionParameters =
            prepareAnonymousSignUpEnvironment(ldapServerHostname, ldapServerPort);

    public static final Hashtable<String, Object> directoryManagerConnectionParameters =
            prepareDirectoryManagerSignUpEnvironment(ldapServerHostname, ldapServerPort);

    public static final int numberOfEntriesUnderPeopleDN = 21;

    public static final String scottSeligmanCN = "cn=Scott Seligman";
    public static final String rosannaLeeRelativeName = "cn=Rosanna Lee,ou=People";
    public static final String nonexistentPersonRelativeName = "cn=Nonexistent Person, ou=People";

    public static final String atFirstUnusedCN = "cn=At-first-unused CN";
    public static final String atFirstUnusedOU = "ou=NewOu";

    public static final String tedGeiselRelativeName = "cn=Ted Geisel, ou=People";
    public static final int tedGeiselAttributesSize = 7;
    public static final int tedGeiselObjectclassesCount = 4;
    public static final String tedGeiselMail = "Ted.Geisel@JNDITutorial.com";
    public static final String attributeNotDefinedForTedGeisel = LdapAttributesNames.golfHandicap;
    public static final int numberOfEntriesWithSnGeisel = 1;

    public static String peopleOU = "ou=People";
    public static String tedGeiselTelephoneNumber = "+1 408 555 5252";
    public static int numberOfTedGeiselTelephoneNumbers = 1;

    //TODO remove duplication, extract constants
    private static Hashtable<String, Object> prepareAnonymousSignUpEnvironment(String ldapServerHostname, int ldapServerPort) {
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, LdapCtxFactory.class.getCanonicalName());
        env.put(Context.PROVIDER_URL, "ldap://" + ldapServerHostname + ":" + ldapServerPort + "/o=JNDITutorial");
        return env;
    }

    private static Hashtable<String, Object> prepareDirectoryManagerSignUpEnvironment(String ldapServerHostname, int ldapServerPort) {
        Hashtable<String, Object> env = new Hashtable<String, Object>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://" + ldapServerHostname + ":" + ldapServerPort + "/o=JNDITutorial");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "cn=Directory Manager");
        env.put(Context.SECURITY_CREDENTIALS, "manager");
        return env;
    }

    public static Hashtable<String, Object> prepareDirectoryManagerSignUpEnvironmentAtPeopleDN(String ldapServerHostname, int ldapServerPort) {
        Hashtable<String, Object> env = new Hashtable<String, Object>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://" + ldapServerHostname + ":" + ldapServerPort + "/ou=People,o=JNDITutorial");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "cn=Directory Manager");
        env.put(Context.SECURITY_CREDENTIALS, "manager");
        return env;
    }


    public static Hashtable<String, Object> prepareAnonymousSignUpEnvironment() {
        return prepareAnonymousSignUpEnvironment(ldapServerHostname, ldapServerPort);
    }
}
