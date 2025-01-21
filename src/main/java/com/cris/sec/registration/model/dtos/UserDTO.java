package com.cris.sec.registration.model.dtos;

import com.cris.sec.registration.validations.ValidEmail;
import com.cris.sec.registration.validations.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@ValidPassword(message = "Las contraseñas no coinciden")
public class UserDTO {

    @NotNull(message = "El campo 'Nombres' no puede estar vacío")
    @NotBlank(message = "El campo 'Nombres' no puede estar vacío")
    private String names;

    @NotNull(message = "El campo 'Apellidos' no puede estar vacío")
    @NotBlank(message = "El campo 'Apellidos' no puede estar vacío")
    private String lastNames;
    @NotNull(message = "El campo 'Email' no puede estar vacío")
    @NotBlank(message = "El campo 'Email' no puede estar vacío")
    @ValidEmail(message = "Ingrese un email correcto")
    private String email;
    @NotNull(message = "El campo 'Contraseña' no puede estar vacío")
    @NotBlank(message = "El campo 'Contraseña' no puede estar vacío")
    private String password;
    @NotNull(message = "El campo 'Confirmar contraseña' no puede estar vacío")
    @NotBlank(message = "El campo 'Confirmar contraseña' no puede estar vacío")
    private String matchingPassword;

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}
