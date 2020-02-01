// Shao-Chien Ou
// 03/13/2019
// CSE143
// TA: Zachariah Wolfe
// Assignment #8
// This program can read a given array of frequencies of characters and construct the huffman code
// for each chracter with more than 0 frequency and store the current huffman codes to the given 
// output in the standard format(ASCII code in first line and huffman code in second line)
// or read in the given input from a .code file and construct the character for each part of code
// and write the corresponding characters to the given output until finish the whole code
// the .code file is encoded in legal, valid standard format 

import java.util.*;
import java.io.*;

public class HuffmanCode  {
    // use 256 as ASCII code if there is no character data
    private static final int NONE_CHAR_ASCII = 256;

    // the tree that stores characters and their frequencies from the given input
    private HuffmanNode overallRoot;

    // post: construct a HuffmanCode with the given array of frequencies
    //       the given array is an array of frequencies of each character with its ASCII code
    public HuffmanCode(int[] frequencies) {
        Queue<HuffmanNode> sortedChar = new PriorityQueue<>();
        for(int i = 0; i < frequencies.length; i++) {
            if(frequencies[i] != 0) {
                HuffmanNode newChar = new HuffmanNode((char) i, frequencies[i]);
                sortedChar.add(newChar);
            }
        }
        overallRoot = buildTreeHelper(sortedChar);
    }

    // take the next two HuffmanNodes of the PriorityQueue and combine them together
    // as a new HuffmanNode
    // store (char) 256 as the character of the new HuffmanNode to represent empty of character
    // add the new HuffmanNode back to the PriorityQueue
    // keep processing until there is only one HuffmanNode tree in the PriorityQueue
    // return the new HuffmanNode tree as new overallRoot
    private HuffmanNode buildTreeHelper(Queue<HuffmanNode> sortedChar) {
        while(sortedChar.size() > 1) {
            HuffmanNode first = sortedChar.remove();
            HuffmanNode second = sortedChar.remove();
            HuffmanNode tempRoot = new HuffmanNode((char) NONE_CHAR_ASCII, first.frequency + second.frequency
                    , first, second);
            sortedChar.add(tempRoot);
        }
        HuffmanNode newOverallRoot = sortedChar.peek();
        return newOverallRoot;
    }

    // post: store the current huffman codes to the given output in the standard format
    //       the format of the output file is pre-order
    public void save(PrintStream output) {
        saveHelper(overallRoot, output, "");
    }

    // print the ASCII code and huffman codes of the character if we reach a character
    // keep searching for a chracter until reach one
    // generate the huffman code of the character while searching
    private void saveHelper(HuffmanNode root, PrintStream output, String compressedCode) {
        if(root.lessf == null && root.moref == null) {
            output.println((int) root.character);
            output.println(compressedCode);
        }
        else {
            saveHelper(root.lessf, output, compressedCode + "0");
            saveHelper(root.moref, output, compressedCode + "1");
        }
    }

    // post: construct a HuffamanNode by reading in the given input from a .code file
    //       the .code file is constructed previously and the given scanner is always
    //       contains data encoded in legal, valid standard format
    //       the format of the given input is pre-order
    public HuffmanCode(Scanner input) {
        while(input.hasNextLine()) {
            int charASCII = Integer.parseInt(input.nextLine());
            String compressedCode = input.nextLine();
            overallRoot = buildTreeHelper(overallRoot, charASCII, compressedCode);
        }
    }

    // return a new HuffmanNode of the character if we already go through the whole
    // huffman code of the character
    // upadate the strcuture of the HuffmanNode tree while going through the huffman code
    // of the character
    // store (char) 256 as the character of the new HuffmanNode to represent empty of character
    private HuffmanNode buildTreeHelper(HuffmanNode root, int charASCII, String compressedCode) {
        if(compressedCode.isEmpty()) {
            HuffmanNode newChar = new HuffmanNode((char) charASCII, 0);
            return newChar;
        }
        else {
            if(root == null) {
                root = new HuffmanNode((char) NONE_CHAR_ASCII, 0);
            }
            if(compressedCode.charAt(0) == '0') {
                root.lessf = buildTreeHelper(root.lessf, charASCII, compressedCode.substring(1));
            }
            else {
                root.moref = buildTreeHelper(root.moref, charASCII, compressedCode.substring(1));
            }
        }
        return root;
    }

    // post: read individual bits from the given input stream and write the corresponding characters
    //       to the given output
    //       it should stop reading when the given input is empty
    //       the given input stream contains a legal encoding of characters for the huffman code
    public void translate(BitInputStream input, PrintStream output) {
        HuffmanNode root = overallRoot;
        while(input.hasNextBit()) {
            if(root.lessf == null && root.moref == null) {
                output.write((int) root.character);
                root = overallRoot;
            }
            else {
                int bit = input.nextBit();
                if(bit == 0) {
                    root = root.lessf;
                }
                else {
                    root = root.moref;
                }
            }
        }
        output.write((int) root.character);
    }

    // class that represent a single node in the tree
    // the nodes of the tree appear in pre-order
    private static class HuffmanNode implements Comparable<HuffmanNode>{
        public HuffmanNode lessf; // reference to less frequency subtree
        public HuffmanNode moref; // reference to more frequency subtree
        public char character; // character stored at this node
        public int frequency; // frequency of character stored at this node

        // constructs a leaf node with the given character and frequency
        public HuffmanNode(char character, int frequency) {
            this(character, frequency, null, null);
        }

        // constructs a leaf or branch node with the given character and frequency and links
        public HuffmanNode(char character, int frequency, HuffmanNode lessf, HuffmanNode moref) {
            this.character = character;
            this.frequency = frequency;
            this.lessf = lessf;
            this.moref = moref;
        }

        // return a number < 0 if this chracter comes before other
        // return a number > 0 if this chracter comes after other
        // return 0 if this has the same frequency with other
        // HuffmanNodes are ordered by frequencies of character
        public int compareTo(HuffmanNode other) {
            return this.frequency - other.frequency;
        }
    }
}