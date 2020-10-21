package loc.calculator.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import loc.calculator.Main;
import loc.calculator.calculation.*;
import loc.calculator.models.VariableModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

//Class controlls Calculator gui
public class CalculatorController {

    @FXML
    protected Label expression;
    @FXML
    private Label result;
    @FXML
    private Button variableButton;

    //List of all variables in the expression
    protected ArrayList<String> variableNameList;

    //list of all the variable models
    protected ArrayList<VariableModel> variableModelList;

    //variable that identifies whether the expression contains variables with an unappropriate name.
    protected boolean containErrorVarName;
    //variable that identifies whether the expression contains variables.
    protected boolean containVar;

    //array contains list of past expression
    protected ArrayList<String> historyListArray;

    //Those 2 below variables used to do copy, paste function
    final Clipboard clipboard;
    final ClipboardContent content;

    // Constructor
    public CalculatorController(){
        clipboard = Clipboard.getSystemClipboard();
        content = new ClipboardContent();
        containErrorVarName = false;
        containVar = false;
    }

    //This function will first analyze the string, if it is a key command, do the command. Otherwise, insert to the expression.
    public void doCommand(String command) {
        switch (command) {
            case " ":
                break;
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "0":
            case ".":
                insertToExpression(command);
                break;
            case "+":
            case "-":
            case "/":
            case "^":
            case "(":
            case ")":
            case "%":
                insertToExpression(" " + command + " ");
                break;
            case "x":
            case "*":
                insertToExpression(" " + "*" + " ");
                break;
            case "=":
                scanExpression(expression.getText());
                if(containVar)
                {
                    openVariableWindow(expression.getText());
                }
                setResult(getResult(expression.getText()));
                String history = expression.getText() + " = " + result.getText();
                addToHistory(history);
                break;
            case "ANS":
                insertToExpression(result.getText());
                break;
            case "CLEAR":
                clearExpression();
                break;
            case "DEL":
                String newExpression = getNewExpresion(expression.getText());
                clearExpression();
                insertToExpression(newExpression);
                break;
            case "HIST":
                openHistoryWindow();
                break;
            case "COPY":
                content.put(DataFormat.PLAIN_TEXT, result.getText());
                clipboard.setContent(content);
                break;
            case "PASTE":
                if(clipboard.hasString()){
                    String pasteString =  clipboard.getString();
                    stringToExpression(pasteString);
                }
                break;
            default:
                insertToExpression(command);
        }
    }

    public void stringToExpression(String pasteString) {
        char[] chars = pasteString.toCharArray();
        for (char character: chars) {
            if(character != ' '){
                doCommand(String.valueOf(character));
            }
        }
    }

    @FXML
    //process a key you pressed and convert it to string. Then, put it to expression
    void keyPressed(KeyEvent event) {
        KeyCode command = event.getCode();
        String commandInString = codeToString(command, event.isShiftDown(), event.isControlDown());
        doCommand(commandInString);
    }

    //translate keycode to String
    public String codeToString(KeyCode command, boolean isShiftDown, boolean isControlDown) {
        if(isControlDown && !isShiftDown){
            switch (command){
                case C:
                    return "COPY";
                case V:
                    return "PASTE";
            }
        }
        if(!isControlDown && isShiftDown){
            switch (command){
                case DIGIT5:
                    return "%";
                case DIGIT6:
                    return "^";
                case DIGIT8:
                    return "x";
                case DIGIT9:
                    return "(";
                case DIGIT0:
                    return ")";
                case EQUALS:
                    return "+";
            }
        }
        else {
            switch (command) {
                case DIGIT0:
                    return "0";
                case DIGIT1:
                    return "1";
                case DIGIT2:
                    return "2";
                case DIGIT3:
                    return "3";
                case DIGIT4:
                    return "4";
                case DIGIT5:
                    return "5";
                case DIGIT6:
                    return "6";
                case DIGIT7:
                    return "7";
                case DIGIT8:
                    return "8";
                case DIGIT9:
                    return "9";
                case ENTER:
                case EQUALS:
                    return "=";
                case SLASH:
                    return "/";
                case BACK_SPACE:
                    return "DEL";
                case ESCAPE:
                    return "CLEAR";
                case X:
                    return "x";
                case MINUS:
                    return "-";
                case PERIOD:
                    return ".";
            }
        }
        return " ";
    }

    //insert String to expression by spacing elements of expression to work with the program.
    public void insertToExpression(String command) {
        if(expression.getText().length() != 0) {
            switch (expression.getText().charAt(expression.getText().length() - 1)) {
                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                case '%':
                case '(':
                case ')':
                    expression.setText(expression.getText() +" " + command);
                    return;
            }
        }
        expression.setText(expression.getText() + command);
    }

    // this function will be called when users click HIST button. History window will pop up
    public void openHistoryWindow(){
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/loc/calculator/resources/History.fxml")));
            Parent root = loader.load();
            Main.getHistoryStage().setScene(new Scene(root));

            HistoryController controller = loader.getController();
            controller.initializeCalculation(historyListArray);

            Main.getHistoryStage().show();
        }catch (IOException e){
            System.out.print(e);
        }
    }

    // This function adds current expression to historyListArray
    public void addToHistory(String item){
        if(historyListArray == null){
            historyListArray = new ArrayList<String>();
        }
        historyListArray.add(item.trim());
    }

    // This function will take expression String as a input and then output the result
    public String getResult(String expression) {
        String postfix = Calculation.inf2postf(expression);
        return Calculation.evaluatePostFix(postfix);
    }

    //set the result we calculated to the result label
    public void setResult(String value) {
        if(containVar){
            result.setText("Error: variables found!");
        }
        result.setText(value);
    }

    //clear the expresision
    protected void clearExpression() {
        expression.setText("");
    }


    //this function will remove one char from the expression.
    protected String getNewExpresion(String expression) {
        String newExpression;
        int newLastIndex = expression.length() - 1;
        boolean foundCommand = false;
        while (newLastIndex >= 0) {
            if (foundCommand && expression.charAt(newLastIndex) != ' ') {
                break;
            }
            if (expression.charAt(newLastIndex) != ' ') {
                foundCommand = true;
            }
            newLastIndex -= 1;
        }
        newExpression = expression.substring(0, newLastIndex + 1);
        return newExpression;
    }

    //This function will handle the event when user click to a number or operator button.
    //The value of the button will be added to expression
    @FXML
    private void onMouseClicked(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        String command = button.getText();
        doCommand(command);
    }

    //Scan Expression to identify whether we find any variables.
    protected void scanExpression(String expression){
        String[] stringList = expression.trim().split("\\s+");
        variableNameList = new ArrayList<String>();
        variableModelList = new ArrayList<VariableModel>();
        containErrorVarName = false;
        containVar = false;
        for (String str : stringList) {
            if (isVariable(str)) {
                if(!variableNameList.contains(str)) {
                    variableNameList.add(str);
                }
                containVar = true;
            }
            else if(!isNumber(str) && !isOperator(str) && str.length() != 0){
                containErrorVarName = true;
            }
        }
        for (String name: variableNameList){
            variableModelList.add(new VariableModel(name, " "));
        }
    }

    private boolean isOperator(String input){
        switch (input){
            case "+":
            case "-":
            case "*":
            case "/":
            case "^":
            case "%":
            case "(":
            case ")":
                return true;
        }
        return false;
    }

    //check if a string is a number
    private boolean isNumber(String input){
        try{
            Double.parseDouble(input);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    //check if string is variable
    private boolean isVariable(String input){
        String variableNamePattern = "^[a-zA-Z_$][a-zA-Z_$0-9]*$";
        if(input.matches(variableNamePattern)){
            return true;
        }
        return false;
    }

    //This function will handle event when users clicks on variables button.
    //the variable window will pop up. the list of variables will appear on the window.
    @FXML
    //This function handle event when users click on variable button
    private void variableButtonOnMouseClicked(MouseEvent mouseEvent) {
        scanExpression(expression.getText());
        openVariableWindow(expression.getText());
    }

    //open the variable window
    protected void openVariableWindow(String exp) {
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/loc/calculator/resources/Variable.fxml")));
            Parent root = loader.load();
            Main.getVariableStage().setScene(new Scene(root));
            VariablesController controller = loader.getController();
            controller.initialize(exp);
            Main.getVariableStage().show();

            //calculatorController in object instance of a class controller can acess to CalculatorControll.
            //pass information from variableController to CalculatorController
            controller.calculatorController = this;
            controller.setAll(variableModelList);
        }catch (IOException e){
            System.out.print(e);
        }

        if(containErrorVarName){
            result.setText("Error: Invalid Variable Name!");
        }
    }



    //after users assign a value to a variable. on the expression, the variable names will be replaced with the assigned values.
    public void replaceExpression(String regex, String replacement){
        String exp = expression.getText();
        String newExp = exp.replaceAll(regex, replacement);
        expression.setText(newExp);
    }
}

