package calculator.ast.operators;

import calculator.ast.AstNode;
import calculator.errors.EvaluationError;
import datastructures.interfaces.IDictionary;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

public class ExpressionOperators {

    /**
     * Accepts an 'toDouble(inner)' AstNode and returns a new number node representing a
     * simplified version of the AstNode 'inner'.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'toDouble'.
     * - The 'node' parameter has exactly one child: the AstNode to convert into a double.
     *
     * Postconditions:
     *
     * - Returns a number AstNode containing the computed double.
     *
     * There are no other side effects for the inputs.
     *
     * For example, if this method receives the AstNode corresponding to
     * 'toDouble(3 + 4)', this method should return the AstNode corresponding
     * to '7'.
     * 
     * This method is required to handle the following binary operations:
     *      +, -, *, /, ^
     *  (addition, subtraction, multiplication, division, and exponentiation, respectively) 
     * and the following unary operations:
     *      negate, sin, cos
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if any of the expressions uses an unknown operation.
     */
    public static AstNode handleToDouble(AstNode node, IDictionary<String, AstNode> variables) {
        // To help you get started, we've implemented this method for you.
        // You should fill in the locations specified by "your code here"
        // in the 'toDoubleHelper' method.
        //
        // If you're not sure why we have a public method calling a private
        // recursive helper method, review your notes from CSE 143 (or the
        // equivalent class you took) about the 'public-private pair' pattern.

        AstNode.assertOperatorValid("toDouble", 1, node);
        AstNode exprToConvert = node.getChildren().get(0);
        return new AstNode(toDoubleHelper(exprToConvert, variables));
    }

    // This method has default (package-private) access so that it can be used in handlePlot.
    static double toDoubleHelper(AstNode node, IDictionary<String, AstNode> variables) {
        // There are three types of nodes, so we have three cases.
        if (node.isNumber()) {
            return node.getNumericValue();
        } else if (node.isVariable()) {
            String name = node.getName();

            if (!variables.containsKey(name)) {
                throw new EvaluationError("Undefined variable: " + name);
            }
            return toDoubleHelper(variables.get(name), variables);
        } else {
            // You may assume the expression node has the correct number of children.
            // If you wish to make your code more robust, you can also use the provided
            // "assertNodeMatches" method to verify the input is valid.
            String name = node.getName();

            if (name.equals("+")) {
                return toDoubleHelper(node.getChildren().get(0), variables) +
                        toDoubleHelper(node.getChildren().get(1), variables);
            }
            else if (name.equals("-")) {
                return toDoubleHelper(node.getChildren().get(0), variables) -
                        toDoubleHelper(node.getChildren().get(1), variables);
            }
            else if (name.equals("*")) {
                return toDoubleHelper(node.getChildren().get(0), variables) *
                        toDoubleHelper(node.getChildren().get(1), variables);
            }
            else if (name.equals("/")) {
                return toDoubleHelper(node.getChildren().get(0), variables) /
                        toDoubleHelper(node.getChildren().get(1), variables);
            }
            else if (name.equals("^")) {
                return Math.pow(toDoubleHelper(node.getChildren().get(0), variables),
                        toDoubleHelper(node.getChildren().get(1), variables));
            }
            else if (name.equals("negate")) {
                return -toDoubleHelper(node.getChildren().get(0), variables);
            }
            else if (name.equals("sin")) {
                return Math.sin(toDoubleHelper(node.getChildren().get(0), variables));
            }
            else if (name.equals("cos")) {
                return Math.cos(toDoubleHelper(node.getChildren().get(0), variables));
            }
            else {
                throw new EvaluationError("Undefined operation: " + name);
            }
        }
    }

    /**
     * Accepts a 'simplify(inner)' AstNode and returns a new node containing a simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'simplify'.
     * - The 'node' parameter has exactly one child: the AstNode to simplify
     *
     * Postconditions:
     *
     * - Returns an AstNode containing the simplified inner parameter.
     *
     * There are no other side effects for the inputs.
     *
     * For example, if we received the AstNode corresponding to the expression
     * "simplify(3 + 4)", you would return the AstNode corresponding to the
     * number "7".
     *
     * Note: there are many possible simplifications we could implement here,
     * but you are only required to implement a single one: constant folding.
     *
     * That is, whenever you see expressions of the form "NUM + NUM", or
     * "NUM - NUM", or "NUM * NUM", simplify them.
     */
    public static AstNode handleSimplify(AstNode node, IDictionary<String, AstNode> variables) {
        // Try writing this one on your own!
        // Hint 1: Your code will likely be structured roughly similarly
        //         to your "handleToDouble" method
        // Hint 2: When you're implementing constant folding, you may want
        //         to call your "handleToDouble" method in some way
        // Hint 3: When implementing your private pair, think carefully about
        //         when you should recurse. Do you recurse after simplifying
        //         the current level? Or before?

        AstNode.assertOperatorValid("simplify", 1, node);
        AstNode exprToConvert = node.getChildren().get(0);
        return simplifyHelper(exprToConvert, variables);
    }

    private static AstNode simplifyHelper(AstNode node, IDictionary<String, AstNode> variables) {
        if (node.isNumber()) {
            return node;
        }
        else if (node.isVariable()) {
            if (!variables.containsKey(node.getName())) {
                return node;
            }
            else {
                return simplifyHelper(variables.get(node.getName()), variables);
            }
        }
        else if (node.getName().equals("+") || node.getName().equals("*") || node.getName().equals("-")) {
            AstNode leftChild = simplifyHelper(node.getChildren().get(0), variables);
            AstNode rightChild = simplifyHelper(node.getChildren().get(1), variables);

            if (leftChild.isNumber() && rightChild.isNumber()) {
                return new AstNode(toDoubleHelper(node, variables));
            }
            else {
                IList<AstNode> result = new DoubleLinkedList<>();
                result.add(leftChild);
                result.add(rightChild);
                return new AstNode(node.getName(), result);
            }
        }
        else {
            IList<AstNode> result = new DoubleLinkedList<>();
            if (node.getName().equals("/") || node.getName().equals("^")) {
                result.add(simplifyHelper(node.getChildren().get(0), variables));
                result.add(simplifyHelper(node.getChildren().get(1), variables));
            }
            else if (node.getName().equals("negate") || node.getName().equals("sin") || node.getName().equals("cos")) {
                result.add(simplifyHelper(node.getChildren().get(0), variables));
            }
            return new AstNode(node.getName(), result);
        }
    }
}
