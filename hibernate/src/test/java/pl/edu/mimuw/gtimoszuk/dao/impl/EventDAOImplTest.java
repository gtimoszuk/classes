package pl.edu.mimuw.gtimoszuk.dao.impl;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.mimuw.gtimoszuk.dao.EventDAO;
import pl.edu.mimuw.gtimoszuk.entity.Event;

@ContextConfiguration(locations = { "classpath:pl/edu/mimuw/gtimoszuk/hibernate/testContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class EventDAOImplTest {

	private static final Logger log = LoggerFactory.getLogger(EventDAOImplTest.class);

	@Autowired
	private EventDAO dao;

	@Test
	public void basicTest() {
		dao.getAll();
	}

	@Test
	public void saveTest() {
		Event e = new Event();
		e.setDate(new Date());
		e.setPlace("here");
		e.setName("name");
		Integer i = dao.save(e);
		log.info("new event id {}", i);
		Assert.assertNotNull(i);
		List<Event> list = dao.getAll();
		for (Event event : list) {
			log.info("event {}", event);
		}
	}

}
