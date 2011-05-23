package pl.edu.mimuw.gtimoszuk.ldap.topics.advanced.security;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialDirContext;
import pl.edu.mimuw.gtimoszuk.ldap.objects.Fruit;

import javax.naming.*;
import javax.naming.directory.Attributes;
import java.util.Hashtable;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.*;
import static pl.edu.mimuw.gtimoszuk.ldap.LdapAttributesNames.supportedSASLMechanisms;
import static pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities.*;

/*
http://download.oracle.com/javase/tutorial/jndi/ldap/authentication.html
 */
public class ModesOfAuthenticationTest extends AbstractNamingContextTestWithInitialDirContext {

    @Override
    public void cleanUpPossibleFixtureChanges() throws NamingException {
        getContext().unbind(atFirstUnusedCN);
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ldap/examples/Simple.java
     */
    @Test
    public void shouldAllowReadAccessForUserLoggedInWithSimpleAuthentication() throws NamingException {
        //given
        Hashtable<String, Object> environment = prepareAnonymousSignUpEnvironment();
        environment.put(Context.SECURITY_AUTHENTICATION, AuthenticationType.simple);
        environment.put(Context.SECURITY_PRINCIPAL, directoryManagerDN);
        environment.put(Context.SECURITY_CREDENTIALS, directoryManagersPassword);

        //when
        createContextUsing(environment);

        //then
        assertThat(getContext().lookup(newHiresOU), is(notNullValue()));
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ldap/examples/UseDiff.java
     */
    @Test(expected = NoPermissionException.class)
    public void shouldNotAllowWriteAccessAfterChangingContextAuthenticationToAnonymous() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);
        getContext().addToEnvironment(Context.SECURITY_AUTHENTICATION, "none");

        //when
        getContext().bind(atFirstUnusedCN, new Fruit("orange"));

        //then an exception is thrown
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ldap/examples/BadAuth.java
     */
    @Test(expected = AuthenticationNotSupportedException.class)
    public void shouldNotAllowInvalidAuthType() throws NamingException {
        //given
        Hashtable<String, Object> environment = prepareAnonymousSignUpEnvironment();
        environment.put(Context.SECURITY_AUTHENTICATION, "invalid authentication type");
        environment.put(Context.SECURITY_PRINCIPAL, someUsersDN);
        environment.put(Context.SECURITY_CREDENTIALS, someUsersPassword);

        //when
        createContextUsing(environment);

        //then an exception is thrown
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ldap/examples/BadPasswd.java
     */
    @Test(expected = AuthenticationException.class)
    public void shouldNotSignInWithInvalidPassword() throws NamingException {
        //given
        Hashtable<String, Object> environment = prepareAnonymousSignUpEnvironment();
        environment.put(Context.SECURITY_AUTHENTICATION, AuthenticationType.simple);
        environment.put(Context.SECURITY_PRINCIPAL, someUsersDN);
        environment.put(Context.SECURITY_CREDENTIALS, "not" + someUsersPassword);

        //when
        createContextUsing(environment);

        //then an exception is thrown
    }

}
