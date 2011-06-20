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
http://download.oracle.com/javase/tutorial/jndi/ops/timelimit.html
 */
public class TimeLimitTest extends AbstractNamingContextTestWithInitialDirContext {

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/SearchTimeLimit.java
     */
    @Test
    public void shouldPerformSearchWithinTimeLimit() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        int timeLimitMiliseconds = 1000;
        searchControls.setTimeLimit(timeLimitMiliseconds);

        //when
        getContext().search("", "(objectclass=*)", searchControls);

        //then no exception is thrown
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/SearchTimeLimit.java
     */
    @Ignore("This probably won't pass without ingerence into LDAP connection (by slowing it down) or adding more test data")
    @Test(expected = TimeLimitExceededException.class)
    public void shouldThrowExceptionOnTimeLimitExceeded() throws NamingException {
        //given
        createContextUsing(anonymousConnectionParameters);

        SearchControls searchControls = new SearchControls();
        int timeLimitMiliseconds = 1;
        searchControls.setTimeLimit(timeLimitMiliseconds);
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //when
        getContext().search("", "(objectclass=*)", searchControls);

        //then an exception is thrown
    }
}
