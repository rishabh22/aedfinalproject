package edu.neu.javachip.aedfinalproject.ui.login;

import edu.neu.javachip.aedfinalproject.model.enterprise.Enterprise;
import edu.neu.javachip.aedfinalproject.model.network.Network;
import edu.neu.javachip.aedfinalproject.model.organization.Organization;
import edu.neu.javachip.aedfinalproject.model.userAccount.UserAccount;
import edu.neu.javachip.aedfinalproject.ui.Validation;
import edu.neu.javachip.aedfinalproject.ui.model.Context;
import edu.neu.javachip.aedfinalproject.util.Utils;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.lang.reflect.Method;

public class LoginController {
    private Context app;
    @FXML
    private CustomTextField txtUserId;
    @FXML
    private CustomPasswordField pwdPassword;
    private ValidationSupport validationSupport = new ValidationSupport();


    @FXML
    private AnchorPane mainPane;

    @FXML
    private void initialize() {
        app = Context.getInstance();

        try {
            Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
            m.setAccessible(true);
            m.invoke(null, txtUserId, txtUserId.rightProperty());

            m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
            m.setAccessible(true);
            m.invoke(null, pwdPassword, pwdPassword.rightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }

        validationSupport.registerValidator(
                txtUserId,
                true,
                Validator.createRegexValidator("Invalid userid", Validation.PATTERN_USERNAME, Severity.ERROR)
        );

        validationSupport.registerValidator(
                pwdPassword,
                true,
                Validator.createRegexValidator("Invalid Password", Validation.PATTERN_PASSWORD, Severity.ERROR)
        );

        /*mainPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });*/
    }

    @FXML
    private void handleLogin() {

        Utils.getGlobalLogger().info("Login button pressed");
        validationSupport.initInitialDecoration();
        if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
            StringBuilder alertContent = new StringBuilder();
            validationSupport.getValidationResult().getErrors().forEach(e -> alertContent.append(e.getText()).append("\n"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, alertContent.toString());
            alert.setHeaderText("Validation Errors");
            alert.show();
            Utils.getGlobalLogger().info("Login validation failed");
            return;
        }

        //Step1: Check in the system admin user account directory if you have the user

        String userName = txtUserId.getText();
        String password = Utils.getHash(pwdPassword.getText());
        UserAccount userAccount = app.getSystem().getUserAccountDirectory().authenticateUser(userName, password);

        Network inNetwork = null;
        Enterprise inEnterprise = null;
        Organization inOrganization = null;

        if (userAccount == null) {
            //Step 2: Go inside each network and check each enterprise
            for (Network network : app.getSystem().getNetworkList()) {
                //Step 2.a: check against each enterprise
                for (Enterprise enterprise : network.getEnterpriseDirectory().getEnterpriseList()) {
                    userAccount = enterprise.getUserAccountDirectory().authenticateUser(userName, password);
                    if (userAccount == null) {
                        //Step 3:check against each organization for each enterprise
                        for (Organization organization : enterprise.getOrganizationDirectory().getOrganizationList()) {
                            userAccount = organization.getUserAccountDirectory().authenticateUser(userName, password);
                            if (userAccount != null) {
                                inEnterprise = enterprise;
                                inOrganization = organization;
                                inNetwork = network;
                                break;
                            }
                        }

                    } else {
                        inEnterprise = enterprise;
                        break;
                    }
                    if (inOrganization != null) {
                        break;
                    }
                }
                if (inEnterprise != null) {
                    break;
                }
            }
        }

        if (userAccount == null) {
            Utils.getGlobalLogger().info("Login failed due to invalid credentials");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please check the username and password");
            alert.setHeaderText("Invalid Credentials");
            alert.show();
            return;
        }

        app.setLoggedInUserAccount(userAccount);
        Utils.getGlobalLogger().info("Login Successful");

        //TO generate dummy patient data
//        userAccount.getEmployee().getEnterprise().getParentNetwork().getPatientDirectory().genPatients();
//        app.storeSystem();

        app.goToUserWorkArea();
    }

    @FXML

    private void handleReset() {
        txtUserId.setText("");
        pwdPassword.setText("");

    }

}
