package pl.edu.mimuw.gtimoszuk.ldap;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import java.util.ArrayList;
import java.util.List;

public class NamingUtilities {

    /**
     * A convenience array constructor
     * @param arrayElements
     * @param <T> element type
     */
    public static <T> T[] $(T... arrayElements) {
        return arrayElements;
    }

    /**
     * Same as NamingUtilities.<T> T[] $(T... arrayElements), only allows explicit array element type specification,
     * as in:
     *      Set<Object> = Sets.newHashSet($(Object, "foo"))
     * which is sometimes needed to circumvent Java's mediocre type inference abilities
     *
     * @param elementClass might be any common superclass of all elements, as arrays are covariant
     * @param arrayElements
     * @param <T> element type, inferred from @param elementClass
     */
    public static <T> T[] $(Class<T> elementClass, T... arrayElements) {
        return arrayElements;
    }

    public static class AuthenticationType {
        public static final String none = "none";
        public static final String simple = "simple";
    }

    public static Multimap<String, Object> toMultimap(Attributes attributes) throws NamingException {
        LinkedListMultimap<String, Object> attributesMultimap = LinkedListMultimap.create();
        NamingEnumeration<? extends Attribute> attributesEnumeration = attributes.getAll();
        while (attributesEnumeration.hasMore()) {
            Attribute attribute = attributesEnumeration.next();
            attributesMultimap.putAll(attribute.getID(), toList(attribute.getAll()));
        }
        return attributesMultimap;
    }

    public static <T> List<T> toList(NamingEnumeration<T> enumeration) throws NamingException {
        List<T> list = new ArrayList<T>();
        while (enumeration.hasMore()) {
            list.add(enumeration.next());
        }
        return list;
    }

    public static BasicAttributes newCaseInsensitiveBasicAttributes() {
        return new BasicAttributes(true);
    }

    public static <T> List<T> toList(NamingEnumeration<T> enumeration, int maxEntries) throws NamingException {
        List<T> list = new ArrayList<T>();
        while (list.size() < maxEntries && enumeration.hasMore()) {
            list.add(enumeration.next());
        }
        return list;
    }

    public static boolean entryExistsIn(Context context, String entryCN) throws NamingException {
        try {
            context.lookup(entryCN);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
}
