package org.zbd.berkley.dpl;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.zbd.berkley.dpl.da.SimpleDA;
import org.zbd.berkley.dpl.entity.SimpleEntityClass;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class SimpleEntityClassTest extends AbstractDLPTest {

	private SimpleDA sda;

	@Before
	public void setUp() {
		environment = setUpEnvironment();
		entityStore = setUpEntityStore(environment);
		sda = new SimpleDA(entityStore);
		loadData();
	}

	@After
	public void tearDown() {
		tearDownEnvironemtAndEntityStore(environment, entityStore);
	}

	protected void loadData() {

		// Instantiate and store some entity classes
		SimpleEntityClass sec1 = new SimpleEntityClass();
		SimpleEntityClass sec2 = new SimpleEntityClass();
		SimpleEntityClass sec3 = new SimpleEntityClass();
		SimpleEntityClass sec4 = new SimpleEntityClass();
		SimpleEntityClass sec5 = new SimpleEntityClass();

		sec1.setPKey("keyone");
		sec1.setSKey("skeyone");

		sec2.setPKey("keytwo");
		sec2.setSKey("skeyone");

		sec3.setPKey("keythree");
		sec3.setSKey("skeytwo");

		sec4.setPKey("keyfour");
		sec4.setSKey("skeythree");

		sec5.setPKey("keyfive");
		sec5.setSKey("skeyfour");

		sda.pIdx.put(sec1);
		sda.pIdx.put(sec2);
		sda.pIdx.put(sec3);
		sda.pIdx.put(sec4);
		sda.pIdx.put(sec5);
	}

	@Test
	public void getTest() {
		System.out.println("getTest");
		SimpleEntityClass sec1 = sda.pIdx.get("keyone");
		SimpleEntityClass sec2 = sda.pIdx.get("keytwo");

		SimpleEntityClass sec4 = sda.sIdx.get("skeythree");

		System.out.println("sec1: " + sec1.getPKey());
		System.out.println("sec2: " + sec2.getPKey());
		System.out.println("sec4: " + sec4.getPKey());
	}

	@Test
	public void cursor1Test() {
		System.out.println("cursor1Test");
		PrimaryIndex<String, SimpleEntityClass> pi = entityStore
				.getPrimaryIndex(String.class, SimpleEntityClass.class);
		EntityCursor<SimpleEntityClass> pi_cursor = pi.entities();
		try {
			Iterator<SimpleEntityClass> i = pi_cursor.iterator();
			while (i.hasNext()) {
				System.out.println(i.next());
			}
		} finally {
			pi_cursor.close();
		}
	}

	@Test
	public void cursor2Test() {
		System.out.println("cursor2Test");
		PrimaryIndex<String, SimpleEntityClass> pi = entityStore
				.getPrimaryIndex(String.class, SimpleEntityClass.class);
		EntityCursor<SimpleEntityClass> pi_cursor = pi.entities();
		try {
			for (SimpleEntityClass i : pi_cursor) {
				System.out.println(i);
			}
			// Always make sure the cursor is closed when we are done with it.
		} finally {
			pi_cursor.close();
		}
	}

	@Test
	public void duplicateSecondaryKeyUsageTest() {
		System.out.println("duplicateSecondaryKeyUsageTest");
		PrimaryIndex<String, SimpleEntityClass> pi = entityStore
				.getPrimaryIndex(String.class, SimpleEntityClass.class);

		SecondaryIndex<String, String, SimpleEntityClass> si = entityStore
				.getSecondaryIndex(pi, String.class, "sKey");

		EntityCursor<SimpleEntityClass> sec_cursor = si.subIndex("skeyone")
				.entities();

		try {
			for (SimpleEntityClass seci : sec_cursor) {
				System.out.println(seci);
			}
			// Always make sure the cursor is closed when we are done with it.
		} finally {
			sec_cursor.close();
		}
	}

	@Test
	public void deteleTest() {
		System.out.println("deteleTest");
		PrimaryIndex<String, SimpleEntityClass> pi = entityStore
				.getPrimaryIndex(String.class, SimpleEntityClass.class);

		SecondaryIndex<String, String, SimpleEntityClass> si = entityStore
				.getSecondaryIndex(pi, String.class, "sKey");

		EntityCursor<SimpleEntityClass> sec_cursor = si.subIndex("skeyone")
				.entities();
		sec_cursor.first();

		try {
			System.out.println("removing");
			SimpleEntityClass sec;
			while ((sec = sec_cursor.nextDup()) != null) {
				if (sec.getSKey() == "some value") {
					sec_cursor.delete();
				}
			}
		} finally {
			sec_cursor.close();
		}

		try {
			System.out.println("checking if removing has been successful");
			si = entityStore.getSecondaryIndex(pi, String.class, "sKey");
			sec_cursor = si.subIndex("skeyone").entities();

			for (SimpleEntityClass seci : sec_cursor) {
				System.out.println(seci);
			}

			// Always make sure the cursor is closed when we are done with it.
		} finally {
			sec_cursor.close();
		}
	}

}
