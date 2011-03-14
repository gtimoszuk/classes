package pl.edu.mimuw.gtimoszuk.db4o;

import java.io.File;
import java.util.List;

import pl.edu.mimuw.gtimoszuk.db4o.domain.Car;
import pl.edu.mimuw.gtimoszuk.db4o.domain.Pilot;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

/**
 * Utils class no test to be stored here
 * 
 * @author gtimoszuk
 * 
 */
public class BaseTest {

	protected static String DB4OFILENAME = "/tmp/testDB4Odb";

	protected static boolean freshDB = true;

	protected static ObjectContainer db = null;

	public static void dbSetup() {
		if (freshDB) {
			new File(DB4OFILENAME).delete();
		}
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
				DB4OFILENAME);

	}

	public void openDB() {
		if (db == null) {
			db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
					DB4OFILENAME);
		}

	}

	public static void dbClose() {
		if (db != null) {
			db.close();
			db = null;
		}
	}

	protected static void listResult(List<?> result) {
		System.out.println(result.size());
		for (Object o : result) {
			System.out.println(o);
		}
	}

	/**
	 * This method assumes existing DB!!
	 */
	protected static void pilotsLoader() {
		Pilot pilot1 = new Pilot("Michael Schumacher", 100);
		db.store(pilot1);
		Pilot pilot2 = new Pilot("Rubens Barrichello", 99);
		db.store(pilot2);

	}

	protected static void carsWithPilorsLoaded() {

		Car car1 = new Car("Ferrari");
		Pilot pilot1 = new Pilot("Michael Schumacher", 100);
		car1.setPilot(pilot1);
		db.store(car1);

		Pilot pilot2 = new Pilot("Rubens Barrichello", 99);
		db.store(pilot2);
		Car car2 = new Car("BMW");
		car2.setPilot(pilot2);
		db.store(car2);
	}

	protected static void carsWithSensorsLoader() {
		// storeFirstCar
		Car car1 = new Car("Ferrari");
		Pilot pilot1 = new Pilot("Michael Schumacher", 100);
		car1.setPilot(pilot1);
		db.store(car1);

		// The second car will take two snapshots immediately at startup.

		// storeSecondCar
		Pilot pilot2 = new Pilot("Rubens Barrichello", 99);
		Car car2 = new Car("BMW");
		car2.setPilot(pilot2);
		car2.snapshot();
		car2.snapshot();
		db.store(car2);
	}
}
