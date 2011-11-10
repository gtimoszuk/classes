package pl.edu.mimuw.gtimoszuk.listener;

import org.hibernate.event.PreLoadEvent;
import org.hibernate.event.PreLoadEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author gtimoszuk
 * 
 */
public class SamplePreLoadEventListener implements PreLoadEventListener {

	private static final Logger log = LoggerFactory.getLogger(SamplePreLoadEventListener.class);

	private static final long serialVersionUID = 3605606414909731699L;

	@Override
	public void onPreLoad(PreLoadEvent event) {
		log.info("entity in pre load state: {} with state {}", event.getEntity(), event.getState());
	}
}
