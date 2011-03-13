package pl.edu.mimuw.gtimoszuk.db4o;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.edu.mimuw.gtimoszuk.db4o.domain.Car;
import pl.edu.mimuw.gtimoszuk.db4o.domain.Pilot;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

public class CarTest extends BaseTest {

	@BeforeClass
	public static void before() {
		dbSetup();
		carsWithPilorsLoaded();
	}

	@AfterClass
	public static void after() {
		dbClose();
	}

	@Test
	public void retrieveAllCarsQBE() {
		Car proto = new Car(null);
		ObjectSet<Car> result = db.queryByExample(proto);
		listResult(result);

		assertEquals(2, result.size());
	}

	@Test
	public void retrieveAllPilotsQBE() {
		Pilot proto = new Pilot(null, 0);
		ObjectSet<Pilot> result = db.queryByExample(proto);
		listResult(result);

		assertEquals(2, result.size());
	}

	@Test
	public void retrieveAllPilots() {
		ObjectSet<Pilot> result = db.queryByExample(Pilot.class);
		listResult(result);

		assertEquals(2, result.size());

	}

	@Test
	public void retrieveCarByPilotQBE() {
		Pilot pilotproto = new Pilot("Rubens Barrichello", 0);
		Car carproto = new Car(null);
		carproto.setPilot(pilotproto);
		ObjectSet<Car> result = db.queryByExample(carproto);
		listResult(result);

		assertEquals(1, result.size());

	}

	@Test
	public void retrieveCarByPilotNameQuery() {
		Query query = db.query();
		query.constrain(Car.class);
		query.descend("pilot").descend("name").constrain("Rubens Barrichello");
		ObjectSet<Car> result = query.execute();
		listResult(result);

		assertEquals(1, result.size());

	}

	@Test
	public void retrieveCarByPilotProtoQuery() {
		Query query = db.query();
		query.constrain(Car.class);
		Pilot proto = new Pilot("Rubens Barrichello", 0);
		query.descend("pilot").constrain(proto);
		ObjectSet<Car> result = query.execute();
		listResult(result);

		assertEquals(1, result.size());

	}

	@Test
	public void retrievePilotByCarModelQuery() {
		Query carquery = db.query();
		carquery.constrain(Car.class);
		carquery.descend("model").constrain("Ferrari");
		Query pilotquery = carquery.descend("pilot");
		ObjectSet<Pilot> result = pilotquery.execute();
		listResult(result);

		assertEquals(1, result.size());

	}

	@Test
	public void retrieveAllPilotsNative() {
		List<Pilot> results = db.query(new Predicate<Pilot>() {

			private static final long serialVersionUID = -2714882265059997529L;

			public boolean match(Pilot pilot) {
				return true;
			}
		});
		listResult(results);

		assertEquals(2, results.size());

	}

	@Test
	public void retrieveAllCars() {
		ObjectSet<Car> results = db.queryByExample(Car.class);
		listResult(results);

		assertEquals(2, results.size());

	}

	@Test
	public void retrieveCarsByPilotNameNative() {
		final String pilotName = "Rubens Barrichello";
		List<Car> results = db.query(new Predicate<Car>() {

			private static final long serialVersionUID = 7966911126948738533L;

			public boolean match(Car car) {
				return car.getPilot().getName().equals(pilotName);
			}
		});
		listResult(results);

		assertEquals(1, results.size());

	}

	@Test
	public void updateCar() {
		List<Car> result = db.query(new Predicate<Car>() {

			private static final long serialVersionUID = 1498798123908328757L;

			public boolean match(Car car) {
				return car.getModel().equals("Ferrari");
			}
		});

		assertEquals(1, result.size());

		Car found = result.get(0);
		found.setPilot(new Pilot("Somebody else", 0));
		db.store(found);

		result = db.query(new Predicate<Car>() {

			private static final long serialVersionUID = 5361537103128010144L;

			public boolean match(Car car) {
				return car.getModel().equals("Ferrari");
			}
		});
		listResult(result);

		assertEquals(1, result.size());

	}

	@Test
	public void updatePilotSingleSession() {
		List<Car> result = db.query(new Predicate<Car>() {

			private static final long serialVersionUID = 2218307568859876015L;

			public boolean match(Car car) {
				return car.getModel().equals("Ferrari");
			}
		});

		assertEquals(1, result.size());

		Car found = result.get(0);
		found.getPilot().addPoints(1);
		db.store(found);
		result = db.query(new Predicate<Car>() {

			private static final long serialVersionUID = 6410449748896002514L;

			public boolean match(Car car) {
				return car.getModel().equals("Ferrari");
			}
		});
		listResult(result);

		assertEquals(1, result.size());

	}

	@Test
	public void updatePilotSeparateSessions() {
		int expectedPoints = -1;
		List<Car> result = db.query(new Predicate<Car>() {

			private static final long serialVersionUID = -2279273961273716655L;

			public boolean match(Car car) {
				return car.getModel().equals("Ferrari");
			}
		});

		assertEquals(1, result.size());

		Car found = result.get(0);
		found.getPilot().addPoints(1);
		expectedPoints = found.getPilot().getPoints();

		db.store(found);
		db.close();
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
				DB4OFILENAME);

		result = db.query(new Predicate<Car>() {

			private static final long serialVersionUID = 1L;

			public boolean match(Car car) {
				return car.getModel().equals("Ferrari");
			}
		});
		listResult(result);

		int actualSize = result.size();
		int acutalPoints = result.get(0).getPilot().getPoints();

		db.close();

		assertEquals(1, actualSize);
		assertEquals(expectedPoints, acutalPoints);

	}

	@Test
	public void updatePilotSeparateSessionsImproved() {
		int expectedPoints = -1;

		db.close();
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().objectClass(Car.class).cascadeOnUpdate(true);
		db = Db4oEmbedded.openFile(config, DB4OFILENAME);

		List<Car> result = db.query(new Predicate<Car>() {

			private static final long serialVersionUID = 2112444850610839285L;

			public boolean match(Car car) {
				return car.getModel().equals("Ferrari");
			}
		});

		assertEquals(1, result.size());

		Car found = result.get(0);
		found.getPilot().addPoints(1);
		expectedPoints = found.getPilot().getPoints();

		db.store(found);
		db.close();
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
				DB4OFILENAME);

		result = db.query(new Predicate<Car>() {
			private static final long serialVersionUID = -4372524896852790743L;

			public boolean match(Car car) {
				return car.getModel().equals("Ferrari");
			}
		});

		listResult(result);

		assertEquals(1, result.size());
		assertEquals(expectedPoints, result.get(0).getPilot().getPoints());

	}

	@SuppressWarnings("serial")
	@Test
	public void deleteFlat() {
		List<Car> result = db.query(new Predicate<Car>() {
			public boolean match(Car car) {
				return car.getModel().equals("Ferrari");
			}
		});

		assertEquals(1, result.size());

		Car found = result.get(0);
		db.delete(found);
		result = db.queryByExample(new Car(null));

		listResult(result);

		assertEquals(1, result.size());

	}

	@SuppressWarnings("serial")
	@Test
	public void deleteDeep() {
		db.close();
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().objectClass(Car.class).cascadeOnDelete(true);
		db = Db4oEmbedded.openFile(config, DB4OFILENAME);

		List<Car> result = db.query(new Predicate<Car>() {
			public boolean match(Car car) {
				return car.getModel().equals("BMW");
			}
		});

		assertEquals(1, result.size());

		Car found = result.get(0);
		db.delete(found);

		result = db.query(new Predicate<Car>() {
			public boolean match(Car car) {
				return true;
			}
		});

		listResult(result);

		assertEquals(0, result.size());

		db.close();
	}

	@SuppressWarnings("serial")
	@Test
	public void deleteDeepRevisited() {

		db.close();
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().objectClass(Car.class).cascadeOnDelete(true);
		db = Db4oEmbedded.openFile(config, DB4OFILENAME);

		ObjectSet<Pilot> result = db.query(new Predicate<Pilot>() {
			public boolean match(Pilot pilot) {
				return pilot.getName().equals("Michael Schumacher");
			}
		});
		Assert.assertTrue(result.hasNext());

		Pilot pilot = result.next();
		Car car1 = new Car("Ferrari");
		Car car2 = new Car("BMW");
		car1.setPilot(pilot);
		car2.setPilot(pilot);
		db.store(car1);
		db.store(car2);
		db.delete(car2);
		List<Car> cars = db.query(new Predicate<Car>() {
			public boolean match(Car car) {
				return true;
			}
		});
		listResult(cars);

		assertEquals(0, result.size());

		Pilot proto = new Pilot(null, 0);
		ObjectSet<Pilot> resultPilot = db.queryByExample(proto);
		listResult(resultPilot);

		assertEquals(0, result.size());

		db.close();
	}

}
