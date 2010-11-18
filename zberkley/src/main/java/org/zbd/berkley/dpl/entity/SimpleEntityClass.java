package org.zbd.berkley.dpl.entity;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.SecondaryKey;
import static com.sleepycat.persist.model.Relationship.*;

@Entity
public class SimpleEntityClass {

    // Primary key is pKey
    @PrimaryKey
    private String pKey;

    // Secondary key is the sKey
    @SecondaryKey(relate=MANY_TO_ONE)
    private String sKey;

    public void setPKey(String data) {
        pKey = data;
    }

    public void setSKey(String data) {
        sKey = data;
    }

    public String getPKey() {
        return pKey;
    }

    public String getSKey() {
        return sKey;
    }
    
    @Override
    public String toString(){
    	String st = "pkey: " + pKey + " sKey: " + sKey;
    	return st;
    }
} 
