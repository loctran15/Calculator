package loc.calculator.calculation;

import javax.swing.*;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.lang.Math;
import java.util.Stack;

public class Calculation {
    public static String evaluatePostFix(String exp)
    {
        if(exp.equals("")){
            return "0";
        }
        else if(exp.equals("ERROR")){
            return exp;
        }
        int max_num_stack = exp.split("\\s+").length;
        DoubleStack stack = new DoubleStack(max_num_stack);

        String[] elements = exp.split("\\s+");
        for (String element : elements) {
            try {
                double number = Double.parseDouble(element);
                stack.push(number);
                continue;
            } catch (NumberFormatException e) {
                ;
            }
            if (stack.currentSize >= 2) {
                double val1 = stack.pop();
                double val2 = stack.pop();
                if (element.equals("+")) {
                    stack.push(val2 + val1);
                }
                else if (element.equals("-")) {
                    stack.push(val2 - val1);
                }
                else if (element.equals("*")) {
                    stack.push(val2 * val1);
                }
                else if (element.equals("/")) {
                    if (val1 != 0) {
                        stack.push(val2 / val1);
                    } else {
                        return "ERROR: Cannot divided by 0";
                    }
                }
                else if (element.equals("%")) {
                    if (val1 != 0) {
                        stack.push(val2 % val1);
                    } else {
                        return "ERROR: Cannot mod by 0";
                    }
                }
                else if (element.equals("^")) {
                    stack.push(Math.pow(val2, val1));
                }
                else {
                    return "ERROR";
                }
            }
            else {
                return "ERROR";
            }

        }
        if(stack.currentSize == 1)
        {
            //Check if stack.pop() is integer or double
            double value = stack.pop();
            if(value % 1 != 0) {
                return String.valueOf(value);
            }
            else
            {
                return String.valueOf((int)value);
            }
        }
        else
        {
            return "ERROR";
        }

    }

    public static String inf2postf(String expression){
        String result = "";
        Stack<String> stack = new Stack<>();
        String[] segment = expression.split("\\s+");
        String lastStr = "";
        for(String c: segment)
        {
            int precedence = precedence(c);
            if(c.equals("-") && (lastStr.equals("") || isOperator(lastStr)))
            {
                result = result + " 0 ";
                precedence = precedence("negative");
            }

            if(c.equals(")") && lastStr.equals("(")){
                return "ERROR";
            }
            //check if the char is operator
            else if(precedence > 0)
            {
                //if the stack is not empty and the current operator has lower or equal precedence to the top operator on the stack,
                //add the top operator of the stack to result and then push the current operator on top of stack
                while(stack.isEmpty() == false && precedence(stack.peek()) >= precedence){
                    result+= stack.pop() + " ";
                }
                stack.push(c);
            }

            //if the character is ")" then pop out an operator from the stack and add it to the result until "(" char (also
            //pop "(" out of the stack.
            else if(c.equals(")"))
            {
                if(stack.isEmpty()){
                    return "ERROR";
                }
                String x = stack.pop();
                while(!x.equals("("))
                {
                    result += x + " ";
                    x = stack.pop();
                }
            }

            //if the character is "(", then push it onto the operator stack.
            else if(c.equals("("))
            {
                stack.push(c);
            }
            else
            {
                //charater is neither operator nor (
                result += c + " ";
            }
            lastStr = c;
        }
        while(!stack.isEmpty()){
            result += stack.pop() + " ";
        }
        return result.trim();
    }
    static int precedence(String c){
        if(c.equals("+") || c.equals("-")){
            return 1;
        }
        else if(c.equals("*") || c.equals("/") || c.equals("%"))
        {
            return 2;
        }
        else if(c.equals("^"))
        {
            return 3;
        }
        else if(c.equals("negative"))
        {
            return 4;
        }
        return -1;
    }

    static boolean isOperator(String c){
        switch (c){
            case "+":
            case "-":
            case "*":
            case "/":
            case "(":
            case "^":
            case "%": return true;
            default: return false;
        }
    }
}
