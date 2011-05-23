package pl.edu.mimuw.gtimoszuk.ldap.topics.advanced.security;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialDirContext;
import pl.edu.mimuw.gtimoszuk.ldap.objects.Fruit;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NoPermissionException;
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
http://download.oracle.com/javase/tutorial/jndi/ldap/anonymous.html
*/
public class AnonymousAuthenticationTest extends AbstractNamingContextTestWithInitialDirContext {

    /*
    http://download.oracle.com/javase/tutorial/jndi/ldap/examples/None.java
    */
    @Test
    public void shouldAllowReadAccessForAnonymousUser() throws NamingException {
        //given
        Hashtable<String, Object> environment = prepareAnonymousSignUpEnvironment();
        environment.put(Context.SECURITY_AUTHENTICATION, AuthenticationType.none);

        //when
        createContextUsing(environment);

        //then
        assertThat(getContext().lookup(newHiresOU), is(notNullValue()));
    }

    @Test(expected = NoPermissionException.class)
    public void shouldNotAllowWriteAccessToAnonymousUser() throws NamingException {
        //given
        Fruit fruit = new Fruit("orange");
        createContextUsing(anonymousConnectionParameters);

        //when
        getContext().bind(atFirstUnusedCN, fruit);

        //then an exception is thrown
    }

}
