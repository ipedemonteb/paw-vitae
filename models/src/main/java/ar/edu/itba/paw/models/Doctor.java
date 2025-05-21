package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Specialty;

@Entity
@Table(name = "doctors")
@PrimaryKeyJoinColumn(name = "doctor_id")
public class Doctor extends User {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "doctor_specialties",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id")
    )
    private List<Specialty> specialtyList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "doctor_coverages",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "coverage_id")
    )
    private List<Coverage> coverageList = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AvailabilitySlot> availabilitySlots = new ArrayList<>();

    @Column(name = "rating")
    private Double rating = 1.0;

    @Column(name = "rating_count")
    private int ratingCount = 0;

    @Column(name = "image_id")
    private Long imageId = -1L;

    public Doctor() {
        // For Hibernate use
    }

    public Doctor(String name, String lastName, String email, String password, String phone, String language, Long imageId, Double rating, int ratingCount, boolean verified) {
        super(name, lastName, email, password, phone, language, verified);
        this.imageId = imageId;
        this.rating = rating;
        this.ratingCount = ratingCount;
    }

    //Deprecated
//    public Doctor(String name, long id, String lastName, String email, String password,
//                  String phone, String language, Long imageId, Double rating,
//                  int ratingCount, boolean verified) {
//        super(name, id, lastName, email, password, phone, language, verified);
//        this.imageId = imageId;
//        this.rating = rating;
//        this.ratingCount = ratingCount;
//    }

    public Doctor(String name, String lastName, String email, String password, String phone, String language, Long imageId, boolean verified, List<Specialty> specialtyList, List<Coverage> coverageList) {
        super(name, lastName, email, password, phone, language, verified);
        this.imageId = imageId;
        this.specialtyList = specialtyList;
        this.coverageList = coverageList;
    }


    public List<Specialty> getSpecialtyList() {
        return specialtyList;
    }

    public void setSpecialtyList(List<Specialty> specialtyList) {
        this.specialtyList = specialtyList;
    }

    public List<Coverage> getCoverageList() {
        return coverageList;
    }

    public void setCoverageList(List<Coverage> coverageList) {
        this.coverageList = coverageList;
    }

    public List<AvailabilitySlot> getAvailabilitySlots() {
        return availabilitySlots;
    }

    public void setAvailabilitySlots(List<AvailabilitySlot> availabilitySlots) {
        this.availabilitySlots = availabilitySlots;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
