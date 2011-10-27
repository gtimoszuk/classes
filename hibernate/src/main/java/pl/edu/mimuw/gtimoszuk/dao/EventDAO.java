package pl.edu.mimuw.gtimoszuk.dao;

import java.util.List;

import pl.edu.mimuw.gtimoszuk.entity.Event;

public interface EventDAO {

	List<Event> getAll();

	Integer save(Event event);

}
