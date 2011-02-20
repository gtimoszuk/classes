package pl.edu.mimuw.gtimoszuk.neo4j.laboratory;

import java.util.Iterator;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Traverser.Order;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.impl.traversal.TraversalDescriptionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.mimuw.gtimoszuk.neo4j.laboratory.relatationship.types.ContainmentTypes;

/**
 * 
 * @author gtimoszuk
 * 
 */
public class TraverseTest extends BaseTest {

	private final static Logger LOG = LoggerFactory.getLogger(TraverseTest.class);
	static Map<String, Long> nodesToIds;

	@BeforeClass
	public static void staticSetup() {
		dbRemove();
		dbSetup();
		nodesToIds = createSampleDB();
	}

	@AfterClass
	public static void tearDown() {
		dbTearDown();
	}

	@Test
	public void traverseDeprecatedExample() {
		Transaction tx = graphDb.beginTx();
		try {
			Node a = graphDb.getNodeById(nodesToIds.get(A));
			org.neo4j.graphdb.Traverser traverser = a.traverse(Order.BREADTH_FIRST, StopEvaluator.DEPTH_ONE,
					ReturnableEvaluator.ALL_BUT_START_NODE, ContainmentTypes.CONTAINS, Direction.OUTGOING);
			Iterator<Node> iterator = traverser.iterator();

			while (iterator.hasNext()) {
				LOG.info((String) iterator.next().getProperty(NAME_PROPERTY_KEY));

			}
			tx.success();
		} finally {
			tx.finish();
		}
	}

	@Test
	public void traverseBrandNewExample() {
		Transaction tx = graphDb.beginTx();
		try {
			Traverser traverser;
			Node a = graphDb.getNodeById(nodesToIds.get(A));

			TraversalDescription traversalDescription = new TraversalDescriptionImpl();
			traversalDescription = traversalDescription.depthFirst();
			traversalDescription = traversalDescription.evaluator(Evaluators.excludeStartPosition());
			traversalDescription = traversalDescription.evaluator(Evaluators.toDepth(1));
			traversalDescription.relationships(ContainmentTypes.CONTAINS, Direction.OUTGOING);
			traverser = traversalDescription.traverse(a);
			Iterator<Path> iterator = traverser.iterator();

			while (iterator.hasNext()) {
				LOG.info((String) iterator.next().endNode().getProperty(NAME_PROPERTY_KEY));

			}

			tx.success();
		} finally {
			tx.finish();
		}
	}
}
