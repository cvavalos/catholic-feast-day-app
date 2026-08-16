package app;
import ui.CalendarController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    public void start(Stage stage) {
        Label label = new Label ("My Liturgical Calendar");
        Scene scene = new Scene(label, 500, 400);


        stage.setTitle("Liturgical Calendar");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
