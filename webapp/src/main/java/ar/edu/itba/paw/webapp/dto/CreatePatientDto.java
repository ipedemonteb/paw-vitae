package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Patient;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class CreatePatientDto {


        private String name;
        private String lastName;
        private String email;
        private String phone;
        private Long coverage;
        private Long neighborhood;
        private String password;
        private String language;


        public String getName() {
            return name;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }


        public void setName(String name) {
            this.name = name;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }



        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

    public Long getCoverage() {
        return coverage;
    }

    public void setCoverage(Long coverage) {
        this.coverage = coverage;
    }

    public Long getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(Long neighborhood) {
        this.neighborhood = neighborhood;
    }
}


