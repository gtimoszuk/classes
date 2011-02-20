package pl.edu.mimuw.gtimoszuk.neo4j.laboratory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import pl.edu.mimuw.gtimoszuk.neo4j.laboratory.relatationship.types.CallTypes;
import pl.edu.mimuw.gtimoszuk.neo4j.laboratory.relatationship.types.ContainmentTypes;

/**
 * 
 * @author gtimoszuk
 * 
 */
public class BaseTest {

	private final static String DB_LOCATION = "/tmp/neo4j";

	protected final static String A = "a";
	protected final static String B = "b";
	protected final static String C = "c";

	protected final static String NAME_PROPERTY_KEY = "name";

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

	/**
	 * db structure:
	 * 
	 * 
	 * <ul>
	 * <li>a contains b</li>
	 * <li>b contains c</li>
	 * <li>a contains c</li>
	 * <ul>
	 * <ul>
	 * <li>c calls a</li>
	 * <li>c calls b</li>
	 * <li>b calls c</li>
	 * <li>b calls a</li>
	 * </ul>
	 */
	public static Map<String, Long> createSampleDB() {
		Map<String, Long> nodesToIds = new HashMap<String, Long>();
		Transaction transaction = null;
		try {
			transaction = graphDb.beginTx();
			Node a = graphDb.createNode();
			Node b = graphDb.createNode();
			Node c = graphDb.createNode();
			nodesToIds.put(A, a.getId());
			nodesToIds.put(B, b.getId());
			nodesToIds.put(C, c.getId());

			a.setProperty(NAME_PROPERTY_KEY, A);
			b.setProperty(NAME_PROPERTY_KEY, B);
			c.setProperty(NAME_PROPERTY_KEY, C);

			a.createRelationshipTo(b, ContainmentTypes.CONTAINS);
			a.createRelationshipTo(c, ContainmentTypes.CONTAINS);
			b.createRelationshipTo(c, ContainmentTypes.CONTAINS);

			c.createRelationshipTo(a, CallTypes.CALLS);
			c.createRelationshipTo(b, CallTypes.CALLS);
			b.createRelationshipTo(c, CallTypes.CALLS);
			b.createRelationshipTo(a, CallTypes.CALLS);

			transaction.success();
		} finally {
			transaction.finish();
		}
		return nodesToIds;
	}
}
