package pl.edu.mimuw.gtimoszuk.dao.impl;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.mimuw.gtimoszuk.dao.EventDAO;
import pl.edu.mimuw.gtimoszuk.entity.Event;

@ContextConfiguration(locations = { "classpath:pl/edu/mimuw/gtimoszuk/hibernate/testContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class EventDAOImplTest {

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
		dao.save(e);
	}

}
