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
http://download.oracle.com/javase/tutorial/jndi/ops/rename.html
*/
public class RenameTest extends AbstractNamingContextTestWithInitialContext {

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/Rename.java
    */
    @Test
    public void shouldRenameEntry() throws NamingException {
        //given
        createContextUsing(prepareDirectoryManagerSignUpEnvironmentAtDN(ldapServerHostname, ldapServerPort, peopleDN));
        String newCN = "cn=Scott S";

        try {
            //when
            getContext().rename(scottSeligmanCN, newCN);

            //then
            assertThat(getContext().lookup(newCN), is(notNullValue()));
        } finally { //clean up fixture changes
            getContext().rename(newCN, scottSeligmanCN);
        }
    }

}
