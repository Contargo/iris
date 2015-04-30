package net.contargo.iris.sequence;

import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * Represents the next id to assign to an entity.
 *
 * @author  Arnold Franke - franke@synyx.de
 */
@Entity
public class UniqueIdSequence {

    public static final String SYSTEM_UNIQUEID_PREFIX = "1301";
    public static final int SYSTEM_UNIQUEID_LENGTH = 16;

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String entityName;

    private BigInteger nextId;

    UniqueIdSequence() {

        // JPA Entity classes must have a default constructor.
    }


    public UniqueIdSequence(String entityName, BigInteger nextId) {

        this.entityName = entityName;
        this.nextId = nextId;
    }

    public Long getId() {

        return id;
    }


    public String getEntityName() {

        return entityName;
    }


    public BigInteger getNextId() {

        return nextId;
    }


    public void incrementNextId() {

        nextId = nextId.add(BigInteger.ONE);
    }


    public void setNextId(BigInteger nextId) {

        this.nextId = nextId;
    }
}
