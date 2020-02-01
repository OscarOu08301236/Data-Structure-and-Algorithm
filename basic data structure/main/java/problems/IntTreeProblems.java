package problems;

import datastructures.IntTree;

// IntelliJ will complain that this is an unused import, but you should use IntTreeNode variables
// in your solution, and then this error should go away.
import datastructures.IntTree.IntTreeNode;

/**
 * Parts b.vi, through b.x should go here.
 *
 * (Implement depthSum, numberNodes, removeLeaves, tighten, and trim as described by the spec.
 * See the spec on the website for picture examples and more explanation!)
 *
 * Also note: you may want to use private helper methods to help you solve these problems.
 * YOU MUST MAKE THE PRIVATE HELPER METHODS STATIC, or else your code will not compile.
 * This happens for reasons that aren't the focus of this assignment and are mostly skimmed over in 142
 * and 143.  If you want to know more you can ask on Piazza or at office hours.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not construct new IntTreeNode objects (though you may have as many IntTreeNode variables as you like).
 * - do not call any IntTree methods
 * - do not construct any external data structures like arrays, queues, lists, etc.
 * - do not mutate the .data field of any nodes (except for numberNodes),
 */

public class IntTreeProblems {

    public static int depthSum(IntTree tree) {
        return depthSum(tree.overallRoot, 1);
    }

    private static int depthSum(IntTreeNode root, int currLevel) {
        if (root == null) {
            return 0;
        } else {
            return depthSum(root.left, currLevel + 1) + depthSum(root.right, currLevel + 1) + currLevel * root.data;
        }
    }

    public static void removeLeaves(IntTree tree) {
        tree.overallRoot = removeLeaves(tree.overallRoot);
    }

    private static IntTreeNode removeLeaves(IntTreeNode root) {
        if (root != null) {
            if (root.left == null && root.right == null) {
                root = null;
            } else {
                root.left = removeLeaves(root.left);
                root.right = removeLeaves(root.right);
            }
        }
        return root;
    }

    public static int numberNodes(IntTree tree) {
        return numberNodes(tree.overallRoot, 1);
    }

    private static int numberNodes(IntTreeNode root, int count) {
        if (root != null) {
            root.data = count;
            int leftCount = numberNodes(root.left, count + 1);
            int rightCount = numberNodes(root.right, leftCount + count + 1);
            return 1 + leftCount + rightCount;
        } else {
            return 0;
        }
    }

    public static void tighten(IntTree tree) {
        tree.overallRoot = tighten(tree.overallRoot);
    }

    private static IntTreeNode tighten(IntTreeNode root) {
        if (root != null) {
            root.left = tighten(root.left);
            root.right = tighten(root.right);
            if (root.left == null && root.right != null) {
                root = root.right;
            } else if (root.left != null && root.right == null) {
                root = root.left;
            }
        }
        return root;
    }

    public static void trim(IntTree tree, int min, int max) {
        tree.overallRoot = trim(tree.overallRoot, min, max);
    }

    private static IntTreeNode trim(IntTreeNode root, int min, int max) {
        if (root != null) {
            if (root.data < min) {
                root = trim(root.right, min, max);
            } else if (root.data > max) {
                root = trim(root.left, min, max);
            } else {
                root.left = trim(root.left, min, max);
                root.right = trim(root.right, min, max);
            }
        }
        return root;
    }
}
