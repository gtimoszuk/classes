package pl.edu.mimuw.gtimoszuk.neo4j.laboratory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import pl.edu.mimuw.gtimoszuk.neo4j.laboratory.relatationship.types.ContainmentTypes;

public class AddRemoveTest extends BaseTest {

	Transaction tx;

	@Before
	public void setup() {
		dbSetup();
		tx = graphDb.beginTx();
	}

	@After
	public void tearDown() {
		dbTearDown();

	}

	@Test
	public void simpleAddTest() {
		try {
			Node firstNode = graphDb.createNode();
			Node secondNode = graphDb.createNode();
			Relationship relationship1 = firstNode.createRelationshipTo(
					secondNode, ContainmentTypes.CONTAINS);
			Relationship relationship2 = secondNode.createRelationshipTo(
					firstNode, ContainmentTypes.IS_CONTAINED);

			firstNode.setProperty("message", "Hello, ");
			secondNode.setProperty("message", "world!");
			relationship1.setProperty("message", "brave Neo4j ");
			relationship2.setProperty("message", "Really brave students");
		} finally {
			tx.finish();
		}
	}
}
