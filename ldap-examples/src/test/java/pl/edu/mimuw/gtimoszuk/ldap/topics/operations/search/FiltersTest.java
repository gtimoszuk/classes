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

/*
http://download.oracle.com/javase/tutorial/jndi/ops/filter.html
 */
public class FiltersTest extends AbstractNamingContextTestWithInitialDirContext {

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/SearchWithFilter.java
     */
    @Test
    public void shouldSearchUsingFilter() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);
        String[] requestedAttributes = { sn, telephoneNumber, golfHandicap, mail };
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(requestedAttributes);
        String filter = "(&(sn=Geisel)(mail=*))";

        //when
        NamingEnumeration<SearchResult> matchingEntries = getContext().search(peopleOU, filter, searchControls);

        //then
        List<SearchResult> results = NamingUtilities.toList(matchingEntries);
        assertThat(results.size(), is(numberOfEntriesWithSnGeisel));
        Multimap<String,Object> foundEntryAttributes = toMultimap(getOnlyElement(results).getAttributes());
        assertThat(foundEntryAttributes.containsKey(mail), is(true));
        assertThat(foundEntryAttributes.containsEntry(sn, tedGeiselSurname), is(true));
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/SearchWithFilterRetAll.java
     */
    @Test
    public void shouldSearchUsingFilterAndReturnAllAttributes() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);
        String filter = "(&(sn=Geisel)(mail=*))";

        //when
        NamingEnumeration<SearchResult> matchingEntries = getContext().search(peopleOU, filter, new SearchControls());

        //then
        List<SearchResult> results = NamingUtilities.toList(matchingEntries);
        assertThat(results.size(), is(numberOfEntriesWithSnGeisel));
        Multimap<String,Object> foundEntryAttributes = toMultimap(getOnlyElement(results).getAttributes());
        assertThat(foundEntryAttributes.keySet().size(), is(tedGeiselAttributesSize));
        assertThat(foundEntryAttributes.get(objectClass).size(), is(tedGeiselObjectclassesCount));
        assertThat(getOnlyElement(foundEntryAttributes.get(mail)), is((Object) tedGeiselMail));
    }
}
