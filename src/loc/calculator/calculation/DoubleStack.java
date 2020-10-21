package loc.calculator.calculation;
public class DoubleStack {
    private int maxSize;
    private double[] stackArray;
    private int top;
    int currentSize;

    public DoubleStack(int max){
        maxSize = max;
        stackArray = new double[maxSize];
        top = -1;
        currentSize = 0;
    }

    public void push(double j){
        stackArray[++top] = j;
        currentSize += 1;
    }

    public double pop() {
        currentSize -= 1;
        return stackArray[top--];

    }

    public double peek() {
        return stackArray[top];
    }

    public boolean isEmpty() {
        return (top == -1);
    }
}
