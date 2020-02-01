package problems;

import datastructures.LinkedIntList;

// IntelliJ will complain that this is an unused import, but you should use ListNode variables
// in your solution, and then this error should go away.

import datastructures.LinkedIntList.ListNode;

/**
 * Parts b.iii, b.iv, and b.v should go here.
 *
 * (Implement reverse3, firstLast, and shift as described by the spec
 *  See the spec on the website for picture examples and more explanation!)
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not construct new ListNode objects (though you may have as many ListNode variables as you like).
 * - do not call any LinkedIntList methods
 * - do not construct any external data structures like arrays, queues, lists, etc.
 * - do not mutate the .data field of any nodes, change the list only by modifying links between nodes.
 * - Your solution should run in linear time with respect to the number of elements of the linked list.
 */

public class LinkedIntListProblems {

    // Reverses the 3 elements in the LinkedIntList (assume there are only 3 elements).
    public static void reverse3(LinkedIntList list) {
        ListNode temp = list.front;
        list.front = list.front.next.next;
        list.front.next = temp.next;
        temp.next = null;
        list.front.next.next = temp;
    }

    public static void shift(LinkedIntList list) {
        if (list != null) {
            if (list.front != null && list.front.next != null) {
                ListNode temp = list.front.next;
                ListNode currT = temp;
                ListNode curr = list.front;
                while (currT != null && curr.next.next != null) {
                    curr.next = curr.next.next;
                    curr = curr.next;
                    currT.next = curr.next;
                    currT = currT.next;
                }
                curr.next = temp;
            }
        }
    }

    public static void firstLast(LinkedIntList list) {
        if (list != null) {
            if (list.front != null && list.front.next != null) {
                ListNode temp = list.front;
                list.front = list.front.next;
                ListNode curr = list.front;
                while (curr.next != null) {
                    curr = curr.next;
                }
                curr.next = temp;
                temp.next = null;
            }
        }
    }
}
