package org.zbd.berkley.dpl.da;

import org.zbd.berkley.dpl.entity.SimpleEntityClass;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class SimpleDA {
    // Open the indices
    public SimpleDA(EntityStore store)
        throws DatabaseException {

        // Primary key for SimpleEntityClass classes
        pIdx = store.getPrimaryIndex(
            String.class, SimpleEntityClass.class);

        // Secondary key for SimpleEntityClass classes
        // Last field in the getSecondaryIndex() method must be
        // the name of a class member; in this case, an 
        // SimpleEntityClass.class data member.
        sIdx = store.getSecondaryIndex(
            pIdx, String.class, "sKey");
    }

    // Index Accessors
    // public fields are bad but who cares for now 
    public PrimaryIndex<String,SimpleEntityClass> pIdx;
    public SecondaryIndex<String,String,SimpleEntityClass> sIdx;
} 
