package app;
import ui.CalendarController;
import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    public void start(Stage stage) {
        CalendarController calendarController = new CalendarController();

        BorderPane ui = calendarController.getView();

        Scene scene = new Scene(ui);

        stage.setTitle("Liturgical Calendar");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
