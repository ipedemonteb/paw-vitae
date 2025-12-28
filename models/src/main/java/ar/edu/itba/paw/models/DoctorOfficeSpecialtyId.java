package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class DoctorOfficeSpecialtyId implements Serializable {
    @Column(name = "office_id")
    private Long officeId;

    @Column(name = "specialty_id")
    private Long specialtyId;

    public Long getOfficeId() {
        return officeId;
    }

    public Long getSpecialtyId() {
        return specialtyId;
    }

}

