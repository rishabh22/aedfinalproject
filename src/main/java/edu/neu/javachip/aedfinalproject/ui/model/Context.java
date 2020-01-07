package edu.neu.javachip.aedfinalproject.ui.model;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import edu.neu.javachip.aedfinalproject.model.ecosystem.Ecosystem;
import edu.neu.javachip.aedfinalproject.model.userAccount.UserAccount;
import edu.neu.javachip.aedfinalproject.ui.Validation;
import edu.neu.javachip.aedfinalproject.ui.ViewFXMLs;
import edu.neu.javachip.aedfinalproject.util.Authenticator;
import edu.neu.javachip.aedfinalproject.util.DB4OUtil;
import edu.neu.javachip.aedfinalproject.util.Utils;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

//Very very very important class which we have created
//This has been created to share data between different controllers
public class Context {


    private Stack<String> fxmlStack = new Stack<>();

    private Ecosystem system;
    private UserAccount loggedInUserAccount;
    //private App app = App.getIntance();
    private DB4OUtil dB4OUtil = DB4OUtil.getInstance();
    @Getter
    @Setter
    private Stage mainStage;
    private Stage currentStage;

    //It is singleton so that we can get the same instance from anywhere->So that a new instance is not created everytime
    private final static Context instance = new Context();//Only created once

    //private AppController

    //Constructor private so no outside access
    private Context() {
        system = dB4OUtil.retrieveSystem();
    }

    //Instance can be returned only using this function
    public static Context getInstance() {
        return instance;
    }

    //Ignore this function
    public boolean login(String userId, String password) {
        if (Authenticator.validate(userId, password)) {
            //loggedUser = User.of(userId);
            //gotoProfile();
            return true;
        } else {
            return false;
        }
    }

    public void openSplash() {
        /*try {
            replaceSceneContent(ViewFXMLs.SPLASH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/

        currentStage = new Stage(StageStyle.TRANSPARENT);
        currentStage.setResizable(false);
        try {
            replaceSceneContent(ViewFXMLs.SPLASH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        currentStage.show();
    }

    public void gotoLogin() {
        /*try {
            replaceSceneContent(ViewFXMLs.LOGIN);
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/

        boolean flag = false;
        if (currentStage != mainStage) {
            currentStage.hide();
            flag = true;
            currentStage = mainStage;
        }
        try {
            replaceSceneContent(ViewFXMLs.LOGIN);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (flag) {
            currentStage.show();
        }
    }

    public void goToUserWorkArea() {


        try {
            replaceSceneContent(loggedInUserAccount.getRole().getWorkArea());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private Parent replaceSceneContent(String fxml) throws Exception {
        Parent page = FXMLLoader.load(getClass().getResource(fxml), null, new JavaFXBuilderFactory());
        Scene scene = currentStage.getScene();
        if (scene == null) {
            scene = new Scene(page, 1300, Region.USE_COMPUTED_SIZE);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            currentStage.setScene(scene);
        } else {
            new FadeOut(currentStage.getScene().getRoot()).setSpeed(0.9).play();
            currentStage.getScene().setRoot(page);
            new FadeIn(currentStage.getScene().getRoot()).setSpeed(0.9).play();
        }
        currentStage.sizeToScene();
        Utils.getGlobalLogger().info("Loaded Scene to stage: " + fxml);
        return page;
    }

    public void logout() {
        Utils.getGlobalLogger().info("Logging out");
        this.storeSystem();
        this.gotoLogin();
    }

    public void closeApp() {
        System.exit(0);
    }

    public void storeSystem() {
        dB4OUtil.storeSystem(system);
    }

    public Ecosystem getSystem() {
        return system;
    }

    public void setLoggedInUserAccount(UserAccount loggedInUserAccount) {
        this.loggedInUserAccount = loggedInUserAccount;
    }

    public UserAccount getLoggedInUserAccount() {
        return loggedInUserAccount;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }



    public Alert buildAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        return alert;
    }

    public void addErrorBorderToField(Node node) {
        node.getStyleClass().add(Validation.ERROR_CSS_CLASS);
    }

    public void removeErrorBorderFromField(Node node) {
        node.getStyleClass().remove(Validation.ERROR_CSS_CLASS);
    }

    public void hideHTMLEditorToolbars(final HTMLEditor editor)
    {
        editor.setVisible(false);
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                Node[] nodes = editor.lookupAll(".tool-bar").toArray(new Node[0]);
                for(Node node : nodes)
                {
                    node.setVisible(false);
                    node.setManaged(false);
                }
                editor.setVisible(true);
            }
        });
    }

}
