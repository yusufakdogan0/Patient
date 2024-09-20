package com.akdogan.app.User.Models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Laborant {
    @Id
    @GeneratedValue
    public Long id;
    @Column
    public String firstName;
    @Column
    public String laborantId;
    @Column
    public String lastName;
    @OneToMany(mappedBy = "laborant", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();


}
