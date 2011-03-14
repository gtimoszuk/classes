package pl.edu.mimuw.gtimoszuk.db4o;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.edu.mimuw.gtimoszuk.db4o.domain.Car;
import pl.edu.mimuw.gtimoszuk.db4o.domain.SensorReadout;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

public class CollectionsExampleTest extends BaseTest {

	@BeforeClass
	public static void beforeClass() {
		dbSetup();
		/** @see BaseTest#carsWithSensorsLoader */
		carsWithSensorsLoader();
	}

	@AfterClass
	public static void afterClass() {
		dbClose();
	}

	@Before
	public void before() {
		openDB();

	}

	@After
	public void after() {
		dbClose();
	}

	@Test
	public void retrieveAllSensorReadout() {
		SensorReadout proto = new SensorReadout(null, null, null);
		ObjectSet<SensorReadout> results = db.queryByExample(proto);
		listResult(results);

		assertEquals(2, results.size());
	}

	@SuppressWarnings("serial")
	@Test
	public void retrieveAllSensorReadoutNative() {
		List<SensorReadout> results = db.query(new Predicate<SensorReadout>() {
			public boolean match(SensorReadout candidate) {
				return true;
			}
		});
		listResult(results);

		assertEquals(2, results.size());

	}

	@Test
	public void retrieveSensorReadoutQBE() {
		SensorReadout proto = new SensorReadout(new double[] { 0.3, 0.1 },
				null, null);
		ObjectSet<SensorReadout> results = db.queryByExample(proto);
		listResult(results);

		assertEquals(1, results.size());

	}

	@SuppressWarnings("serial")
	@Test
	public void retrieveSensorReadoutNative() {
		List<SensorReadout> results = db.query(new Predicate<SensorReadout>() {
			public boolean match(SensorReadout candidate) {
				return Arrays.binarySearch(candidate.getValues(), 0.3) >= 0
						&& Arrays.binarySearch(candidate.getValues(), 1.0) >= 0;
			}
		});
		listResult(results);

		assertEquals(0, results.size());

	}

	@Test
	public void retrieveCarQBE() {
		SensorReadout protoreadout = new SensorReadout(
				new double[] { 0.6, 0.2 }, null, null);
		List<SensorReadout> protohistory = new ArrayList<SensorReadout>();
		protohistory.add(protoreadout);
		Car protocar = new Car(null, protohistory);
		ObjectSet<Car> result = db.queryByExample(protocar);
		listResult(result);

		assertEquals(1, result.size());

	}

	@SuppressWarnings("serial")
	@Test
	public void retrieveCarNative() {
		List<Car> results = db.query(new Predicate<Car>() {
			public boolean match(Car candidate) {
				List<SensorReadout> history = candidate.getHistory();
				for (SensorReadout readout : history) {
					if (Arrays.binarySearch(readout.getValues(), 0.6) >= 0
							|| Arrays.binarySearch(readout.getValues(), 0.2) >= 0)
						return true;
				}
				return false;
			}
		});
		listResult(results);

		assertEquals(1, results.size());

	}

	/**
	 * left with no types to make example less meaningful
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void retrieveCollections() {
		ObjectSet result = db.queryByExample(new ArrayList());
		listResult(result);

		// as we are initializing readouts
		assertEquals(2, result.size());

	}

	/**
	 * left with no types to make example less meaningful
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void retrieveArrays() {
		ObjectSet result = db.queryByExample(new double[] { 0.6, 0.4 });
		listResult(result);

		// Important note QBE does not work well with arrays
		assertEquals(0, result.size());

	}

	@Test
	public void retrieveSensorReadoutQuery() {
		Query query = db.query();
		query.constrain(SensorReadout.class);
		Query valuequery = query.descend("values");
		// gti:ihmo a bi tricky :)
		valuequery.constrain(0.3);
		valuequery.constrain(0.1);
		ObjectSet<SensorReadout> result = query.execute();
		listResult(result);

		assertEquals(1, result.size());

	}

	@Test
	public void retrieveCarQuery() {
		Query query = db.query();
		query.constrain(Car.class);
		Query historyquery = query.descend("history");
		historyquery.constrain(SensorReadout.class);
		Query valuequery = historyquery.descend("values");
		valuequery.constrain(0.3);
		valuequery.constrain(0.1);
		ObjectSet<Car> result = query.execute();
		listResult(result);

		assertEquals(1, result.size());

	}

	@SuppressWarnings("serial")
	@Test
	public void updateCar() {
		db.close();
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().objectClass(Car.class).cascadeOnUpdate(true);
		db = Db4oEmbedded.openFile(config, DB4OFILENAME);
		List<Car> results = db.query(new Predicate<Car>() {
			public boolean match(Car candidate) {
				return true;
			}
		});
		// all cars
		assertEquals(2, results.size());

		Car car = results.get(0);
		car.snapshot();
		db.store(car);
		SensorReadout proto = new SensorReadout(null, null, null);
		ObjectSet<SensorReadout> result = db.queryByExample(proto);
		listResult(result);
		assertEquals(3, result.size());

	}

	// this test bases on a results of previous one (SIC!)
	@SuppressWarnings("serial")
	@Test
	public void updateCollection() {
		db.close();
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().objectClass(Car.class).cascadeOnUpdate(true);
		db = Db4oEmbedded.openFile(config, DB4OFILENAME);
		ObjectSet<Car> results = db.query(new Predicate<Car>() {
			public boolean match(Car candidate) {
				return true;
			}
		});

		Assert.assertTrue(results.hasNext());

		Car car = results.next();

		car.getHistory().remove(0);
		db.store(car.getHistory());
		results = db.query(new Predicate<Car>() {
			public boolean match(Car candidate) {
				return true;
			}
		});

		while (results.hasNext()) {
			car = results.next();
			for (int idx = 0; idx < car.getHistory().size(); idx++) {
				System.out.println(car.getHistory().get(idx));
			}
		}

	}

	@SuppressWarnings("serial")
	@Test
	public void deleteAll() {
		db.close();
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().objectClass(Car.class).cascadeOnDelete(true);
		db = Db4oEmbedded.openFile(config, DB4OFILENAME);
		ObjectSet<Car> cars = db.query(new Predicate<Car>() {
			public boolean match(Car candidate) {
				return true;
			}
		});
		while (cars.hasNext()) {
			db.delete(cars.next());
		}
		ObjectSet<SensorReadout> readouts = db
				.query(new Predicate<SensorReadout>() {
					public boolean match(SensorReadout candidate) {
						return true;
					}
				});
		while (readouts.hasNext()) {
			db.delete(readouts.next());
		}
	}

}
