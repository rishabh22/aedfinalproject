package edu.neu.javachip.aedfinalproject.ui;

public interface ViewFXMLs {
    String SPLASH = "/fxml/splash.fxml";
    String LOGIN = "/fxml/login/login.fxml";
    String HEADER = "/fxml/common/userHeader.fxml";
    String TRACK_WORK_REQUESTS = "/fxml/common/track_work_requests.fxml";

    enum SYSADMIN_VIEWS {
        DASHBOARD("/fxml/sysadmin/dashboard.fxml"),
        SIDEBAR("/fxml/sysadmin/sidebar.fxml"),
        MANAGE_NETWORKS("/fxml/sysadmin/manage_networks.fxml"),
        MANAGE_ENTERPRISES("/fxml/sysadmin/manage_enterprises.fxml"),
        ADD_ENTERPRISE("/fxml/sysadmin/add_enterprise.fxml"),
        UPDATE_ENTERPRISE("/fxml/sysadmin/update_enterprise.fxml"),
        MANAGE_ADMINS("/fxml/sysadmin/manage_admins.fxml"),
        ADD_USER_ACCOUNT("/fxml/sysadmin/add_useraccount.fxml"),
        UPDATE_USER_ACCOUNT("/fxml/sysadmin/update_useraccount.fxml");


        private String value;

        private SYSADMIN_VIEWS(String value) {
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

    enum ENTERPRISE_ADMIN_VIEWS {
        DASHBOARD("/fxml/enterpriseadmin/dashboard.fxml"),
        MANAGE_ORGANIZATIONS("/fxml/enterpriseadmin/manage_organizations.fxml"),
        ADD_ORGANIZATION("/fxml/enterpriseadmin/add_organizations.fxml"),
        UPDATE_ORGANIZATION("/fxml/enterpriseadmin/update_organization.fxml"),
        MANAGE_EMPLOYEES("/fxml/enterpriseadmin/manage_employees.fxml"),
        ADD_EMPLOYEE("/fxml/enterpriseadmin/add_employee.fxml"),
        UPDATE_EMPLOYEE("/fxml/enterpriseadmin/update_employee.fxml"),
        MANAGE_USER_ACCOUNTS("/fxml/enterpriseadmin/manage_user_accounts.fxml"),
        ADD_USER_ACCOUNT("/fxml/enterpriseadmin/add_user_account.fxml"),
        UPDATE_USER_ACCOUNT("/fxml/enterpriseadmin/update_user_account.fxml");

        private String value;

        private ENTERPRISE_ADMIN_VIEWS(String value) {
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

    enum PRIMARY_CARE_PROVIDER_VIEWS {
        DASHBOARD("/fxml/primarycarecenter/primarycareprovider/dashboard.fxml"),
        DASHBOARD_CONTENT("/fxml/primarycarecenter/primarycareprovider/dashboard_content.fxml"),
        MANAGE_PATIENTS("/fxml/primarycarecenter/primarycareprovider/manage_patients.fxml"),
        WORK_AREA("/fxml/common/work_area.fxml"),
        ADD_PATIENT("/fxml/primarycarecenter/primarycareprovider/add_patient.fxml"),
        UPDATE_PATIENT("/fxml/primarycarecenter/primarycareprovider/update_patient.fxml"),
        RECORD_PATIENT_PROBLEM("/fxml/primarycarecenter/primarycareprovider/record_patient_problem.fxml"),
        CHAT_CLIENT("/fxml/primarycarecenter/primarycareprovider/chat_client.fxml"),
        TRACK_OPINION_REQUESTS("/fxml/primarycarecenter/primarycareprovider/track_work_requests.fxml");

        private String value;

        private PRIMARY_CARE_PROVIDER_VIEWS(String value) {
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

    enum DOCTOR_VIEWS {
        DASHBOARD("/fxml/digitalhealthcareprovider/doctor/dashboard.fxml"),
        WORK_AREA("/fxml/common/work_area.fxml"),
        PROCESS_TREATMENT_REQUEST("/fxml/digitalhealthcareprovider/doctor/process_treatment_request.fxml"),
        PRESCRIBE_TESTS("/fxml/digitalhealthcareprovider/doctor/prescribe_tests.fxml"),
        PRESCRIBE_TREATMENT("/fxml/digitalhealthcareprovider/doctor/prescribe_treatment.fxml"),
        CHAT_SERVER("/fxml/digitalhealthcareprovider/doctor/chat.fxml");

        private String value;

        private DOCTOR_VIEWS(String value) {
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

    enum LAB_TECHS_VIEWS {
        DASHBOARD("/fxml/primarycarecenter/labtechnician/dashboard.fxml"),
        WORK_AREA("/fxml/common/work_area.fxml"),
        PROCESS_TEST_REQUEST("/fxml/primarycarecenter/labtechnician/process_test_request.fxml");

        private String value;

        private LAB_TECHS_VIEWS(String value) {
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

    enum MEDICAL_STORE_PROVIDER_VIEWS {
        DASHBOARD("/fxml/primarycarecenter/medicalstoreprovider/dashboard.fxml"),
        WORK_AREA("/fxml/common/work_area.fxml"),
        PROCESS_MEDICAL_ITEM_REQUEST("/fxml/primarycarecenter/medicalstoreprovider/process_medicalitem_request.fxml");

        private String value;

        private MEDICAL_STORE_PROVIDER_VIEWS(String value) {
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

    enum PHARMACIST_VIEWS {
        DASHBOARD("/fxml/digitalhealthcareprovider/pharmacist/dashboard.fxml"),
        WORK_AREA("/fxml/common/work_area.fxml");

        private String value;

        private PHARMACIST_VIEWS(String value) {
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
}
