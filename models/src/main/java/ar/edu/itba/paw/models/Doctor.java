package ar.edu.itba.paw.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DoctorOffice> doctorOffices = new ArrayList<>();



    @Column(name = "rating")
    private Double rating = 1.0;

    @Column(name = "rating_count")
    private int ratingCount = 0;

    @Column(name = "image_id")
    private Long imageId = null;

    @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private DoctorProfile profile;

    @JsonManagedReference
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("startDate DESC")
    private List<DoctorExperience> experiences = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DoctorCertification> certifications = new ArrayList<>();


    public Doctor() {
    }

    public Doctor(String name, String lastName, String email, String password, String phone, String language, Long imageId, Double rating, int ratingCount, boolean verified) {
        super(name, lastName, email, password, phone, language, verified);
        this.imageId = imageId;
        this.rating = rating;
        this.ratingCount = ratingCount;
    }


    public Doctor(String name, String lastName, String email, String password, String phone, String language, Long imageId, boolean verified, List<Specialty> specialtyList, List<Coverage> coverageList) {
        super(name, lastName, email, password, phone, language, verified);
        this.imageId = imageId;
        this.specialtyList = specialtyList;
        this.coverageList = coverageList;

    }

    public static class Views {
        public static class Public {}
        public static class Private extends Public {}
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

    public List<DoctorOffice> getDoctorOffices() {
        return doctorOffices;
    }

    public void setDoctorOffices(List<DoctorOffice> doctorOffices) {
        this.doctorOffices = doctorOffices;
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

    public DoctorProfile getProfile() { return profile; }

    public void setProfile(DoctorProfile profile) { this.profile = profile;}

    public List<DoctorExperience> getExperiences() { return experiences; }

    public void setExperiences(List<DoctorExperience> experiences) { this.experiences = experiences; }

    public List<DoctorCertification> getCertifications() { return certifications; }

    public void setCertifications(List<DoctorCertification> certifications) { this.certifications = certifications; }


    private int specialtiesHashCode() {
        return specialtyList == null ? 0 :
                specialtyList.stream()
                        .mapToInt(Specialty::hashCode)
                        .sum();
    }

    private int coveragesHashCode() {
        return coverageList == null ? 0 :
                coverageList.stream()
                        .mapToInt(Coverage::hashCode)
                        .sum();
    }

    private int officesHashCode() {
        return doctorOffices == null ? 0 :
                doctorOffices.stream()
                        .mapToInt(DoctorOffice::hashCode)
                        .sum();
    }

    private int experiencesHashCode() {
        return experiences == null ? 0 :
                experiences.stream()
                        .mapToInt(DoctorExperience::hashCode)
                        .sum();
    }

    private int certificationsHashCode() {
        return certifications == null ? 0 :
                certifications.stream()
                        .mapToInt(DoctorCertification::hashCode)
                        .sum();
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), specialtiesHashCode(), coveragesHashCode(), officesHashCode(), rating, ratingCount, imageId, profile.hashCode(), experiencesHashCode(), certificationsHashCode());
    }

}
