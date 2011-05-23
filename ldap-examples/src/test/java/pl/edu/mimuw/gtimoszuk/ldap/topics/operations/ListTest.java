package pl.edu.mimuw.gtimoszuk.ldap.topics.operations;

import org.junit.Test;
import pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialContext;
import pl.edu.mimuw.gtimoszuk.ldap.objects.Fruit;

import javax.naming.*;
import javax.naming.ldap.LdapContext;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.*;
import static pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities.entryExistsIn;

/*
http://download.oracle.com/javase/tutorial/jndi/ops/list.html
*/
public class ListTest extends AbstractNamingContextTestWithInitialContext {

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/List.java
    */
    @Test
    public void shouldListAllEntries() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);

        //when
        NamingEnumeration<NameClassPair> enumeration = getContext().list(peopleOU);

        //then
        assertThat(NamingUtilities.toList(enumeration).size(), is(numberOfEntriesUnderPeopleDN));
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/ListBindings.java
    */
    @Test
    public void shouldListAllBindings() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);

        //when
        NamingEnumeration<Binding> bindings = getContext().listBindings(peopleOU);

        //then
        assertThat(NamingUtilities.toList(bindings).size(), is(numberOfEntriesUnderPeopleDN));
    }

}
