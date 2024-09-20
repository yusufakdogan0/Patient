package com.akdogan.app.User.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Report {
    @Id
    @GeneratedValue
    public Long id;
    @Column
    public String patientFirstName;
    @Column
    public String patientLastName;
    @Column
    public String patientId;
    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public Date date;
    @Column
    public String diagnosisHeader;
    @Column
    public String diagnosis;
    @ManyToOne
    @JoinColumn(name = "laborantId", nullable = false)
    public Laborant laborant;
    @JsonIgnore
    @Column
    @Lob
    public byte[] image;



    // Getters and setters
    public String getPatientFirstName() { return patientFirstName; }
    public void setPatientFirstName(String patientFirstName) { this.patientFirstName = patientFirstName; }

    public String getPatientLastName() { return patientLastName; }
    public void setPatientLastName(String patientLastName) { this.patientLastName = patientLastName; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getDiagnosisHeader() { return diagnosisHeader; }
    public void setDiagnosisHeader(String diagnosisHeader) { this.diagnosisHeader = diagnosisHeader; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public Laborant getLaborant() { return laborant; }
    public void setLaborant(Laborant laborant) { this.laborant = laborant; }


}


