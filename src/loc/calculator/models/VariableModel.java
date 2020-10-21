package loc.calculator.models;

public class VariableModel {
    protected String variableName;
    protected String variableValue;

    public VariableModel(){

    }

    public VariableModel(String variableName, String variableValue){
        this.variableName = variableName;
        this.variableValue = variableValue;
    }

    public String getVariableName(){
        return variableName;
    }

    public void setVariableName(String name){
        variableName = name;
    }

    public String getVariableValue(){
        return variableValue;
    }

    public void setVariableValue(String value){
        variableValue = value;
    }
}
