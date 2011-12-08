/**
 * 
 */
package pl.edu.mimuw.gtimoszuk.neo4j.laboratory;


/**
 * @author gtimoszuk
 * 
 */
public class IndexTest extends BaseTest {
	/*
	 * private final static Logger LOG =
	 * LoggerFactory.getLogger(IndexTest.class); static Map<String, Long>
	 * nodesToIds; static IndexService indexService;
	 * 
	 * @BeforeClass public static void staticSetup() { dbRemove(); dbSetup();
	 * nodesToIds = createSampleDB(); indexService = new
	 * LuceneIndexService(graphDb);
	 * 
	 * }
	 * 
	 * @AfterClass public static void tearDown() { dbTearDown(); }
	 * 
	 * @Test public void basicIndexTest() { Transaction tx = graphDb.beginTx();
	 * try { // Create a node with a "name" property and index it in the //
	 * IndexService. Node personNode = graphDb.createNode();
	 * personNode.setProperty("name", "Thomas Anderson");
	 * indexService.index(personNode, "name", personNode.getProperty("name"));
	 * 
	 * // Get the node with the name "Mattias Persson" Node node =
	 * indexService.getSingleNode("name", "Thomas Anderson"); // also see
	 * index.getNodes method. LOG.info("Node id: " + node.getId() +
	 * " node name property: " + node.getProperty("name")); assert
	 * personNode.equals(node);
	 * 
	 * } finally { tx.finish(); } }
	 * 
	 * @Test public void basicTimelineTest() throws InterruptedException {
	 * Transaction tx = graphDb.beginTx(); try { Node rootNode =
	 * graphDb.createNode(); LOG.info("root node id: " + rootNode.getId());
	 * Timeline timeline = new Timeline("my_timeline", rootNode, graphDb);
	 * 
	 * // Add nodes to your timeline long startTime =
	 * System.currentTimeMillis(); for (int i = 0; i < 500; i++) {
	 * timeline.addNode(graphDb.createNode(), System.currentTimeMillis());
	 * Thread.sleep(new Random().nextInt(30)); }
	 * 
	 * // Get all the nodes in the timeline timeline.getAllNodes(); // All nodes
	 * after timestamp (3 seconds after the start time)
	 * timeline.getAllNodesAfter(startTime + 3000); // All nodes before
	 * timestamp timeline.getAllNodesBefore(System.currentTimeMillis()); // All
	 * nodes between these timestamps // GTI: not really but life...
	 * Iterable<Node> nodes = timeline.getAllNodesBetween(startTime,
	 * System.currentTimeMillis() - 3000); Iterator<Node> iterator =
	 * nodes.iterator(); int i = 0; while (iterator.hasNext()) {
	 * LOG.info("number: " + i + " node id: " + iterator.next().getId()); ++i; }
	 * } finally { tx.finish(); } }
	 */
}
