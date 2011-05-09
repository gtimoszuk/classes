package pl.edu.mimuw.gtimoszuk.ldap;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

import java.util.ArrayList;
import java.util.List;

public class NamingUtilities {

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
}
