package net.contargo.iris.address.staticsearch.upload;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;


/**
 * This entity contains all information necessary to perform an import of static addresses from a csv file.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Entity
@Table(name = "StaticAddressImportJob")
public class StaticAddressImportJob {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String csvPath;

    public StaticAddressImportJob() {

        // JPA entity classes need a no-arg constructor
    }


    public StaticAddressImportJob(String email, String csvPath) {

        this.email = email;
        this.csvPath = csvPath;
    }

    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }


    public String getEmail() {

        return email;
    }


    public void setEmail(String email) {

        this.email = email;
    }


    public String getCsvPath() {

        return csvPath;
    }


    public void setCsvPath(String csvPath) {

        this.csvPath = csvPath;
    }
}
