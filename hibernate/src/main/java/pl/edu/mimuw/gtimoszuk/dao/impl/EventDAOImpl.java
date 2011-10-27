package pl.edu.mimuw.gtimoszuk.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.gtimoszuk.dao.EventDAO;
import pl.edu.mimuw.gtimoszuk.entity.Event;

@Repository
@Transactional(readOnly = true)
public class EventDAOImpl extends HibernateDaoSupport implements EventDAO {

	@Override
	public List<Event> getAll() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);
		@SuppressWarnings("unchecked")
		List<Event> events = getHibernateTemplate().findByCriteria(criteria);

		return events;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Integer save(Event event) {
		Integer res = (Integer) getHibernateTemplate().save(event);
		return res;
	}

}
