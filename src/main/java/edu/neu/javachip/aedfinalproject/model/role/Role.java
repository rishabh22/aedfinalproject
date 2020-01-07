package edu.neu.javachip.aedfinalproject.model.role;

public abstract class Role {
    private RoleType type;

    public Role(RoleType type) {
        this.type = type;
    }

    public enum RoleType{
        SYSTEM_ADMIN("System Admin"),
        ADMIN("Admin"),
        DOCTOR("Doctor"),
        DOCTORS_ASSISTANT("Doctor's Assistant"),
        PHARMACIST("Pharmacist"),
        LAB_TECHNICIAN("Lab Technician"),
        MEDICAL_STORE_PROVIDER("Medical Store Provider"),
        PRIMARY_CARE_PROVIDER("Primary Care Provider");

        private String value;
        private RoleType(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }


    public abstract String getWorkArea();

    public RoleType getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.type.value;
    }
}
