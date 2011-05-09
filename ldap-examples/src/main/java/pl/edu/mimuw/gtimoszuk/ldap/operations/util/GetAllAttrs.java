package pl.edu.mimuw.gtimoszuk.ldap.operations.util;

import pl.edu.mimuw.gtimoszuk.ldap.ConnectionUtils;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class GetAllAttrs {

	public static void printAttrs(Attributes attrs) {
		if (attrs == null) {
			System.out.println("No attributes");
		} else {
			/* Print each attribute */
			try {
				for (NamingEnumeration ae = attrs.getAll(); ae.hasMore();) {
					Attribute attr = (Attribute) ae.next();
					System.out.println("attribute: " + attr.getID());

					/* print each value */
					for (NamingEnumeration e = attr.getAll(); e.hasMore(); System.out.println("value: " + e.next()))
						;
				}
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		// Set up the environment for creating the initial context
        Hashtable<String, Object> env = ConnectionUtils.prepareAnonymousSignUpEnvironment();

		try {
			// Create the initial context
			DirContext ctx = new InitialDirContext(env);

			// Get all the attributes of named object
			Attributes answer = ctx.getAttributes("cn=Ted Geisel, ou=People");

			// Print the answer
			printAttrs(answer);

			// Close the context when we're done
			ctx.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}