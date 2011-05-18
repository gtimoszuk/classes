package pl.edu.mimuw.gtimoszuk.ldap.topics;

import com.google.common.collect.Multimap;
import org.junit.Ignore;
import org.junit.Test;
import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.*;

import pl.edu.mimuw.gtimoszuk.ldap.Fixture;
import pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialDirContext;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.SizeLimitExceededException;
import javax.naming.TimeLimitExceededException;
import javax.naming.directory.*;
import java.util.List;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities.toMultimap;
import static pl.edu.mimuw.gtimoszuk.ldap.LdapAttributesNames.*;
import static pl.edu.mimuw.gtimoszuk.ldap.TestIntentionRevealers.oneFor;

public class SearchTest extends AbstractNamingContextTestWithInitialDirContext {

    //TODO too long
    @Test
    public void shouldFindAllMatchingEntries() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);

        String searchedSN = "Geisel";
        String[] requestedAttributes = {sn, telephoneNumber, Fixture.attributeNotDefinedForTedGeisel, mail};
        int expectedAttributesCount = requestedAttributes.length - oneFor(Fixture.attributeNotDefinedForTedGeisel);
        Attributes matchAttrs = NamingUtilities.newCaseInsensitiveBasicAttributes();
        matchAttrs.put(new BasicAttribute(sn, searchedSN));
        matchAttrs.put(new BasicAttribute(mail));

        //when
        NamingEnumeration<SearchResult> matchingEntries = getContext().search("ou=People", matchAttrs, requestedAttributes);

        //then
        List<SearchResult> results = NamingUtilities.toList(matchingEntries);
        assertThat(results.size(), is(Fixture.numberOfEntriesWithSnGeisel));
        Multimap<String,Object> foundEntryAttributes = toMultimap(getOnlyElement(results).getAttributes());
        assertThat(foundEntryAttributes.keySet().size(), is(expectedAttributesCount));
        assertThat(foundEntryAttributes.containsKey(mail), is(true));
        assertThat(foundEntryAttributes.containsEntry(sn, searchedSN), is(true));
    }

    @Test
    public void shouldReturnResultsUpToLimit() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);
        String surnameStartsWithMFilter = "(sn=*)";
        int maxResults = 1;
        SearchControls searchControls = new SearchControls();
        searchControls.setCountLimit(maxResults);

        //when
        NamingEnumeration<SearchResult> foundEntries =
                getContext().search(Fixture.peopleOU, surnameStartsWithMFilter, searchControls);

        //then
        List<SearchResult> results = NamingUtilities.toList(foundEntries, maxResults);
        assertThat(results.size() <= maxResults, is(true));
    }

    @Test
    public void willThrowExceptionWhenCallingHasMoreAfterGettingElementsUpToLimit() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);
        String surnameStartsWithMFilter = "(sn=*)";
        int maxResults = 2;
        SearchControls searchControls = new SearchControls();
        searchControls.setCountLimit(maxResults);

        NamingEnumeration<SearchResult> foundEntries =
                getContext().search(Fixture.peopleOU, surnameStartsWithMFilter, searchControls);
        foundEntries.next();
        foundEntries.next();
        try {
            //when
            foundEntries.hasMore();

            //then an exception is thrown. Otherwise:
            fail("Expected " + SizeLimitExceededException.class.getSimpleName());
        } catch (SizeLimitExceededException e) {
            //this was expected
        }
    }

    @Test
    public void willThrowExceptionWhenCallingNextAfterGettingElementsUpToLimit() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);
        String surnameStartsWithMFilter = "(sn=*)";
        int maxResults = 2;
        SearchControls searchControls = new SearchControls();
        searchControls.setCountLimit(maxResults);

        NamingEnumeration<SearchResult> foundEntries =
                getContext().search(Fixture.peopleOU, surnameStartsWithMFilter, searchControls);
        foundEntries.next();
        foundEntries.next();
        try {
            //when
            foundEntries.next();

            //then an exception is thrown. Otherwise:
            fail("Expected " + SizeLimitExceededException.class.getSimpleName());
        } catch (SizeLimitExceededException e) {
            //this was expected
        }
    }

    @Test
    public void shouldSearchWithObjectScope() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);
        String[] attrIDs = { "sn", "telephonenumber", "golfhandicap", "mail" };
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(attrIDs);
        searchControls.setSearchScope(SearchControls.OBJECT_SCOPE);
        String filter = "(&(sn=Geisel)(mail=*))";

        //when
        NamingEnumeration<SearchResult> matchingEntries = getContext().search("cn=Ted Geisel, ou=People", filter, searchControls);

        //then
        List<SearchResult> results = NamingUtilities.toList(matchingEntries);
        assertThat(results.size(), is(Fixture.numberOfEntriesWithSnGeisel));
        Multimap<String,Object> foundEntryAttributes = toMultimap(getOnlyElement(results).getAttributes());
        assertThat(foundEntryAttributes.containsKey(mail), is(true));
        assertThat(foundEntryAttributes.containsEntry(sn, "Geisel"), is(true));
    }

    @Test
    public void shouldSearchWithSubtreeScope() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);
        String[] attrIDs = { "sn", "telephonenumber", "golfhandicap", "mail" };
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(attrIDs);
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String filter = "(&(sn=Geisel)(mail=*))";

        //when
        NamingEnumeration<SearchResult> matchingEntries = getContext().search("", filter, searchControls);

        //then
        List<SearchResult> results = NamingUtilities.toList(matchingEntries);
        assertThat(results.size(), is(Fixture.numberOfEntriesWithSnGeisel));
        Multimap<String,Object> foundEntryAttributes = toMultimap(getOnlyElement(results).getAttributes());
        assertThat(foundEntryAttributes.containsKey(mail), is(true));
        assertThat(foundEntryAttributes.containsEntry(sn, "Geisel"), is(true));
    }

    @Test
    public void shouldSearchUsingFilter() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);
        String[] attrIDs = { "sn", "telephonenumber", "golfhandicap", "mail" };
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(attrIDs);
        String filter = "(&(sn=Geisel)(mail=*))";

        //when
        NamingEnumeration<SearchResult> matchingEntries = getContext().search("ou=People", filter, searchControls);

        //then
        List<SearchResult> results = NamingUtilities.toList(matchingEntries);
        assertThat(results.size(), is(Fixture.numberOfEntriesWithSnGeisel));
        Multimap<String,Object> foundEntryAttributes = toMultimap(getOnlyElement(results).getAttributes());
        assertThat(foundEntryAttributes.containsKey(mail), is(true));
        assertThat(foundEntryAttributes.containsEntry(sn, "Geisel"), is(true));
    }

    @Test
    public void shouldSearchUsingFilterAndReturnAllAttributes() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);
        String filter = "(&(sn=Geisel)(mail=*))";

        //when
        NamingEnumeration<SearchResult> matchingEntries = getContext().search("ou=People", filter, new SearchControls());

        //then
        List<SearchResult> results = NamingUtilities.toList(matchingEntries);
        assertThat(results.size(), is(Fixture.numberOfEntriesWithSnGeisel));
        Multimap<String,Object> foundEntryAttributes = toMultimap(getOnlyElement(results).getAttributes());
        assertThat(foundEntryAttributes.keySet().size(), is(Fixture.tedGeiselAttributesSize));
        assertThat(foundEntryAttributes.get(objectClass).size(), is(Fixture.tedGeiselObjectclassesCount));
        assertThat(getOnlyElement(foundEntryAttributes.get(mail)), is((Object) Fixture.tedGeiselMail));
    }

    @Test
    public void shouldReturnAllAttributes() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);

        Attributes matchAttrs = new BasicAttributes(true); // ignore case
        matchAttrs.put(new BasicAttribute("sn", "Geisel"));
        matchAttrs.put(new BasicAttribute("mail"));

        //when
        NamingEnumeration<SearchResult> matchingEntries = getContext().search("ou=People", matchAttrs);

        //then
        List<SearchResult> results = NamingUtilities.toList(matchingEntries);
        assertThat(results.size(), is(Fixture.numberOfEntriesWithSnGeisel));
        Multimap<String,Object> foundEntryAttributes = toMultimap(getOnlyElement(results).getAttributes());
        assertThat(foundEntryAttributes.containsKey(mail), is(true));
        assertThat(foundEntryAttributes.containsEntry(sn, "Geisel"), is(true));
    }

    @Test
    public void shouldPerformSearchWithinTimeLimit() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);

        SearchControls ctls = new SearchControls();
        ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        int timeLimitMiliseconds = 1000;
        ctls.setTimeLimit(timeLimitMiliseconds);

        //when
        getContext().search("", "(objectclass=*)", ctls);

        //then no exception is thrown
    }

    @Ignore("This probably won't pass without ingerence into LDAP connection (by slowing it down) or adding more test data")
    @Test(expected = TimeLimitExceededException.class)
    public void shouldThrowExceptionOnTimeLimitExceeded() throws NamingException {
        //given
        createContextUsing(Fixture.anonymousConnectionParameters);

        SearchControls ctls = new SearchControls();
        int timeLimitMiliseconds = 1;
        ctls.setTimeLimit(timeLimitMiliseconds);
        ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //when
        getContext().search("", "(objectclass=*)", ctls);

        //then an exception is thrown
    }
}
