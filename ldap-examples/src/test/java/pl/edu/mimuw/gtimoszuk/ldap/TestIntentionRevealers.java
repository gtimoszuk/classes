package pl.edu.mimuw.gtimoszuk.ldap;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

public class TestIntentionRevealers {

    public static int oneFor(Object itemToCount) {
        return 1;
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
