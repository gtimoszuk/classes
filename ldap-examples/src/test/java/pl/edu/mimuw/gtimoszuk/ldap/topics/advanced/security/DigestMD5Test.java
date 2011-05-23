package pl.edu.mimuw.gtimoszuk.ldap.topics.advanced.security;

import org.junit.Test;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialContext;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Hashtable;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.*;

/*
http://download.oracle.com/javase/tutorial/jndi/ldap/digest.html
*/
public class DigestMD5Test extends AbstractNamingContextTestWithInitialContext {

    /*
    http://download.oracle.com/javase/tutorial/jndi/ldap/examples/Digest.java
     */
    @Test
    public void shouldSignInUsingDigestMD5() throws NamingException {
        //given
        Hashtable<String, Object> environment =
                prepareAnonymousSignUpEnvironment(hostNameForDigestSignIn(), ldapServerPort);
        environment.put(Context.SECURITY_AUTHENTICATION, "DIGEST-MD5");
		environment.put(Context.SECURITY_PRINCIPAL, CUserDN);
		environment.put(Context.SECURITY_CREDENTIALS, CUserPassword);

        //when
        createContextUsing(environment);

        //then
        verifySignedIn();
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ldap/examples/DigestRealm.java
     */
    @Test
    public void shouldSignInUsingDigestMD5WithRealmSpecified() throws NamingException {
        //given
        Hashtable<String, Object> environment =
                prepareAnonymousSignUpEnvironment(hostNameForDigestSignIn(), ldapServerPort);
        environment.put(Context.SECURITY_AUTHENTICATION, "DIGEST-MD5");
		environment.put(Context.SECURITY_PRINCIPAL, CUserDN);
		environment.put(Context.SECURITY_CREDENTIALS, CUserPassword);
		environment.put("java.naming.security.sasl.realm", digestMd5Realm);

        //when
        createContextUsing(environment);

        //then
        verifySignedIn();
    }

    private void verifySignedIn() throws NamingException {
        assertThat(getContext().lookup(newHiresOU), is(notNullValue()));
    }

    @Test(expected = AuthenticationException.class)
    public void shouldThrowExceptionWhenRealmDoesNotExist() throws NamingException {
        //given
        Hashtable<String, Object> environment =
                prepareAnonymousSignUpEnvironment(hostNameForDigestSignIn(), ldapServerPort);
        environment.put(Context.SECURITY_AUTHENTICATION, "DIGEST-MD5");
		environment.put(Context.SECURITY_PRINCIPAL, CUserDN);
		environment.put(Context.SECURITY_CREDENTIALS, CUserPassword);
		environment.put("java.naming.security.sasl.realm", "NonexistentRealm");

        //when
        createContextUsing(environment);

        //then an exception is thrown
    }

    private String hostNameForDigestSignIn() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Couldn't obtain localhost's IPv4 address", e);
        }
    }
}
