package pl.edu.mimuw.gtimoszuk.db4o;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import pl.edu.mimuw.gtimoszuk.db4o.domain.Pilot;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

/**
 * This class only purpose is to create a db to be presented in OME, so no
 * deletes at all
 * 
 * @author gtimoszuk
 * 
 */
@Ignore
public class DB4OOMEHelper {

	private static final String DB4OFILENAME = "/home/ballo0/GTI/zajecia/db4o/sampleDB";

	ObjectContainer db = null;

	@Before
	public void dbOpen() {
		new File(DB4OFILENAME).delete();
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
				DB4OFILENAME);
	}

	@Test
	public void piotCreator() {

		try {
			Pilot pilot1 = new Pilot("Michael Schumacher", 100);
			db.store(pilot1);
			System.out.println("Stored " + pilot1);
			Pilot pilot2 = new Pilot("Rubens Barrichello", 99);
			db.store(pilot2);
			System.out.println("Stored " + pilot2);
		} finally {
			db.close();
		}

	}

}
