/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

/**
 *
 * @author Home Tech
 */
public class Patient {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private int age;
    private String email;
    private String phone;
    private String gender;
    private String role;
    private static int id;
    

   public Patient(int id, String username, String email, String role, String password, String firstname, String lastname, int age, String phone, String gender) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.role = role;
    this.password = password;
    this.firstname = firstname;
    this.lastname = lastname;
    this.age = age;
    this.phone = phone;
    this.gender = gender;
}


    

 
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

  public int getId() {
    return id;
}


}



