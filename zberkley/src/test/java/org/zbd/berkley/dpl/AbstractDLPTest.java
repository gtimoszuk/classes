package org.zbd.berkley.dpl;

import java.io.File;

import org.junit.Before;

import com.sleepycat.je.Durability;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;

public class AbstractDLPTest {
	
	protected final static File ENV_HOME=new File("./target/bdb/test1");
	
	protected Environment environment;
	
	protected EntityStore entityStore;
	
	@Before
	public void cleanENV_HOME_dir(){		
		if (!ENV_HOME.exists()){
			ENV_HOME.mkdirs();
		}else{
			for(File f:ENV_HOME.listFiles()){
				f.delete();
			}
		}
	}

	public Environment setUpEnvironment(){
		EnvironmentConfig config=new EnvironmentConfig();
		config.setAllowCreate(true);
		config.setDurability(Durability.COMMIT_SYNC); // Kiedy commitujemy log
		
		config.setTransactional(true);//Czy chcemy supportować izolacje transakcji (true=repeatable reads, false=read uncommited)
		config.setTxnSerializableIsolation(true);//czy chemy mieć dostępne transakcje na poziomie true=Serializable		
		
		Environment env=new Environment(ENV_HOME, config);
		return env;
	}
	
	public void tearDownEnvironemtAndEntityStore(Environment environment, EntityStore entityStore){
		entityStore.close();
		environment.close();
	}
	
	public EntityStore setUpEntityStore(Environment environment) {
		StoreConfig storeConfig=new StoreConfig();
		storeConfig.setAllowCreate(true);
		storeConfig.setExclusiveCreate(true);
		storeConfig.setTransactional(true);
		
		EntityStore store=new EntityStore(environment, "test1", storeConfig);
		return store;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public EntityStore getEntityStore() {
		return entityStore;
	}

	public void setEntityStore(EntityStore entityStore) {
		this.entityStore = entityStore;
	}

	public static File getEnvHome() {
		return ENV_HOME;
	}
	
}
