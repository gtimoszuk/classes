package pl.edu.mimuw.gtimoszuk.ldap.topics.operations;

import org.junit.Test;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialContext;
import pl.edu.mimuw.gtimoszuk.ldap.objects.Fruit;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.*;
import static pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities.entryExistsIn;

/*
http://download.oracle.com/javase/tutorial/jndi/ops/lookup.html
*/
public class LookupTest extends AbstractNamingContextTestWithInitialContext {

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/Lookup.java
    */
    @Test
    public void shouldLookupAnEntry() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);

        //when
        LdapContext subcontext = (LdapContext) getContext().lookup(rosannaLeeRelativeName);

        //then
        assertThat(subcontext, is(notNullValue()));
    }

    @Test(expected = NameNotFoundException.class)
    public void shouldThrowExceptionWhenLookupFoundNoEntry() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);

        //when
        getContext().lookup(nonexistentPersonRelativeName);

        //then an exception is thrown
    }

}
