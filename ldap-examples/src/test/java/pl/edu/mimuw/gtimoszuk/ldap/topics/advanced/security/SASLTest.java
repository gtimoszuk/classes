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
http://download.oracle.com/javase/tutorial/jndi/ldap/sasl.html
 */
public class SASLTest extends AbstractNamingContextTestWithInitialDirContext {

     /*
    http://download.oracle.com/javase/tutorial/jndi/ldap/examples/ServerSasl.java
     */
    @Test
    public void shouldReturnAvailableSaslMechanisms() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);
        Set<Object> saslMechanismsAvailableByDefaultInOpenDS =
                newHashSet($(Object.class, "PLAIN", "EXTERNAL", "DIGEST-MD5", "CRAM-MD5"));
        String ldapServerUrl = "ldap://" + ldapServerHostname + ":" + ldapServerPort;

        //when
        Attributes attributes = getContext().getAttributes(ldapServerUrl, $(supportedSASLMechanisms));

        //then
        Set<Object> actualSupportedSASLMechanisms = newHashSet(toMultimap(attributes).get(supportedSASLMechanisms));
        assertThat(actualSupportedSASLMechanisms, CoreMatchers.is(saslMechanismsAvailableByDefaultInOpenDS));
    }

}
