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
http://download.oracle.com/javase/tutorial/jndi/ops/modattrs.html
*/
public class ModifyAttributesTest extends AbstractNamingContextTestWithInitialDirContext {

    @Override
    protected void augumentGivens() throws NamingException {
        Attributes actualValuesOfFixedAttributes =
                getContext().getAttributes(tedGeiselRelativeName, $(mail, telephoneNumber));
        assertThat(actualValuesOfFixedAttributes, is(attributesFixedForTedGeisel()));
    }

    @Override
    protected void cleanUpPossibleFixtureChanges() throws NamingException {
        getContext().modifyAttributes(
                tedGeiselRelativeName, DirContext.REPLACE_ATTRIBUTE, emptyAttributesFor(mail, telephoneNumber)
        );
        getContext().modifyAttributes(tedGeiselRelativeName, DirContext.ADD_ATTRIBUTE, attributesFixedForTedGeisel());
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
        attributes.put(new BasicAttribute(mail, tedGeiselMail));
        attributes.put(new BasicAttribute(telephoneNumber, tedGeiselTelephoneNumber));
        return attributes;
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/ModAttrs.java
     */
    @Test
    public void shouldReplaceMailAttribute() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);
        String newMailValue = "geisel@wizards.com";

        //when
        Attribute newMailAttribute = new BasicAttribute(mail, newMailValue);
        ModificationItem mailModification = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, newMailAttribute);
        getContext().modifyAttributes(tedGeiselRelativeName, $(mailModification));

        //then
        Multimap<String, Object> tedGeiselAttributes = toMultimap(getContext().getAttributes(tedGeiselRelativeName));
        assertThat((String) getOnlyElement(tedGeiselAttributes.get(mail)), is(newMailValue));
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/ModAttrs.java
     */
    @Test
    public void shouldAddAnotherTelephoneAttributeValue() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);

        //when
        Attribute newTelephoneAttribute = new BasicAttribute(telephoneNumber, "+1 555 555 5555");
        ModificationItem telephoneAddition = new ModificationItem(DirContext.ADD_ATTRIBUTE, newTelephoneAttribute);
        getContext().modifyAttributes(tedGeiselRelativeName, $(telephoneAddition));

        //then
        Multimap<String, Object> tedGeiselAttributes = toMultimap(getContext().getAttributes(tedGeiselRelativeName));
        assertThat(tedGeiselAttributes.get(telephoneNumber).size(), is(numberOfTedGeiselTelephoneNumbers + oneFor(telephoneAddition)));
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/ModAttrs.java
     */
    @Test
    public void shouldRemoveTelephoneNumberAttribute() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);

        //when
        Attribute attributeToRemove = new BasicAttribute(telephoneNumber, tedGeiselTelephoneNumber);
        ModificationItem telephoneRemoval = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attributeToRemove);
        getContext().modifyAttributes(tedGeiselRelativeName, $(telephoneRemoval));

        //then
        Multimap<String, Object> tedGeiselAttributes = toMultimap(getContext().getAttributes(tedGeiselRelativeName));
        assertThat(tedGeiselAttributes.keySet().size(), is(tedGeiselAttributesSize - oneFor(telephoneRemoval)));
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/ModAttrs.java
     */
    @Test
    public void shouldPerformMultipleModifications() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);
        ModificationItem telephoneRemoval =
                new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(telephoneNumber));
        ModificationItem mailRemoval = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(mail));
        ModificationItem[] removals = {mailRemoval, telephoneRemoval};

        //when
        getContext().modifyAttributes(tedGeiselRelativeName, removals);

        //then
        Multimap<String, Object> tedGeiselAttributes = toMultimap(getContext().getAttributes(tedGeiselRelativeName));
        assertThat(tedGeiselAttributes.keySet().size(), is(tedGeiselAttributesSize - removals.length));
    }

    @Test
    public void shouldRemoveTelephoneNumberAttributeWhenGivenEmptyAttribute() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);

        //when
        Attributes attributesToRemove = emptyAttributesFor(telephoneNumber);
        getContext().modifyAttributes(tedGeiselRelativeName, DirContext.REMOVE_ATTRIBUTE, attributesToRemove);

        //then
        Multimap<String, Object> tedGeiselAttributes = toMultimap(getContext().getAttributes(tedGeiselRelativeName));
        assertThat(tedGeiselAttributes.keySet().size(), is(tedGeiselAttributesSize - attributesToRemove.size()));
    }

}
