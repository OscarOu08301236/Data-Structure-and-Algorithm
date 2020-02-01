package calculator.ast.operators;

import calculator.ast.AstNode;
import calculator.errors.EvaluationError;
import calculator.gui.ImageDrawer;
import datastructures.interfaces.IDictionary;
import static calculator.ast.operators.ExpressionOperators.toDoubleHelper;
import datastructures.interfaces.IList;
import datastructures.concrete.DoubleLinkedList;

public class GuiOperators {
    /**
     * This function is responsible for handling the `clear()` operation node.
     *
     * It clears the plotting window when invoked.
     */
    public static AstNode handleClear(AstNode wrapper, IDictionary<String, AstNode> variables, ImageDrawer drawer) {
        AstNode.assertOperatorValid("clear", wrapper);

        drawer.getGraphics().clearRect(0, 0, drawer.getWidth(), drawer.getHeight());

        return wrapper;
    }

    /**
     * Takes as input a 'plot(exprToPlot, var, varMin, varMax, step)' AstNode,
     * the dictionary of variables, and an ImageDrawer, and generates the
     * corresponding plot on the ImageDrawer. Returns some arbitrary AstNode.
     *
     * There are no other side effects for the inputs.
     *
     * Example 1:
     *
     * >>> plot(3 * x, x, 2, 5, 0.5)
     *
     * This method will receive the AstNode corresponding to 'plot(3 * x, x, 2, 5, 0.5)'.
     * Your 'handlePlot' method is then responsible for plotting the equation
     * "3 * x", varying "x" from 2 to 5 in increments of 0.5.
     *
     * In this case, this means you'll be plotting the following points:
     *
     * [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
     *
     * ---
     *
     * Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4"
     * from -10 to 10 in 0.01 increments. In this case, "a" is our "x" variable.
     *
     * >>> c := 4
     * 4
     * >>> step := 0.01
     * 0.01
     * >>> plot(a^2 + c*a + a, a, -10, 10, step)
     *
     * ---
     *
     * @throws EvaluationError  if any of the child expressions other than 'var' contains an undefined variable
     * @throws EvaluationError  if 'var' contains a defined variable or is not a variable
     * @throws EvaluationError  if varMin > varMax
     * @throws EvaluationError  if 'step' is zero or negative
     */
    public static AstNode handlePlot(AstNode node, IDictionary<String, AstNode> variables, ImageDrawer drawer) {
        AstNode.assertOperatorValid("plot", 5, node);
        // plot(exprToPlot, var, varMin, varMax, step)
        AstNode functionNode = node.getChildren().get(0);
        AstNode var = node.getChildren().get(1);
        AstNode min = node.getChildren().get(2);
        AstNode max = node.getChildren().get(3);
        AstNode step = node.getChildren().get(4);

        if (variables.containsKey(var.getName())) {
            throw new EvaluationError("Already defined: " + var.getName());
        }
        if (!var.isVariable()) {
            throw new EvaluationError("Not a variable: " + var.getName());
        }
        String varName = var.getName();
        variables.put(varName, new AstNode(0));
        try {
            toDoubleHelper(functionNode, variables);
        }
        catch (EvaluationError err) {
            throw new EvaluationError("Include undefined variable");
        }
        variables.remove(varName);

        double minVar = 0;
        double maxVar = 0;
        double stepValue = 0;
        try {
            minVar = toDoubleHelper(min, variables);
            maxVar = toDoubleHelper(max, variables);
            stepValue = toDoubleHelper(step, variables);
        }
        catch (EvaluationError err) {
            throw new EvaluationError("Include undefined number(s)");
        }
        if (minVar > maxVar) {
            throw new EvaluationError("minVar > maxVar");
        }
        if (stepValue < 0) {
            throw new EvaluationError("step < 0");
        }

        IList<Double> x = new DoubleLinkedList<>();
        IList<Double> y = new DoubleLinkedList<>();
        for (double i = minVar; i <= maxVar; i += stepValue) {
            x.add(i);
            variables.put(varName, new AstNode(i));
            double yValue = toDoubleHelper(functionNode, variables);
            y.add(yValue);
        }
        variables.remove(varName);
        drawer.drawScatterPlot("plot", varName, "f(" + varName + ")", x, y);

        // Note: every single function we add MUST return an
        // AST node that your "simplify" function is capable of handling.
        // However, your "simplify" function doesn't really know what to do
        // with "plot" functions (and what is the "plot" function supposed to
        // evaluate to anyways?) so we'll settle for just returning an
        // arbitrary number.
        //
        // When working on this method, you should uncomment the following line:

        return new AstNode(1);
    }
}

