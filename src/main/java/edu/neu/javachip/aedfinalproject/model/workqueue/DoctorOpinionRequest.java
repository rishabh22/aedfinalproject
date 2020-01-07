package edu.neu.javachip.aedfinalproject.model.workqueue;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


public class DoctorOpinionRequest extends WorkRequest {
    @Setter
    @Getter
    private String patientProblem;

    @Getter
    @Setter
    private byte[] image;

    @Getter
    private List<LabResultWorkRequest> labResultWorkRequests = new ArrayList<>();

    public DoctorOpinionRequest() {
        this.setType(Type.DOCTORS_OPINION_REQUEST);
    }

    public void addLabResult(LabResultWorkRequest labResultWorkRequest){
        labResultWorkRequests.add(labResultWorkRequest);
    }
}
