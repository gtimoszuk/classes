package pl.edu.mimuw.gtimoszuk.neo4j.laboratory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import pl.edu.mimuw.gtimoszuk.neo4j.laboratory.relatationship.types.ContainmentTypes;

/**
 * A lot of bad
 * 
 * @author gtimoszuk
 * 
 */
public class AddRemoveTest extends BaseTest {

	private Transaction tx;

	@BeforeClass
	public static void staticSetup() {
		dbRemove();
	}

	@Before
	public void setup() {
		dbSetup();
		tx = graphDb.beginTx();
	}

	@After
	public void tearDown() {
		tx.finish();
		dbTearDown();

	}

	@Test
	public void simpleAddTest() {
		try {
			Node firstNode = graphDb.createNode();
			Node secondNode = graphDb.createNode();
			Relationship relationship1 = firstNode.createRelationshipTo(secondNode, ContainmentTypes.CONTAINS);
			Relationship relationship2 = secondNode.createRelationshipTo(firstNode, ContainmentTypes.IS_CONTAINED);

			firstNode.setProperty("message", "Hello, ");
			secondNode.setProperty("message", "world!");
			relationship1.setProperty("message", "brave Neo4j ");
			relationship2.setProperty("message", "Really brave students");
			tx.success(); // XXX:gti without it tx finishes but no changes preserved
		} finally {
			tx.finish();
		}
	}

	@Test
	public void simpleGetTest() {
		long nodeId = 1l;
		String message = "message value";
		String messageKey = "message";

		try {

			Node retrieved = graphDb.getNodeById(nodeId);
			assertNotNull(retrieved);
			assertEquals(nodeId, retrieved.getId());
		} finally {
			tx.finish();
		}

		try {
			tx = graphDb.beginTx();
			Node firstNode = graphDb.createNode();
			firstNode.setProperty(messageKey, message);
			nodeId = firstNode.getId();
			tx.success();
		} finally {
			tx.finish();
		}
		try {
			tx = graphDb.beginTx();
			Node retrieved = graphDb.getNodeById(nodeId);
			assertNotNull(retrieved);
			assertEquals(nodeId, retrieved.getId());
			assertEquals(message, retrieved.getProperty(messageKey));
		} finally {
			tx.finish();
		}
	}
}
