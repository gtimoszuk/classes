package pl.edu.mimuw.gtimoszuk.db4o;

import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.edu.mimuw.gtimoszuk.db4o.domain.Pilot;

import com.db4o.query.Predicate;

/**
 * 
 * @author gtimoszuk
 * 
 */
public class NativeQueriesTest extends BaseTest {

	@BeforeClass
	public static void before() {
		dbSetup();
		pilotsLoader();
	}

	@AfterClass
	public static void after() {
		dbClose();
	}

	@Test
	public void firstQuery() {

		List<Pilot> pilots = db.query(new Predicate<Pilot>() {
			/**
			 * just to avoid warning
			 */
			private static final long serialVersionUID = -2227013492829071415L;

			public boolean match(Pilot pilot) {
				return pilot.getPoints() == 100;
			}
		});
		Assert.assertEquals(1, pilots.size());
	}

	@Test
	public void advancedGet() {

		List<Pilot> result = db.query(new Predicate<Pilot>() {
			/**
			 * just to avoid warning
			 */
			private static final long serialVersionUID = -2227013492829071414L;

			public boolean match(Pilot pilot) {
				return pilot.getPoints() > 99 && pilot.getPoints() < 199
						|| pilot.getName().equals("Rubens Barrichello");
			}
		});
		Assert.assertEquals(2, result.size());
	}
}
