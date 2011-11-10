package pl.edu.mimuw.gtimoszuk.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Two simple aspects one logs particular method and other one tries to all
 * *..*EventListener.on* calls
 * 
 * @author gtimoszuk
 * 
 */
@Aspect
public class LoggingAspect {

	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

	@Before("execution(* *..*EventListener.on*(..))")
	public void eventsLogger(JoinPoint thisJoinPoint) {
		log.info("aspectGTI: before signature {} with {}", thisJoinPoint.getSignature(), thisJoinPoint.getTarget()
				.getClass());
	}

	@Before("execution(void pl.edu.mimuw.gtimoszuk.listener.SamplePreLoadEventListener.onPreLoad(..))")
	public void eventsLogger2(JoinPoint thisJoinPoint) {
		log.info("aspect: before signature {}", thisJoinPoint.getSignature());
	}

}
