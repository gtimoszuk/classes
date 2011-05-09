package pl.edu.mimuw.gtimoszuk.ldap.topics;

import org.junit.Test;
import pl.edu.mimuw.gtimoszuk.ldap.Fixture;
import pl.edu.mimuw.gtimoszuk.ldap.TestIntentionRevealers;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialDirContext;

import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities.newCaseInsensitiveBasicAttributes;

public class SubcontextTest extends AbstractNamingContextTestWithInitialDirContext {

    private String additionalSubcontextOU = "ou=Some additional subcontext OU";

    @Override
    protected void augumentGivens() throws NamingException {
        assertThat(TestIntentionRevealers.entryExistsIn(getContext(), Fixture.atFirstUnusedOU), is(false));
        assertThat(TestIntentionRevealers.entryExistsIn(getContext(), additionalSubcontextOU), is(false));
        getContext().createSubcontext(additionalSubcontextOU, exampleSubcontextContextAttributes());
    }

    @Override
    protected void cleanUpPossibleFixtureChanges() throws NamingException {
        getContext().destroySubcontext(Fixture.atFirstUnusedOU);
        getContext().destroySubcontext(additionalSubcontextOU);
    }

    @Test
    public void shouldCreateSubcontext() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);
        String newContextOU = Fixture.atFirstUnusedOU;

        //when
        DirContext newContext = getContext().createSubcontext(newContextOU, exampleSubcontextContextAttributes());

        //then
        assertThat(newContext.getNameInNamespace(), is(newContextOU + "," + getContext().getNameInNamespace()));
        NamingEnumeration<NameClassPair> subcontexts = getContext().list("");
        assertThat(containsContextWithName(newContextOU, subcontexts), is(true));
    }

    private boolean containsContextWithName(String name, NamingEnumeration<NameClassPair> contexts) throws NamingException {
        while (contexts.hasMore()) {
            NameClassPair context = contexts.next();
            if (context.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void shouldDestroySubcontext() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);
        String contextToDestroyOU = additionalSubcontextOU;

        //when
        getContext().destroySubcontext(contextToDestroyOU);

        //then
        assertThat(TestIntentionRevealers.entryExistsIn(getContext(), contextToDestroyOU), is(false));
    }

    private Attributes exampleSubcontextContextAttributes() {
        Attributes attrs = newCaseInsensitiveBasicAttributes();
        Attribute objclass = new BasicAttribute("objectclass");
        objclass.add("top");
        objclass.add("organizationalUnit");
        attrs.put(objclass);
        return attrs;
    }

    @Test
    public void shouldNotThrowWhenDestroyingNonexistentContext() throws NamingException {
        //given
        createContextUsing(Fixture.directoryManagerConnectionParameters);

        //when
        getContext().destroySubcontext("ou=Some nonexistent context name");

        //then no exception is thrown
    }
}
