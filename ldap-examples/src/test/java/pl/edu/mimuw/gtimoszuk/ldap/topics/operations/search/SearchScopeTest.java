package pl.edu.mimuw.gtimoszuk.ldap.topics.operations.search;

import com.google.common.collect.Multimap;
import org.junit.Ignore;
import org.junit.Test;
import pl.edu.mimuw.gtimoszuk.ldap.Fixture;
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
import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.numberOfOrganizationalUnitsWithinPeopleSubtree;
import static pl.edu.mimuw.gtimoszuk.ldap.LdapAttributesNames.*;
import static pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities.toMultimap;
import static pl.edu.mimuw.gtimoszuk.ldap.TestIntentionRevealers.oneFor;

/*
http://download.oracle.com/javase/tutorial/jndi/ops/scope.html
 */
public class SearchScopeTest extends AbstractNamingContextTestWithInitialDirContext {

    private final String objectClassesOnlyFilter = "(objectclass=organizationalUnit)";

    /*
   http://download.oracle.com/javase/tutorial/jndi/ops/examples/SearchObject.java
    */
    @Test
    public void shouldSearchWithObjectScope() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.OBJECT_SCOPE);

        //when
        NamingEnumeration<SearchResult> matchingEntries =
                getContext().search(peopleOU, objectClassesOnlyFilter, searchControls);

        //then
        List<SearchResult> results = NamingUtilities.toList(matchingEntries);
        assertThat(results.size(), is(oneFor(peopleOU)));
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/SearchSubtree.java
     */
    @Test
    public void shouldSearchWithSubtreeScope() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //when
        NamingEnumeration<SearchResult> matchingEntries =
                getContext().search(peopleOU, objectClassesOnlyFilter, searchControls);

        //then
        List<SearchResult> results = NamingUtilities.toList(matchingEntries);
        assertThat(results.size(), is(oneFor(peopleOU) + numberOfOrganizationalUnitsWithinPeopleSubtree));
    }
}
