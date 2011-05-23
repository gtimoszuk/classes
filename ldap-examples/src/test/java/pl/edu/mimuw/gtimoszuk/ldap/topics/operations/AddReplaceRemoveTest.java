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
http://download.oracle.com/javase/tutorial/jndi/ops/bind.html
*/
public class AddReplaceRemoveTest extends AbstractNamingContextTestWithInitialContext {

    private static final String additionalAlreadyUsedFruitCN = "cn=Some already taken fruit CN";

    @Override
    protected void augumentGivens() throws NamingException {
        assertThat(entryExistsIn(getContext(), nonexistentPersonRelativeName), is(false));
        assertThat(entryExistsIn(getContext(), additionalAlreadyUsedFruitCN), is(false));
        getContext().bind(additionalAlreadyUsedFruitCN, new Fruit("some predefined fruit"));
    }

    @Override
    public void cleanUpPossibleFixtureChanges() throws NamingException {
        getContext().unbind(atFirstUnusedCN);
        getContext().unbind(additionalAlreadyUsedFruitCN);
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/Bind.java
    */
    @Test
    public void shouldCreateNewEntry() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);
        String fruitName = "orange";
        Fruit fruit = new Fruit(fruitName);
        String newFruitCN = atFirstUnusedCN;

        //when
        getContext().bind(newFruitCN, fruit);

        //then
        Object fruitObject = getContext().lookup(newFruitCN);
        assertThat(fruitObject, is(instanceOf(Fruit.class)));
        assertThat(fruitObject.toString(), is(fruitName));
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/Rebind.java
    */
    @Test
    public void shouldCreateNewEntryUsingRebind() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);
        String fruitName = "lemon";
        Fruit fruit = new Fruit(fruitName);
        String newFruitCN = atFirstUnusedCN;

        //when
        getContext().rebind(newFruitCN, fruit);

        //then
        Object fruitObject = getContext().lookup(newFruitCN);
        assertThat(fruitObject, instanceOf(Fruit.class));
        assertThat(fruitObject.toString(), is(fruitName));
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/Rebind.java
    */
    @Test
    public void shouldReplaceExistingEntry() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);
        String fruitCN = additionalAlreadyUsedFruitCN;
        String fruitName = "yummy, fresh fruit";

        //when
        getContext().rebind(fruitCN, new Fruit(fruitName));

        //then
        Object fruitObject = getContext().lookup(fruitCN);
        assertThat(fruitObject, instanceOf(Fruit.class));
        assertThat(fruitObject.toString(), is(fruitName));
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/Unbind.java
    */
    @Test
    public void shouldRemoveObjectFromDirectory() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);
        String fruitCN = additionalAlreadyUsedFruitCN;

        //when
        getContext().unbind(fruitCN);

        //then
        assertThat(entryExistsIn(getContext(), fruitCN), is(false));
    }

    @Test
    public void shouldAllowUnbindingNonexistentObjects() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);
        String fruitCN = atFirstUnusedCN;

        //when
        getContext().unbind(fruitCN);

        //then an exception is thrown
    }

}
