// Controller for the code. Due to simplicity of actual code I decided to not write a service layer.
// Therefore, this is actually Service + Controller

package com.akdogan.app.User.Controller;

import com.akdogan.app.User.Exception.ResourceNotFoundException;
import com.akdogan.app.User.Models.*;
import com.akdogan.app.User.Repo.LaborantRepo;
import com.akdogan.app.User.Repo.ReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    public LaborantRepo laborantRepo;
    @Autowired
    public ReportRepo reportRepo;


    // CRUD FOR LABORANTS
    // Getting a list of laborants
    @GetMapping(value = "/Laborants")
    public List<Laborant> getUsers(){
        return laborantRepo.findAll();
    }
    // Adding a new laborant
    @PostMapping(value = "/Laborants")
    public Laborant addLaborant(@RequestBody Laborant laborant) {
        return laborantRepo.save(laborant);
    }
    // Deleting an existing laborant
    @DeleteMapping(value = "/Laborants/delete/{id}")
    public String deleteLaborant(@PathVariable Long id){
        Laborant laborant = laborantRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Report not found with id: " + id));;
        laborantRepo.delete(laborant);
        return "deleted....";
    }


    // CRUD FOR REPORTS
    // Get the list of all reports sorted by id
    @GetMapping(value = "/Reports")
    public List<Report> getReports(){
        return reportRepo.findAll();
    }
    // Adding a new report
    @PostMapping(value = "Reports")
    public ResponseEntity<String> addReport(
            @RequestParam("patientFirstName") String patientFirstName,
            @RequestParam("patientLastName") String patientLastName,
            @RequestParam("patientId") String patientId,
            @RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date date,
            @RequestParam("diagnosisHeader") String diagnosisHeader,
            @RequestParam("diagnosis") String diagnosis,
            @RequestParam("laborantId") Long laborantId,
            @RequestParam("image") MultipartFile image) throws IOException {
        // Find the Laborant by ID
        Laborant laborant = laborantRepo.findById(laborantId)
                .orElseThrow(() -> new ResourceNotFoundException("Laborant not found"));

        // Create and save the Report
        Report report = new Report();
        report.setId(generateUniqueId()); // Set a unique ID
        report.setPatientFirstName(patientFirstName);
        report.setPatientLastName(patientLastName);
        report.setPatientId(patientId);
        report.setDate(date);
        report.setDiagnosisHeader(diagnosisHeader);
        report.setDiagnosis(diagnosis);
        report.setLaborant(laborant);
        report.image = image.getBytes();
        reportRepo.save(report);

        return ResponseEntity.ok("Report created successfully");
    }
    // Delete an existing report
    @DeleteMapping(value = "/Reports/delete/{id}")
    public String deleteReport(@PathVariable Long id){
        Report report = reportRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Report not found with id: " + id));;
        reportRepo.delete(report);
        return "deleted....";
    }
    // Update an existing report
    @PutMapping("/Reports/update/{id}")
    public ResponseEntity<String> updateReport(
            @PathVariable Long id,
            @RequestParam("patientFirstName") String patientFirstName,
            @RequestParam("patientLastName") String patientLastName,
            @RequestParam("patientId") String patientId,
            @RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date date,
            @RequestParam("diagnosisHeader") String diagnosisHeader,
            @RequestParam("diagnosis") String diagnosis,
            @RequestParam("laborantId") Long laborantId,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        // Find the existing report by ID
        Report existingReport = reportRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

        // Find the Laborant by ID
        Laborant laborant = laborantRepo.findById(laborantId)
                .orElseThrow(() -> new ResourceNotFoundException("Laborant not found"));

        // Update the fields of the existing report
        existingReport.setPatientFirstName(patientFirstName);
        existingReport.setPatientLastName(patientLastName);
        existingReport.setPatientId(patientId);
        existingReport.setDate(date);
        existingReport.setDiagnosisHeader(diagnosisHeader);
        existingReport.setDiagnosis(diagnosis);
        existingReport.setLaborant(laborant);
        existingReport.image = file.getBytes();


        // Save the updated report
        reportRepo.save(existingReport);

        return ResponseEntity.ok("Report updated successfully");
    }






    // SORTING REPORTS BASED ON DATE
    @GetMapping(value = "/Reports/sorted")
    public List<Report> getSortedReports() {
        return reportRepo.findAll(Sort.by(Sort.Direction.ASC, "date"));
    }

    @GetMapping(value = "/Reports/reverseSorted")
    public List<Report> getReverseSortedReports() {
        return reportRepo.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }



    // TO SEE IMAGES
    @GetMapping("/Reports/Image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Report report = reportRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

        byte[] imageBytes = report.image;
        if (imageBytes == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Adjust the content type if using a different image format
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
    // Check an existing report
    @GetMapping("/Reports/{id}")
    public Report getReport(@PathVariable Long id) {
        Report report = reportRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        return report;
    }

    // Method to generate a unique ID between 10000 and 99999
    private Long generateUniqueId() {
        Long id;
        do {
            id = (long) (10000 + Math.random() * (99999 - 10000));
        } while (reportRepo.existsById(id)); // Keep generating until a unique ID is found
        return id;
    }






}








