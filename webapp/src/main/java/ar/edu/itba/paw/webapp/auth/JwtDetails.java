package ar.edu.itba.paw.webapp.auth;

import java.util.Date;

public class JwtDetails {
    private final String id;
    private final String token;
    private final String email;
    private final Date issuedAt;
    private final Date expiration;
    private final JwtTokenType type;


    private JwtDetails(String id, String token, String email, Date issuedAt, Date expiration, JwtTokenType type) {
        this.id = id;
        this.token = token;
        this.email = email;
        this.issuedAt = issuedAt;
        this.expiration = expiration;
        this.type = type;
    }

    public String getId() {
        return id;
    }
    public String getToken() {
        return token;
    }
    public String getEmail() {
        return email;
    }
    public Date getIssuedAt() {
        return issuedAt;
    }
    public Date getExpiration() {
        return expiration;
    }
    public JwtTokenType getType() {
        return type;
    }
    public static class Builder {
        private String id;
        private String token;
        private String email;
        private Date issuedAt;
        private Date expiration;
        private JwtTokenType type;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setIssuedDate(Date issuedDate) {
            this.issuedAt = issuedDate;
            return this;
        }

        public Builder setExpirationDate(Date expirationDate) {
            this.expiration = expirationDate;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setTokenType(JwtTokenType tokenType) {
            this.type = tokenType;
            return this;
        }

        public JwtDetails build() {
            return new JwtDetails(id, token, email, issuedAt, expiration, type);
        }
    }


}
