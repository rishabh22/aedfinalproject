package edu.neu.javachip.aedfinalproject.model.workqueue;

import edu.neu.javachip.aedfinalproject.model.organization.Organization;
import edu.neu.javachip.aedfinalproject.model.patient.Patient;
import edu.neu.javachip.aedfinalproject.model.userAccount.UserAccount;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;


public abstract class WorkRequest {
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private WorkRequest.Type type;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private UserAccount sender;
    @Getter
    @Setter
    private UserAccount receiver;
    @Getter
    @Setter
    private Patient patient;
    @Getter
    @Setter
    private Organization sentTo;
    @Getter
    private WorkRequestStatus status = WorkRequestStatus.PENDING;
    @Getter
    @Setter
    private LocalDateTime requestDate = LocalDateTime.now();
    @Getter
    @Setter
    private LocalDateTime resolveDate;
    @Getter
    @Setter
    private DoctorOpinionRequest doctorOpinionRequest;

    public enum WorkRequestStatus {
        SUCCESS("Successfully Completed"),
        ERROR("Error"),
        PENDING("Pending"),
        ASSIGNED("Assigned"),
        WAITING("Waiting for another request"),
        UNDER_PROCESSING("Under Processing"),
        REJECTED("Rejected"),
        CANCELLED("Cancelled");

        private String value;

        private WorkRequestStatus(String value){
            this.value=value;
        }
        public String getValue() {
            return value;
        }
        @Override
        public String toString(){
            return value;
        }
    }

    public enum Type {
        DOCTORS_OPINION_REQUEST("Doctor's Opinion"),
        LAB_TEST_REQUEST("Lab Test"),
        LAB_RESULT_REQUEST("Lab Result"),
        PROVIDE_MEDICAL_ITEMS_REQUEST("Provide Medical Items"),
        RESTOCK_MEDICAL_INVENTORY("Restock Medical Inventory"),
        PROVIDE_TREATMENT_REQUEST("Provide Treatment");

        private String value;

        private Type(String value){
            this.value=value;
        }
        public String getValue() {
            return value;
        }
        @Override
        public String toString(){
            return value;
        }
    }

    public void setStatus(WorkRequestStatus status) {
        this.status = status;
        if(status.getValue().equals(WorkRequestStatus.SUCCESS.getValue())){
            this.resolveDate = LocalDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkRequest that = (WorkRequest) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
