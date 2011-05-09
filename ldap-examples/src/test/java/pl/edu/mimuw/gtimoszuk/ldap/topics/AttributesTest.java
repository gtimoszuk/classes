package pl.edu.mimuw.gtimoszuk.ldap.topics;

import com.google.common.collect.Multimap;
import org.junit.Test;
import pl.edu.mimuw.gtimoszuk.ldap.Fixture;
import pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities;
import pl.edu.mimuw.gtimoszuk.ldap.TestIntentionRevealers;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialDirContext;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import static pl.edu.mimuw.gtimoszuk.ldap.LdapAttributesNames.*;
import static com.google.common.collect.Iterables.getOnlyElement;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AttributesTest extends AbstractNamingContextTestWithInitialDirContext {

    @Test
    public void shouldGetAllAttributes() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);

        //when
        Attributes attributes = getContext().getAttributes(Fixture.tedGeiselRelativeName);

        //then
        Multimap<String, Object> attributesMultimap = NamingUtilities.toMultimap(attributes);
        assertThat(attributesMultimap.keySet().size(), is(Fixture.tedGeiselAttributesSize));
        assertThat(attributesMultimap.get(objectClass).size(), is(Fixture.tedGeiselObjectclassesCount));
        assertThat(getOnlyElement(attributesMultimap.get(mail)), is((Object) Fixture.tedGeiselMail));
    }

    @Test
    public void shouldGetSelectedAttributes() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);
        String[] requestedAttributes = {sn, telephoneNumber, Fixture.attributeNotDefinedForTedGeisel, mail};
        int expectedAttributesCount = requestedAttributes.length - TestIntentionRevealers.oneFor(Fixture.attributeNotDefinedForTedGeisel);

        //when
        Attributes answer = getContext().getAttributes(Fixture.tedGeiselRelativeName, requestedAttributes);

        //then
        Multimap<String, Object> attributesMultimap = NamingUtilities.toMultimap(answer);
        assertThat(attributesMultimap.keySet().size(), is(expectedAttributesCount));
        assertThat(getOnlyElement(attributesMultimap.get(mail)), is((Object) Fixture.tedGeiselMail));
    }

    //TODO urls to tests

}
