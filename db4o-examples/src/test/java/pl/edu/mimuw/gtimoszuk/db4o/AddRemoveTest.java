package pl.edu.mimuw.gtimoszuk.db4o;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.edu.mimuw.gtimoszuk.db4o.domain.Pilot;

import com.db4o.ObjectSet;

/**
 * This class presents how to add or remove objects. DB is created on
 * 
 * @author gtimoszuk
 * 
 */
public class AddRemoveTest extends BaseTest {

	@BeforeClass
	public static void before() {
		dbSetup();
	}

	@AfterClass
	public static void after() {
		dbClose();
	}

	@Test
	public void sampleStoreTest() {
		// storeFirstPilot
		Pilot pilot1 = new Pilot("Michael Schumacher", 100);
		db.store(pilot1);
		System.out.println("Stored " + pilot1);

		// We'll need a second pilot, too.

		// storeSecondPilot
		Pilot pilot2 = new Pilot("Rubens Barrichello", 99);
		db.store(pilot2);
		System.out.println("Stored " + pilot2);
	}

	@Test
	public void retrieveTest() {

		// retrieveAllPilots
		ObjectSet<Pilot> result = db.queryByExample(Pilot.class);
		listResult(result);
	}

	@Test
	public void retrieveInAModernWayTest() {
		List<Pilot> pilots = db.query(Pilot.class);
		listResult(pilots);

	}

	@Test
	public void updatePilotTest() {

		int pointsExpected = -1;

		// updatePilot
		ObjectSet<Pilot> result = db.queryByExample(new Pilot(
				"Michael Schumacher", 0));
		Pilot found = result.next();
		found.addPoints(11);
		pointsExpected = found.getPoints();
		db.store(found);
		System.out.println("Added 11 points for " + found);
		retrieveInAModernWayTest();

		result = db.queryByExample(new Pilot("Michael Schumacher", 0));
		found = result.next();

		assertEquals(pointsExpected, found.getPoints());
	}

	@Test
	public void deleteTest() {
		// deleteFirstPilotByName
		ObjectSet<Pilot> result = db.queryByExample(new Pilot(
				"Michael Schumacher", 0));
		Pilot found = result.next();
		db.delete(found);
		System.out.println("Deleted " + found);
		result = db.queryByExample(new Pilot("Michael Schumacher", 0));
		// we assume that there should be no Michael Schumacher in db now
		assertFalse(result.hasNext());

	}

}
