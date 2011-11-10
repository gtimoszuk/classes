package pl.edu.mimuw.gtimoszuk.interceptor;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author gtimoszuk
 * 
 */
public class SampleInterceptor extends EmptyInterceptor implements Interceptor {

	private static final long serialVersionUID = 6954497749321362918L;

	private static final Logger log = LoggerFactory.getLogger(SampleInterceptor.class);

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		log.info("loading object: {} with prop names {}", entity, propertyNames);
		return super.onLoad(entity, id, state, propertyNames, types);
	}
}
