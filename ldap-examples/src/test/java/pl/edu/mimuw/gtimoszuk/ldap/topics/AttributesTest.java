package pl.edu.mimuw.gtimoszuk.ldap.topics;

import com.google.common.collect.Multimap;
import org.junit.Test;
import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.*;

import pl.edu.mimuw.gtimoszuk.ldap.Fixture;
import pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialDirContext;

import javax.naming.NamingException;
import javax.naming.directory.*;

import static pl.edu.mimuw.gtimoszuk.ldap.LdapAttributesNames.*;
import static com.google.common.collect.Iterables.getOnlyElement;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static pl.edu.mimuw.gtimoszuk.ldap.LdapAttributesNames.mail;
import static pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities.toMultimap;
import static pl.edu.mimuw.gtimoszuk.ldap.TestIntentionRevealers.oneFor;

public class AttributesTest extends AbstractNamingContextTestWithInitialDirContext {

    @Override
    protected void augumentGivens() throws NamingException {
        Attributes actualValuesOfFixedAttributes =
                getContext().getAttributes(Fixture.tedGeiselRelativeName, restrictedTo(mail, telephoneNumber));
        assertThat(actualValuesOfFixedAttributes, is(attributesFixedForTedGeisel()));
    }

    @Override
    protected void cleanUpPossibleFixtureChanges() throws NamingException {
        getContext().modifyAttributes(Fixture.tedGeiselRelativeName, DirContext.REPLACE_ATTRIBUTE, emptyAttributesFor(mail, telephoneNumber));
        getContext().modifyAttributes(Fixture.tedGeiselRelativeName, DirContext.ADD_ATTRIBUTE, attributesFixedForTedGeisel());
    }

    private Attributes emptyAttributesFor(String... attributeNames) {
        Attributes attributes = NamingUtilities.newCaseInsensitiveBasicAttributes();
        for (String attributeName : attributeNames) {
            attributes.put(new BasicAttribute(attributeName));
        }
        return attributes;
    }

    private Attributes attributesFixedForTedGeisel() {
        Attributes attributes = NamingUtilities.newCaseInsensitiveBasicAttributes();
        attributes.put(new BasicAttribute(mail, Fixture.tedGeiselMail));
        attributes.put(new BasicAttribute(telephoneNumber, Fixture.tedGeiselTelephoneNumber));
        return attributes;
    }

    @Test
    public void shouldGetAllAttributes() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);

        //when
        Attributes attributes = getContext().getAttributes(Fixture.tedGeiselRelativeName);

        //then
        Multimap<String, Object> attributesMultimap = toMultimap(attributes);
        assertThat(attributesMultimap.keySet().size(), is(Fixture.tedGeiselAttributesSize));
        assertThat(attributesMultimap.get(objectClass).size(), is(Fixture.tedGeiselObjectclassesCount));
        assertThat(getOnlyElement(attributesMultimap.get(mail)), is((Object) Fixture.tedGeiselMail));
    }

    @Test
    public void shouldGetSelectedAttributes() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);
        String[] requestedAttributes = {sn, telephoneNumber, Fixture.attributeNotDefinedForTedGeisel, mail};
        int expectedAttributesCount = requestedAttributes.length - oneFor(Fixture.attributeNotDefinedForTedGeisel);

        //when
        Attributes answer = getContext().getAttributes(Fixture.tedGeiselRelativeName, requestedAttributes);

        //then
        Multimap<String, Object> attributesMultimap = toMultimap(answer);
        assertThat(attributesMultimap.keySet().size(), is(expectedAttributesCount));
        assertThat(getOnlyElement(attributesMultimap.get(mail)), is((Object) Fixture.tedGeiselMail));
    }

    //TODO urls to tests

    @Test
    public void shouldReplaceMailAttribute() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);
        String newMailValue = "geisel@wizards.com";

        //when
        Attribute newMailAttribute = new BasicAttribute(mail, newMailValue);
        ModificationItem mailModification = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, newMailAttribute);
        getContext().modifyAttributes(Fixture.tedGeiselRelativeName, usingModifications(mailModification));

        //then
        Multimap<String, Object> tedGeiselAttributes = toMultimap(getContext().getAttributes(Fixture.tedGeiselRelativeName));
        assertThat((String) getOnlyElement(tedGeiselAttributes.get(mail)), is(newMailValue));
    }

    //TODO search all static and move to proper place
    private static ModificationItem[] usingModifications(ModificationItem... modifications) {
        return modifications;
    }

    @Test
    public void shouldAddAnotherTelephoneAttributeValue() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);

        //when
        Attribute newTelephoneAttribute = new BasicAttribute(telephoneNumber, "+1 555 555 5555");
        ModificationItem telephoneModification = new ModificationItem(DirContext.ADD_ATTRIBUTE, newTelephoneAttribute);
        getContext().modifyAttributes(Fixture.tedGeiselRelativeName, usingModifications(telephoneModification));

        //then
        Multimap<String, Object> tedGeiselAttributes = toMultimap(getContext().getAttributes(Fixture.tedGeiselRelativeName));
        assertThat(tedGeiselAttributes.get(telephoneNumber).size(), is(Fixture.numberOfTedGeiselTelephoneNumbers + 1));
    }

    @Test
    public void shouldRemoveTelephoneNumberAttribute() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);

        //when
        Attribute attributeToRemove = new BasicAttribute(telephoneNumber, Fixture.tedGeiselTelephoneNumber);
        ModificationItem telephoneModification = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attributeToRemove);
        getContext().modifyAttributes(Fixture.tedGeiselRelativeName, usingModifications(telephoneModification));

        //then
        Multimap<String, Object> tedGeiselAttributes = toMultimap(getContext().getAttributes(Fixture.tedGeiselRelativeName));
        assertThat(tedGeiselAttributes.keySet().size(), is(Fixture.tedGeiselAttributesSize - 1));
    }

    @Test
    public void shouldPerformMultipleModifications() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);
        ModificationItem telephoneRemoval = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(telephoneNumber));
        ModificationItem mailRemoval = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(mail));
        ModificationItem[] removals = {mailRemoval, telephoneRemoval};

        //when
        getContext().modifyAttributes(Fixture.tedGeiselRelativeName, removals);

        //then
        Multimap<String, Object> tedGeiselAttributes = toMultimap(getContext().getAttributes(Fixture.tedGeiselRelativeName));
        assertThat(tedGeiselAttributes.keySet().size(), is(Fixture.tedGeiselAttributesSize - removals.length));
    }

    @Test
    public void shouldRemoveTelephoneNumberAttributeWhenGivenEmptyAttribute() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);

        //when
        Attributes attributesToRemove = emptyAttributesFor(telephoneNumber);
        getContext().modifyAttributes(Fixture.tedGeiselRelativeName, DirContext.REMOVE_ATTRIBUTE, attributesToRemove);

        //then
        Multimap<String, Object> tedGeiselAttributes = toMultimap(getContext().getAttributes(Fixture.tedGeiselRelativeName));
        assertThat(tedGeiselAttributes.keySet().size(), is(Fixture.tedGeiselAttributesSize - 1));
    }

    private String[] restrictedTo(String... attributeNames) {
        return attributeNames;
    }

}
