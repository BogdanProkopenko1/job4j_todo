package ru.job4j.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String pass;

    public User(String name, String email, String pass) {
        this.name = name;
        this.email = email;
        this.pass = pass;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return pass;
    }
}
