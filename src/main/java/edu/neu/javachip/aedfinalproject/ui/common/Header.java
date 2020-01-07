package edu.neu.javachip.aedfinalproject.ui.common;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;
import edu.neu.javachip.aedfinalproject.ui.model.Context;
import javafx.scene.paint.Color;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Header extends AnchorPane {
    private Context app;
    @FXML
    private Label lblEmpName;

    @FXML
    private Label lblEmpRole;

    @FXML
    private ImageView imgProfile;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblEnterprise;

    public Header() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ViewFXMLs.HEADER));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        app = Context.getInstance();
        lblEmpName.setText(app.getLoggedInUserAccount().getEmployee().getName());
        try {
            lblEmpRole.setText(app.getLoggedInUserAccount().getRole().getType().getValue());
            imgProfile.setPreserveRatio(true);
            imgProfile.setEffect(new DropShadow(10,2,2, Color.BLUEVIOLET));
            imgProfile.setImage(new Image(new ByteArrayInputStream(app.getLoggedInUserAccount().getEmployee().getProfilePic())));

        } catch (Exception ignored){

        }
    }


    @FXML
    private void handleLogout() {
        app.logout();
    }

    public void changeHeaderTitle(String title){
        lblTitle.setText(title.toUpperCase());
    }
    public void changeEnterpriseName(String enterpriseName){
        lblEnterprise.setText(enterpriseName);
    }
}
