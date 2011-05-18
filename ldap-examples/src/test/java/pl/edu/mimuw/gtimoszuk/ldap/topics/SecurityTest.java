package pl.edu.mimuw.gtimoszuk.ldap.topics;

import org.junit.Test;
import pl.edu.mimuw.gtimoszuk.ldap.Fixture;

import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.*;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class SecurityTest {

    //TODO closing the contexts

    @Test(expected = AuthenticationNotSupportedException.class)
    public void shouldNotAllowInvalidAuthType() throws NamingException {
        //given
        Hashtable<String, Object> environment = Fixture.prepareAnonymousSignUpEnvironment();
        environment.put(Context.SECURITY_AUTHENTICATION, "custom");
        environment.put(Context.SECURITY_PRINCIPAL, "cn=S. User, ou=NewHires, o=JNDITutorial");
        environment.put(Context.SECURITY_CREDENTIALS, "mysecret");

        //when
        new InitialDirContext(environment);

        //then an exception is thrown
    }

    @Test(expected = AuthenticationException.class)
    public void shouldNotLoginWithInvalidPassword() throws NamingException {
        //given
        Hashtable<String, Object> environment = Fixture.prepareAnonymousSignUpEnvironment();
        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
		environment.put(Context.SECURITY_PRINCIPAL, "cn=S. User, ou=NewHires, o=JNDITutorial");
		environment.put(Context.SECURITY_CREDENTIALS, "notmysecret");

        //when
        new InitialDirContext(environment);

        //then an exception is thrown
    }

    @Test
    public void shouldAllowReadAccessForAnonymousUser() throws NamingException {
        //given
        Hashtable<String, Object> env = Fixture.prepareAnonymousSignUpEnvironment();
        env.put(Context.SECURITY_AUTHENTICATION, "none");

        //when
        DirContext ctx = new InitialDirContext(env);

        //then
        assertThat(ctx.lookup("ou=NewHires"), is(notNullValue()));
    }

    @Test
    public void shouldAllowReadAccessForUserLoggedInWithSimpleAuthentication() throws NamingException {
        //given
        Hashtable<String, Object> env = Fixture.prepareAnonymousSignUpEnvironment();
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "cn=Directory Manager"); //TODO deduplicate with Fixture
		env.put(Context.SECURITY_CREDENTIALS, "manager");

        //when
        DirContext ctx = new InitialDirContext(env);

        //then
        assertThat(ctx.lookup("ou=NewHires"), is(notNullValue()));
    }

}
