package pl.edu.mimuw.gtimoszuk.neo4j.laboratory;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * 
 * @author gtimoszuk
 * 
 */
public class TraverseTest extends BaseTest {

	@BeforeClass
	public static void staticSetup() {
		dbRemove();
		dbSetup();
	}

	@AfterClass
	public static void tearDown() {
		dbTearDown();
	}

}
