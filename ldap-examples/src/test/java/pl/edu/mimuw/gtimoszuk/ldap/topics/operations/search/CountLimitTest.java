package pl.edu.mimuw.gtimoszuk.ldap.topics.operations.search;

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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.anonymousConnectionParameters;
import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.peopleOU;

/*
http://download.oracle.com/javase/tutorial/jndi/ops/countlimit.html
 */
public class CountLimitTest extends AbstractNamingContextTestWithInitialDirContext {

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/SearchCountLimit.java
     */
    @Test
    public void shouldReturnResultsUpToLimit() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);
        String surnameStartsWithMFilter = "(sn=*)";
        int maxResults = 1;

        //when
        SearchControls searchControls = new SearchControls();
        searchControls.setCountLimit(maxResults);
        NamingEnumeration<SearchResult> foundEntries =
                getContext().search(peopleOU, surnameStartsWithMFilter, searchControls);

        //then
        List<SearchResult> results = NamingUtilities.toList(foundEntries, maxResults);
        assertThat(results.size() <= maxResults, is(true));
    }

    @Test
    public void willThrowExceptionWhenCallingHasMoreAfterGettingElementsUpToLimit() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);
        String surnameStartsWithMFilter = "(sn=*)";
        int maxResults = 2;
        SearchControls searchControls = new SearchControls();
        searchControls.setCountLimit(maxResults);

        NamingEnumeration<SearchResult> foundEntries =
                getContext().search(peopleOU, surnameStartsWithMFilter, searchControls);
        foundEntries.next();
        foundEntries.next();
        try {
            //when
            foundEntries.hasMore();

            //then an exception is thrown. Otherwise:
            fail("Expected " + SizeLimitExceededException.class.getCanonicalName());
        } catch (SizeLimitExceededException e) {
            //this was expected
        }
    }

    @Test
    public void willThrowExceptionWhenCallingNextAfterGettingElementsUpToLimit() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);
        String surnameStartsWithMFilter = "(sn=*)";
        int maxResults = 2;
        SearchControls searchControls = new SearchControls();
        searchControls.setCountLimit(maxResults);

        NamingEnumeration<SearchResult> foundEntries =
                getContext().search(peopleOU, surnameStartsWithMFilter, searchControls);
        foundEntries.next();
        foundEntries.next();
        try {
            //when
            foundEntries.next();

            //then an exception is thrown. Otherwise:
            fail("Expected " + SizeLimitExceededException.class.getCanonicalName());
        } catch (SizeLimitExceededException e) {
            //this was expected
        }
    }

}
