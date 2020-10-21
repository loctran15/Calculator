package loc.calculator.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import loc.calculator.Main;
import loc.calculator.models.VariableModel;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class VariablesController {
    @FXML
    TableView tableView;

    @FXML
    TableColumn<VariableModel, String> variablesCol;

    @FXML
    TableColumn<VariableModel, String> valueCol;

    @FXML
    TextField expression;

    @FXML
    Button calculateButton;

    // this variable contains a list of variables. This variable is used to connect with the tableView.
    // Whenever we update the variable. The tableview will also be updated.
    private ObservableList<VariableModel> observableList;

    public CalculatorController calculatorController;

    public void initialize(String exp){
        observableList = FXCollections.observableArrayList();
        variablesCol.setCellValueFactory(new PropertyValueFactory<>("variableName"));
        valueCol.setCellValueFactory(new PropertyValueFactory<>("variableValue"));
        tableView.setItems(observableList);

        tableView.setEditable(true);
        valueCol.setCellFactory(TextFieldTableCell.forTableColumn());
        expression.setText(exp);
    }


    //This function will take a list of models as a input and then print the list of models to the variable window
    public void setAll(ArrayList<VariableModel> models){
        observableList.setAll(models);
    }

    //the function handles the event when user assign a value to a variable. the value will replace the variable names in the expression.
    public void ValueOnEditCommit(TableColumn.CellEditEvent<VariableModel, String> variableModelStringCellEditEvent) {
        VariableModel variableModel = (VariableModel) tableView.getSelectionModel().getSelectedItem();
        variableModel.setVariableValue(variableModelStringCellEditEvent.getNewValue());
        calculatorController.replaceExpression(variableModel.getVariableName(), variableModel.getVariableValue());
    }

    // The function is called when users hit enter on expression label text.
    public void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER)
        {
            //reset the expression in calculator window
            calculatorController.clearExpression();
            //set the expression in calculator window to expression in variable window
            calculatorController.stringToExpression(expression.getText());
            //Scan the expression of variable window to detect variables.
            calculatorController.scanExpression(calculatorController.expression.getText());
            //insert all variables to observableList
            setAll(calculatorController.variableModelList);
        }
    }


    //handle the event when users hit calculateButton
    public void onMouseClicked(MouseEvent mouseEvent) {
        calculatorController.doCommand("=");
    }
}
