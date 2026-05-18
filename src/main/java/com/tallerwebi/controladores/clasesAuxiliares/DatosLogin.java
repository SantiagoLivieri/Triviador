package com.tallerwebi.controladores.clasesAuxiliares;

public class DatosLogin {

  private String email;
  private String password;

  public DatosLogin(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public DatosLogin() {}

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
}
