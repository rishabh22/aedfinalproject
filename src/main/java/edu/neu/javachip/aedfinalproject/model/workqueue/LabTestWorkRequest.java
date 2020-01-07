package edu.neu.javachip.aedfinalproject.model.workqueue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabTestWorkRequest extends WorkRequest {
    private String labTests;
    private DoctorOpinionRequest doctorOpinionRequest;

    public LabTestWorkRequest() {
        this.setType(Type.LAB_TEST_REQUEST);
    }
}
