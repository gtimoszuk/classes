package pl.edu.mimuw.gtimoszuk.neo4j.laboratory;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

/**
 * 
 * @author gtimoszuk
 * 
 */
public class BaseTest {

	private final static String DB_LOCATION = "/tmp/neo4j";

	protected static GraphDatabaseService graphDb;

	public static void dbRemove() {
		deleteDir(new File(DB_LOCATION));
	}

	public static void dbSetup() {
		graphDb = new EmbeddedGraphDatabase(DB_LOCATION);

	}

	public static void dbTearDown() {
		graphDb.shutdown();
	}

	protected static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	public GraphDatabaseService getGraphDb() {
		return graphDb;
	}

	public void setGraphDb(GraphDatabaseService graphDb) {
		this.graphDb = graphDb;
	}

	/**
	 * db stucture
	 */
	public static void sampleDB() {

	}

}
