package pl.edu.mimuw.gtimoszuk.ldap.topics;

import org.junit.Test;
import pl.edu.mimuw.gtimoszuk.ldap.Fixture;

import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.*;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class DigestMD5Test {

    @Test
    public void shouldLogInUsingDigestMD5() throws NamingException {
        //given
        Hashtable<String, Object> env = Fixture.prepareAnonymousSignUpEnvironment();
        env.put(Context.SECURITY_AUTHENTICATION, "DIGEST-MD5");
		env.put(Context.SECURITY_PRINCIPAL, "dn:cn=C. User, ou=NewHires, o=JNDITutorial");
		env.put(Context.SECURITY_CREDENTIALS, "mysecret");

        //when
        DirContext ctx = new InitialDirContext(env);

        //then
        assertThat(ctx.lookup("ou=NewHires"), is(notNullValue()));
    }

    @Test
    public void shouldLogInUsingDigestMD5WithRealmSpecified() throws NamingException {
        //given
        Hashtable<String, Object> env = Fixture.prepareAnonymousSignUpEnvironment();
        env.put(Context.SECURITY_AUTHENTICATION, "DIGEST-MD5");
		env.put(Context.SECURITY_PRINCIPAL, "dn:cn=C. User, ou=NewHires, o=JNDITutorial");
		env.put(Context.SECURITY_CREDENTIALS, "mysecret");
		env.put("java.naming.security.sasl.realm", "JNDITutorial");

        //when
        DirContext ctx = new InitialDirContext(env);

        //then
        assertThat(ctx.lookup("ou=NewHires"), is(notNullValue()));
    }

    @Test(expected = AuthenticationException.class)
    public void shouldThrowExceptionWhenRealmDoesNotExist() throws NamingException {
        //given
        Hashtable<String, Object> env = Fixture.prepareAnonymousSignUpEnvironment();
        env.put(Context.SECURITY_AUTHENTICATION, "DIGEST-MD5");
		env.put(Context.SECURITY_PRINCIPAL, "dn:cn=C. User, ou=NewHires, o=JNDITutorial");
		env.put(Context.SECURITY_CREDENTIALS, "mysecret");
		env.put("java.naming.security.sasl.realm", "NonexistentRealm");

        //when
        new InitialDirContext(env);

        //then an exception is thrown
    }
}
