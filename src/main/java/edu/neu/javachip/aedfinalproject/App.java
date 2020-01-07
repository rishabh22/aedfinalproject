package edu.neu.javachip.aedfinalproject;

import edu.neu.javachip.aedfinalproject.ui.StringConstants;
import edu.neu.javachip.aedfinalproject.ui.model.Context;
import edu.neu.javachip.aedfinalproject.util.Utils;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;


//This is our starting class
public class App extends Application {


    @Override
    public void start(Stage primaryStage) {
        try {
            Utils.getGlobalLogger().info("Setting up the app stage");
//            Platform.setImplicitExit(false);

            Context context = Context.getInstance();
            context.setCurrentStage(primaryStage);
            context.openSplash();
            primaryStage.setTitle(StringConstants.APP_NAME);
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/steth_black.png")));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((screenBounds.getWidth() - 1300) / 2);
            primaryStage.setY((screenBounds.getHeight() - 650) / 2 - 50);
            context.setMainStage(primaryStage);
//            primaryStage.initStyle(StageStyle.UNDECORATED);

//            primaryStage.show();
        } catch (Exception ex) {
            //Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static void main(String[] args) {
        Utils.setupLogger();
        Utils.getGlobalLogger().info("Starting application");
        launch(args);
    }







    /*public void showContentPane(String sURL){
        try {
            getContentPane().getChildren().clear();
            URL url = getClass().getResource(sURL);

            //this method returns the AnchorPane pContent
            AnchorPane n = (AnchorPane) FXMLLoader.load(url, ResourceBundle.getBundle("src.bundles.bundle", getLocale()));
            AnchorPane.setTopAnchor(n, 0.0);
            AnchorPane.setBottomAnchor(n, 0.0);
            AnchorPane.setLeftAnchor(n, 0.0);
            AnchorPane.setRightAnchor(n, 0.0);

            getContentPane().getChildren().add(n);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }*/
}
