package loc.calculator;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage historyStage = null;
    private static Stage variableStage = null;
    @Override
    // start your application
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("resources/Calculator.fxml"));
        primaryStage.setTitle("Calculator");
        primaryStage.setScene(new Scene(root, 283, 551));
        primaryStage.show();

        createHistoryStage();
        createVariableStage();
    }

    // create a history window
    public void createHistoryStage(){
        historyStage = new Stage();
        historyStage.setTitle("History");
        historyStage.setAlwaysOnTop(true);
        historyStage.setResizable(false);
        historyStage.initModality(Modality.APPLICATION_MODAL);
    }

    // create a variable window
    public void createVariableStage(){
        variableStage = new Stage();
        variableStage.setTitle("Variable list");
        variableStage.setAlwaysOnTop(true);
        variableStage.setResizable(false);
        variableStage.initModality(Modality.APPLICATION_MODAL);
    }

    public static Stage getHistoryStage() {
        return historyStage;
    }

    public static Stage getVariableStage() {return variableStage; }

    public static void main(String[] args) {
        launch(args);
    }
}
