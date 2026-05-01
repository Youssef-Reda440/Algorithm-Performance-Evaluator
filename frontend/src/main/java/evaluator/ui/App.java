package evaluator.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Dashboard dashboard = new Dashboard();
        
        Scene scene = new Scene(dashboard.getView(), 1200, 750);

        // CSS will be added later
        
        stage.setTitle("Algorithm Performance Evaluator");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}