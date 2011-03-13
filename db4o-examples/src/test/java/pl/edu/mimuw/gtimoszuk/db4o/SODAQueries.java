package pl.edu.mimuw.gtimoszuk.db4o;

import org.junit.Test;

import pl.edu.mimuw.gtimoszuk.db4o.domain.Pilot;

import com.db4o.ObjectSet;
import com.db4o.query.Query;

public class SODAQueries extends BaseTest {

	@Test
	public void simpleGet() {

		// retrieveComplexSODA
		Query query = db.query();
		query.constrain(Pilot.class);
		Query pointQuery = query.descend("points");
		query.descend("name")
				.constrain("Rubens Barrichello")
				.or(pointQuery.constrain(99).greater()
						.and(pointQuery.constrain(199).smaller()));
		ObjectSet<Pilot> result = query.execute();
		listResult(result);
	}

}
