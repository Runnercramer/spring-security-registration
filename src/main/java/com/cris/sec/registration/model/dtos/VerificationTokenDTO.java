package com.cris.sec.registration.model.dtos;

import java.util.Date;

public class VerificationTokenDTO {
    private String token;

    private UserDTO userDTO;

    private Date expirationDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUser() {
        return userDTO;
    }

    public void setUser(UserDTO user) {
        this.userDTO = user;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("userDTO").append(this.userDTO)
                .append("expirationDate").append(this.expirationDate)
                .toString();
    }
}
