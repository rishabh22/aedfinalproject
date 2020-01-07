package edu.neu.javachip.aedfinalproject.model.network;

import edu.neu.javachip.aedfinalproject.model.patient.PatientDirectory;
import edu.neu.javachip.aedfinalproject.model.enterprise.EnterpriseDirectory;

import java.util.Objects;

public class Network {
    private int id;
    private PatientDirectory patientDirectory = new PatientDirectory();
    private EnterpriseDirectory enterpriseDirectory=new EnterpriseDirectory();
    private String name;

    public Network(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PatientDirectory getPatientDirectory() {
        return patientDirectory;
    }

    public EnterpriseDirectory getEnterpriseDirectory() {
        return enterpriseDirectory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Network network = (Network) o;
        return id == network.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
