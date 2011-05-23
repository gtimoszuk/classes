package pl.edu.mimuw.gtimoszuk.ldap.topics.operations;

import org.junit.Test;
import pl.edu.mimuw.gtimoszuk.ldap.base.AbstractNamingContextTestWithInitialDirContext;

import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.atFirstUnusedOU;
import static pl.edu.mimuw.gtimoszuk.ldap.Fixture.directoryManagerConnectionParameters;
import static pl.edu.mimuw.gtimoszuk.ldap.LdapAttributesNames.objectClass;
import static pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities.entryExistsIn;
import static pl.edu.mimuw.gtimoszuk.ldap.NamingUtilities.newCaseInsensitiveBasicAttributes;

/*
http://download.oracle.com/javase/tutorial/jndi/ops/create.html
 */
public class SubcontextTest extends AbstractNamingContextTestWithInitialDirContext {

    private String additionalSubcontextOU = "ou=Some additional subcontext OU";

    @Override
    protected void augumentGivens() throws NamingException {
        assertThat(entryExistsIn(getContext(), atFirstUnusedOU), is(false));
        assertThat(entryExistsIn(getContext(), additionalSubcontextOU), is(false));
        getContext().createSubcontext(additionalSubcontextOU, exampleSubcontextContextAttributes());
    }

    @Override
    protected void cleanUpPossibleFixtureChanges() throws NamingException {
        getContext().destroySubcontext(atFirstUnusedOU);
        getContext().destroySubcontext(additionalSubcontextOU);
    }

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/Create.java
     */
    @Test
    public void shouldCreateSubcontext() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);
        String newContextOU = atFirstUnusedOU;

        //when
        DirContext newContext = getContext().createSubcontext(newContextOU, exampleSubcontextContextAttributes());

        //then
        assertThat(newContext.getNameInNamespace(), is(newContextOU + "," + getContext().getNameInNamespace()));
        NamingEnumeration<NameClassPair> subcontexts = getContext().list("");
        assertThat(containsContextWithName(newContextOU, subcontexts), is(true));
    }

    private Attributes exampleSubcontextContextAttributes() {
        Attributes attributes = newCaseInsensitiveBasicAttributes();
        Attribute objectClasses = new BasicAttribute(objectClass);
        objectClasses.add("top");
        objectClasses.add("organizationalUnit");
        attributes.put(objectClasses);
        return attributes;
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

    /*
    http://download.oracle.com/javase/tutorial/jndi/ops/examples/Destroy.java
     */
    @Test
    public void shouldDestroySubcontext() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);
        String contextToDestroyOU = additionalSubcontextOU;

        //when
        getContext().destroySubcontext(contextToDestroyOU);

        //then
        assertThat(entryExistsIn(getContext(), contextToDestroyOU), is(false));
    }

    @Test
    public void shouldNotThrowWhenDestroyingNonexistentContext() throws NamingException {
        //given
        createContextUsing(directoryManagerConnectionParameters);

        //when
        getContext().destroySubcontext("ou=Some nonexistent context name");

        //then no exception is thrown
    }
}
