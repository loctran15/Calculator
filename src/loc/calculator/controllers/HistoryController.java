package loc.calculator.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class HistoryController {
    @FXML
    private ListView historyList;

    public void initializeCalculation(ArrayList<String> calculation_history){
        if(calculation_history != null)
        {
        for(String item: calculation_history){
            historyList.getItems().add(item);
        }
        }
    }
}
