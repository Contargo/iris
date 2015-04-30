package net.contargo.iris.sequence.service;

import java.math.BigInteger;


/**
 * Manages Sequences of IDs in the database.
 *
 * @author  Arnold Franke - franke@synyx.de
 */
public interface SequenceService {

    /**
     * Determines the next id to use for the Entity of the given entity name and increments the Id in the DB.
     *
     * @param  entityName  as selector to search next id of this entity
     *
     * @return  the next id of the given entity
     */
    BigInteger getNextId(String entityName);


    /**
     * Set the next id to use manually.
     *
     * @param  entityName  to set the id
     * @param  id  to set
     */
    void setNextId(String entityName, BigInteger id);
}
