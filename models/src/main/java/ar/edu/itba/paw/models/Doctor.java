package ar.edu.itba.paw.models;

import javax.print.Doc;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends User {

    private List<Specialty> specialtyList;
    private List<Coverage> coverageList = new ArrayList<>();
    private List<AvailabilitySlot> availabilitySlots = new ArrayList<>();
    private Double rating = 5.0;
    private int ratingCount = 0;
    private long imageId;
    public Doctor(String name, long id, String lastName, String email, String password, String phone, String language,long imageId,Double rating,int ratingCount) {
        super(name, id, lastName, email, password, phone, language);
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.imageId = imageId;
    }
    public Doctor(String name, long id, String lastName, String email, String password, String phone, String language,long imageId) {
        super(name, id, lastName, email, password, phone, language);
        this.imageId = imageId;
    }

    public List<Specialty> getSpecialtyList() {
        return specialtyList;
    }

    public Double getRating() {
        return rating;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
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

}