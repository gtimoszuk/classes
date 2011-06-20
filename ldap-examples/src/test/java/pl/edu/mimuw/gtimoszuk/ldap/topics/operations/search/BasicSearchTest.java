package pl.edu.mimuw.gtimoszuk.ldap.topics.operations.search;

import com.google.common.collect.Multimap;
import org.junit.Ignore;
import org.junit.Test;
import pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialDirContext;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.SizeLimitExceededException;
import javax.naming.TimeLimitExceededException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.List;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.*;
import static pl.edu.mimuw.gtimoszuk.ldap.LdapAttributesNames.*;
import static pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities.toMultimap;
import static pl.edu.mimuw.gtimoszuk.ldap.TestIntentionRevealers.oneFor;

/*
http://download.oracle.com/javase/tutorial/jndi/ops/basicsearch.html
 */
public class BasicSearchTest extends AbstractNamingContextTestWithInitialDirContext {

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/Search.java
     */
    @Test
    public void shouldFindAllMatchingEntries() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);

        String searchedSN = tedGeiselSurname;
        Attributes attributesToMatch = NamingUtilities.newCaseInsensitiveBasicAttributes();
        attributesToMatch.put(new BasicAttribute(sn, searchedSN));
        attributesToMatch.put(new BasicAttribute(mail));
        String[] requestedAttributes = {sn, telephoneNumber, attributeNotDefinedForTedGeisel, mail};
        int expectedAttributesCount = requestedAttributes.length - oneFor(attributeNotDefinedForTedGeisel);

        //when
        NamingEnumeration<SearchResult> matchingEntries = getContext().search(peopleOU, attributesToMatch, requestedAttributes);

        //then
        List<SearchResult> results = NamingUtilities.toList(matchingEntries);
        assertThat(results.size(), is(numberOfEntriesWithSnGeisel));
        SearchResult searchResult = getOnlyElement(results);
        Multimap<String,Object> foundEntryAttributes = toMultimap(searchResult.getAttributes());
        assertThat(foundEntryAttributes.keySet().size(), is(expectedAttributesCount));
        assertThat(foundEntryAttributes.containsKey(mail), is(true));
        assertThat(foundEntryAttributes.containsEntry(sn, searchedSN), is(true));
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/SearchRetAll.java
     */
    @Test
    public void shouldReturnAllAttributes() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);

        Attributes attributesToMatch = NamingUtilities.newCaseInsensitiveBasicAttributes();
        attributesToMatch.put(new BasicAttribute(sn, tedGeiselSurname));
        attributesToMatch.put(new BasicAttribute(mail));

        //when
        NamingEnumeration<SearchResult> matchingEntries = getContext().search(peopleOU, attributesToMatch);

        //then
        List<SearchResult> results = NamingUtilities.toList(matchingEntries);
        assertThat(results.size(), is(numberOfEntriesWithSnGeisel));
        Multimap<String,Object> foundEntryAttributes = toMultimap(getOnlyElement(results).getAttributes());
        assertThat(foundEntryAttributes.containsKey(mail), is(true));
        assertThat(foundEntryAttributes.containsEntry(sn, tedGeiselSurname), is(true));
    }
}
