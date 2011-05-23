package pl.edu.mimuw.gtimoszuk.ldap;

import com.sun.jndi.ldap.LdapCtxFactory;

import javax.naming.Context;
import java.util.Hashtable;

import static pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities.AuthenticationType;

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
    public static final String tedGeiselSurname = "Geisel";
    public static final String tedGeiselMail = "Ted.Geisel@JNDITutorial.com";
    public static final int tedGeiselAttributesSize = 7;
    public static final int tedGeiselObjectclassesCount = 4;
    public static final String attributeNotDefinedForTedGeisel = LdapAttributesNames.golfHandicap;
    public static final String tedGeiselTelephoneNumber = "+1 408 555 5252";

    public static final int numberOfTedGeiselTelephoneNumbers = 1;
    public static final int numberOfEntriesWithSnGeisel = 1;
    public static final String JNDITutorialO = "o=JNDITutorial";
    public static final String peopleOU = "ou=People";
    public static final String newHiresOU = "ou=NewHires";

    public static final String someUsersDN = "cn=S. User, ou=NewHires, " + JNDITutorialO;
    public static final String someUsersPassword = "mysecret";

    public static final String directoryManagerDN = "cn=Directory Manager";
    public static final String directoryManagersPassword = "manager";

    public static final String CUserDN = "dn:cn=C. User, ou=NewHires, o=JNDITutorial";
    public static final String CUserPassword = "mysecret";

    public static final String peopleDN = peopleOU + "," + JNDITutorialO;

    public static final String digestMd5Realm = "JNDITutorial";
    public static int numberOfOrganizationalUnitsWithinPeopleSubtree = 1;

    public static Hashtable<String, Object> prepareAnonymousSignUpEnvironment() {
        return prepareAnonymousSignUpEnvironment(ldapServerHostname, ldapServerPort);
    }

    public static Hashtable<String, Object> prepareAnonymousSignUpEnvironment(
            String ldapServerHostname, int ldapServerPort
    ) {
        return prepareAnonymousSignUpEnvironmentAtDN(ldapServerHostname, ldapServerPort, JNDITutorialO);
    }

    private static Hashtable<String, Object> prepareDirectoryManagerSignUpEnvironment(
            String ldapServerHostname, int ldapServerPort
    ) {
        return prepareDirectoryManagerSignUpEnvironmentAtDN(ldapServerHostname, ldapServerPort, JNDITutorialO);
    }

    public static Hashtable<String, Object> prepareDirectoryManagerSignUpEnvironmentAtDN(
            String ldapServerHostname, int ldapServerPort, String distinguishedName
    ) {
        Hashtable<String, Object> env =
                prepareAnonymousSignUpEnvironmentAtDN(ldapServerHostname, ldapServerPort, distinguishedName);
        env.put(Context.SECURITY_AUTHENTICATION, AuthenticationType.simple);
        env.put(Context.SECURITY_PRINCIPAL, directoryManagerDN);
        env.put(Context.SECURITY_CREDENTIALS, directoryManagersPassword);
        return env;
    }

    private static Hashtable<String, Object> prepareAnonymousSignUpEnvironmentAtDN(
            String ldapServerHostname, int ldapServerPort, String distinguishedName
    ) {
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, LdapCtxFactory.class.getCanonicalName());
        env.put(Context.PROVIDER_URL, "ldap://" + ldapServerHostname + ":" + ldapServerPort + "/" + distinguishedName);
        return env;
    }
}
