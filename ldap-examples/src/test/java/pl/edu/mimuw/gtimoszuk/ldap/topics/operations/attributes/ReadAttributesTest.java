package pl.edu.mimuw.gtimoszuk.ldap.topics.operations.attributes;

import com.google.common.collect.Multimap;
import org.junit.Test;
import pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialDirContext;

import javax.naming.NamingException;
import javax.naming.directory.*;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.*;
import static pl.edu.mimuw.gtimoszuk.ldap.LdapAttributesNames.*;
import static pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities.$;
import static pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities.toMultimap;
import static pl.edu.mimuw.gtimoszuk.ldap.TestIntentionRevealers.oneFor;

/*
http://download.oracle.com/javase/tutorial/jndi/ops/getattrs.html
*/
public class ReadAttributesTest extends AbstractNamingContextTestWithInitialDirContext {

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/GetAllAttrs.java
    */
    @Test
    public void shouldGetAllAttributes() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);

        //when
        Attributes attributes = getContext().getAttributes(tedGeiselRelativeName);

        //then
        Multimap<String, Object> attributesMultimap = toMultimap(attributes);
        assertThat(attributesMultimap.keySet().size(), is(tedGeiselAttributesSize));
        assertThat(attributesMultimap.get(objectClass).size(), is(tedGeiselObjectclassesCount));
        assertThat(getOnlyElement(attributesMultimap.get(mail)), is((Object) tedGeiselMail));
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/GetAttrs.java
    */
    @Test
    public void shouldGetSelectedAttributes() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);
        String[] requestedAttributes = {sn, telephoneNumber, attributeNotDefinedForTedGeisel, mail};
        int expectedAttributesCount = requestedAttributes.length - oneFor(attributeNotDefinedForTedGeisel);

        //when
        Attributes answer = getContext().getAttributes(tedGeiselRelativeName, requestedAttributes);

        //then
        Multimap<String, Object> attributesMultimap = toMultimap(answer);
        assertThat(attributesMultimap.keySet().size(), is(expectedAttributesCount));
        assertThat(getOnlyElement(attributesMultimap.get(mail)), is((Object) tedGeiselMail));
    }

}
