package pl.edu.mimuw.gtimoszuk.db4o;

import static junit.framework.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.edu.mimuw.gtimoszuk.db4o.domain.Pilot;

import com.db4o.ObjectSet;
import com.db4o.query.Constraint;
import com.db4o.query.Query;

/**
 * 
 * @author gtimoszuk
 * 
 */
public class SODAQueriesTest extends BaseTest {

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
	public void simpleGet() {

		// retrieveComplexSODA
		Query query = db.query();
		query.constrain(Pilot.class);
		Query pointQuery = query.descend("points");
		query.descend("name")
				.constrain("Rubens Barrichello")
				.or(pointQuery.constrain(99).greater()
						.and(pointQuery.constrain(199).smaller()));
		ObjectSet<Pilot> result = query.execute();
		listResult(result);
	}

	@Test
	public void retrieveAllPilots() {
		Query query = db.query();
		query.constrain(Pilot.class);
		ObjectSet<Pilot> result = query.execute();
		listResult(result);

		assertEquals(2, result.size());
	}

	@Test
	public void retrievePilotByName() {
		Query query = db.query();
		query.constrain(Pilot.class);
		query.descend("name").constrain("Michael Schumacher");
		ObjectSet<Pilot> result = query.execute();
		listResult(result);

		assertEquals(1, result.size());

	}

	@Test
	public void retrievePilotByExactPoints() {
		Query query = db.query();
		query.constrain(Pilot.class);
		query.descend("points").constrain(100);
		ObjectSet<Pilot> result = query.execute();
		listResult(result);

		assertEquals(1, result.size());

	}

	@Test
	public void retrieveByNegation() {
		Query query = db.query();
		query.constrain(Pilot.class);
		query.descend("name").constrain("Michael Schumacher").not();
		ObjectSet<Pilot> result = query.execute();
		listResult(result);

		assertEquals(1, result.size());

	}

	@Test
	public void retrieveByConjunction() {
		Query query = db.query();
		query.constrain(Pilot.class);
		Constraint constr = query.descend("name").constrain(
				"Michael Schumacher");
		query.descend("points").constrain(99).and(constr);
		ObjectSet<Pilot> result = query.execute();
		listResult(result);

		assertEquals(0, result.size());

	}

	@Test
	public void retrieveByDisjunction() {
		Query query = db.query();
		query.constrain(Pilot.class);
		Constraint constr = query.descend("name").constrain(
				"Michael Schumacher");
		query.descend("points").constrain(99).or(constr);
		ObjectSet<Pilot> result = query.execute();
		listResult(result);

		assertEquals(2, result.size());

	}

	@Test
	public void retrieveByComparison() {
		Query query = db.query();
		query.constrain(Pilot.class);
		query.descend("points").constrain(99).greater();
		ObjectSet<Pilot> result = query.execute();
		listResult(result);

		assertEquals(1, result.size());

	}

	@Test
	public void retrieveByDefaultFieldValue() {
		Pilot somebody = new Pilot("Somebody else", 0);
		db.store(somebody);
		Query query = db.query();
		query.constrain(Pilot.class);
		query.descend("points").constrain(0);
		ObjectSet<Pilot> result = query.execute();
		listResult(result);

		assertEquals(1, result.size());

		db.delete(somebody);
	}

	@Test
	public void retrieveSorted() {
		Query query = db.query();
		query.constrain(Pilot.class);
		query.descend("name").orderAscending();
		ObjectSet<Pilot> result = query.execute();

		assertEquals(2, result.size());
		listResult(result);

		query.descend("name").orderDescending();
		result = query.execute();

		assertEquals(2, result.size());
		listResult(result);

	}

}
