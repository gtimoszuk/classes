package pl.edu.mimuw.gtimoszuk.ldap.topics;

import org.junit.Test;
import pl.edu.mimuw.gtimoszuk.ldap.Fixture;
import pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities;
import pl.edu.mimuw.gtimoszuk.ldap.TestIntentionRevealers;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialContext;
import pl.edu.mimuw.gtimoszuk.ldap.objects.Fruit;

import javax.naming.*;
import javax.naming.ldap.LdapContext;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class OperationsTest extends AbstractNamingContextTestWithInitialContext {

    private static final String additionalAlreadyUsedFruitCN = "cn=Some already taken fruit CN";

    @Override
    protected void augumentGivens() throws NamingException {
        assertThat(TestIntentionRevealers.entryExistsIn(getContext(), Fixture.nonexistentPersonRelativeName), is(false));
        assertThat(TestIntentionRevealers.entryExistsIn(getContext(), additionalAlreadyUsedFruitCN), is(false));
        getContext().bind(additionalAlreadyUsedFruitCN, new Fruit("some predefined fruit"));
    }

    @Override
    public void cleanUpPossibleFixtureChanges() throws NamingException {
        getContext().unbind(Fixture.atFirstUnusedCN);
        getContext().unbind(additionalAlreadyUsedFruitCN);
    }

    public static final String peopleOU = "ou=People";

    /**
     * http://download.oracle.com/javase/tutorial/jndi/ops/lookup.html
     * http://download.oracle.com/javase/tutorial/jndi/ops/examples/Lookup.java
     */
    @Test
    public void shouldLookupAnEntry() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);

        //when
        LdapContext subcontext = (LdapContext) getContext().lookup(Fixture.rosannaLeeRelativeName);

        //then
        assertThat(subcontext, is(notNullValue()));
    }


    /**
     *  http://download.oracle.com/javase/tutorial/jndi/ops/lookup.html
     */
    @Test(expected = NameNotFoundException.class)
    public void shouldThrowExceptionWhenLookupFoundNoEntry() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);

        //when
        getContext().lookup(Fixture.nonexistentPersonRelativeName);

        //then an exception is thrown
    }

    /**
     *  http://download.oracle.com/javase/tutorial/jndi/ops/list.html
     *  http://download.oracle.com/javase/tutorial/jndi/ops/examples/List.java
     */
    @Test
    public void shouldListAllEntries() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);

        //when
        NamingEnumeration<NameClassPair> enumeration = getContext().list(peopleOU);

        //then
        assertThat(NamingUtilities.toList(enumeration).size(), is(Fixture.numberOfEntriesUnderPeopleDN));
    }

    /**
     *  http://download.oracle.com/javase/tutorial/jndi/ops/list.html
     *  http://download.oracle.com/javase/tutorial/jndi/ops/examples/ListBindings.java
     */
    @Test
    public void shouldListAllBindings() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);

        //when
        NamingEnumeration<Binding> bindings = getContext().listBindings("ou=People");

        //then
        assertThat(NamingUtilities.toList(bindings).size(), is(Fixture.numberOfEntriesUnderPeopleDN));
    }

    //TODO terminology: bind vs signUp vs create initial context; add vs bind (both latter prefered?)

    /**
     *  http://download.oracle.com/javase/tutorial/jndi/ops/list.html
     */
    @Test(expected = NoPermissionException.class)
    public void shouldNotAllowWriteAccessToAnonymousUser() throws NamingException {
        //given
        Fruit fruit = new Fruit("orange");
        createContextUsing(Fixture.anonymousConnectionParameters);

        //when
        getContext().bind(Fixture.atFirstUnusedCN, fruit);

        //then an exception is thrown
    }

    @Test(expected = NoPermissionException.class)
    public void shouldNotAllowWriteAccessAfterChangingContextAuthenticationToAnonymous() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);

        getContext().addToEnvironment(Context.SECURITY_AUTHENTICATION, "none");

        //then
        getContext().bind(Fixture.atFirstUnusedCN, new Fruit("orange"));
    }

    /**
     *  http://download.oracle.com/javase/tutorial/jndi/ops/list.html
     *  http://download.oracle.com/javase/tutorial/jndi/ops/examples/Bind.java
     */
    @Test
    public void shouldCreateNewEntry() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);
        String fruitName = "orange";
        Fruit fruit = new Fruit(fruitName);
        String newFruitCN = Fixture.atFirstUnusedCN;

        //when
        getContext().bind(newFruitCN, fruit);

        //then
        Object fruitObject = getContext().lookup(newFruitCN);
        assertThat(fruitObject, instanceOf(Fruit.class));
        assertThat(fruitObject.toString(), is(fruitName));
    }

    /**
     *  http://download.oracle.com/javase/tutorial/jndi/ops/list.html
     *  http://download.oracle.com/javase/tutorial/jndi/ops/examples/Rebind.java
     */
    @Test
    public void shouldCreateNewEntryUsingRebind() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);
        String fruitName = "lemon";
        Fruit fruit = new Fruit(fruitName);
        String newFruitCN = Fixture.atFirstUnusedCN;

        //when
        getContext().rebind(newFruitCN, fruit);

        //then
        Object fruitObject = getContext().lookup(newFruitCN);
        assertThat(fruitObject, instanceOf(Fruit.class));
        assertThat(fruitObject.toString(), is(fruitName));
    }

    /**
     *  http://download.oracle.com/javase/tutorial/jndi/ops/list.html
     *  http://download.oracle.com/javase/tutorial/jndi/ops/examples/Rebind.java
     */
    @Test
    public void shouldReplaceExistingEntry() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);
        String fruitCN = additionalAlreadyUsedFruitCN;
        String fruitName = "yummy, fresh fruit";

        //when
        getContext().rebind(fruitCN, new Fruit(fruitName));

        //then
        Object fruitObject = getContext().lookup(fruitCN);
        assertThat(fruitObject, instanceOf(Fruit.class));
        assertThat(fruitObject.toString(), is(fruitName));
    }

    /**
     *  http://download.oracle.com/javase/tutorial/jndi/ops/list.html
     */
    @Test
    public void shouldAllowUnbindingNonexistentObjects() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);
        String fruitCN = Fixture.atFirstUnusedCN;

        //when
        getContext().unbind(fruitCN);

        //then an exception is thrown
    }

    /**
     *  http://download.oracle.com/javase/tutorial/jndi/ops/list.html
     *  http://download.oracle.com/javase/tutorial/jndi/ops/examples/Unbind.java
     */
    @Test
    public void shouldDeleteObjectFromDirectory() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);
        String fruitCN = additionalAlreadyUsedFruitCN;

        //when
        getContext().unbind(fruitCN);

        //then
        assertThat(TestIntentionRevealers.entryExistsIn(getContext(), fruitCN), is(false));
    }

    @Test
    public void shouldRenameEntry() throws NamingException {
        //given
        createContextUsing(Fixture.prepareDirectoryManagerSignUpEnvironmentAtPeopleDN(Fixture.ldapServerHostname, Fixture.ldapServerPort));
        String newCN = "cn=Scott S";

        try {
            //when
            getContext().rename(Fixture.scottSeligmanCN, newCN);

            //then
            assertThat(getContext().lookup(newCN), is(notNullValue()));
        } finally {
            getContext().rename(newCN, Fixture.scottSeligmanCN);
        }
    }


}
