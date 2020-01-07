package edu.neu.javachip.aedfinalproject.model.patient;

import edu.neu.javachip.aedfinalproject.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PatientDirectory {

    private List<Patient> patientList = new ArrayList<>();

    public List<Patient> getPatientList() {
        return patientList;
    }

    public PatientDirectory() {
        genPatients();
    }

    public void genPatients() {
        for (int i = 0; i < 1000; i++) {
            Patient patient = new Patient();
            patient.setName("Patient " + i);
            patient.setHeight(78);
            patient.setWeight(165);
            patient.setEmail("noreply@dummymail.com");
            patient.setPhoneNumber(0000000000L);
            int ageOffset = 0 + (int) (Math.random() * ((5 - 0) + 1));
            patient.setDateOfBirth(LocalDate.now().minusYears(ageOffset));
            int genderIndex = 0 + (int) (Math.random() * ((Gender.values().length - 1 - 0) + 1));
            patient.setGender(Gender.values()[genderIndex]);
            int yearOffset = 0 + (int) (Math.random() * ((5 - 0) + 1));
            patient.setCreatedOn(LocalDateTime.now().minusYears(yearOffset));
            addPatient(patient);
        }
    }

    public void addPatient(Patient patient) {
        patient.setId(patientList.stream().mapToInt(Patient::getId).max().orElse(0) + 1);
        patientList.add(patient);
    }

    public void deletePatient(Patient patient) {
        patientList.remove(patient);
    }

    public int getPatientCount() {
        return patientList.size();
    }
}
