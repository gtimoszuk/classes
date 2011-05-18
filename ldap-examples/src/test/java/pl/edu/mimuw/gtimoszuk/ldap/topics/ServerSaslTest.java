package pl.edu.mimuw.gtimoszuk.ldap.topics;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;
import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.*;

import pl.edu.mimuw.gtimoszuk.ldap.Fixture;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialContext;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialDirContext;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities.toMultimap;

public class ServerSaslTest extends AbstractNamingContextTestWithInitialDirContext {

    @Test
    public void shouldReturnAvailableSaslMechanisms() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);
        Set<Object> saslMechanismsAvailableByDefaultInOpenDS = Sets.<Object>newHashSet("PLAIN", "EXTERNAL", "DIGEST-MD5", "CRAM-MD5");

        //when
        Attributes attributes = getContext().getAttributes("ldap://localhost:1389", new String[]{"supportedSASLMechanisms"});

        //then
        Set<Object> supportedSASLMechanisms = newHashSet(toMultimap(attributes).get("supportedSASLMechanisms"));
        assertThat(supportedSASLMechanisms, is(saslMechanismsAvailableByDefaultInOpenDS));
    }
}