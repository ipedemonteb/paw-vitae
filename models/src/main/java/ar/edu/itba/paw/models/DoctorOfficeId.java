package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DoctorOfficeId implements Serializable {

    @Column(name = "doctor_id")
    private long doctorId;

    @Column(name = "neighborhood_id")
    private long neighborhoodId;

    @Column(name = "office_name", length = 50)
    private String officeName;

    public DoctorOfficeId() { }

    public DoctorOfficeId(long doctorId, long neighborhoodId, String officeName) {
        this.doctorId       = doctorId;
        this.neighborhoodId = neighborhoodId;
        this.officeName     = officeName;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public long getNeighborhoodId() {
        return neighborhoodId;
    }

    public String getOfficeName() {
        return officeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoctorOfficeId)) return false;
        DoctorOfficeId that = (DoctorOfficeId) o;
        return Objects.equals(doctorId, that.doctorId)
                && Objects.equals(neighborhoodId, that.neighborhoodId)
                && Objects.equals(officeName, that.officeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, neighborhoodId, officeName);
    }
}

