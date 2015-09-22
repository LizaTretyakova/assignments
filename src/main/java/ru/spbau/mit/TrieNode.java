package ru.spbau.mit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by liza on 21.09.15.
 */
public class TrieNode {
    public static final int nextCharSize = 52 + 1;
    TrieNode[] nextChar = new TrieNode[nextCharSize];
    int size = 0;
    boolean isTerminal = false;

    public int charToIndex(char c) {
        int num = Character.getNumericValue(c);
        if(num >= Character.getNumericValue('a') && num <= Character.getNumericValue('z')) {
            return num - Character.getNumericValue('a') + 1;
        }
        if(num >= Character.getNumericValue('A') && num <= Character.getNumericValue('Z')) {
            return num - Character.getNumericValue('A') + 26 + 1;
        }
        return -1;
    }

    public TrieNode goDown(String element, int i) {
        if(i == element.length()) {
            return isTerminal ? this : null;
        }

        int index = charToIndex(element.charAt(i));
        return (nextChar[index] != null) ? nextChar[index].goDown(element, i + 1) : null;
    }


    public boolean add(String element, int i) {
        if(i == element.length()) {
            boolean notExistedBefore = !isTerminal;
            if(notExistedBefore) {
                size++;
                isTerminal = true;
            }
            return notExistedBefore;
        }
        int curChar = charToIndex(element.charAt(i));

        if(nextChar[curChar] == null) {
            nextChar[curChar] = new TrieNode();
        }

        boolean addedNewString = nextChar[curChar].add(element, i + 1);
        if(addedNewString) {
            size++;
        }
        return addedNewString;
    }

    public boolean remove(String element, int i) {
        if(i == element.length()) {
            boolean res = isTerminal;
            isTerminal = false;
            return res;
        }
        int curChar = charToIndex(element.charAt(i));

        boolean removed = false;
        if(nextChar[curChar] != null) {
            removed = nextChar[curChar].remove(element, i + 1);
        }
        size = removed ? size - 1 : size;
        return removed;
    }

    public void serialize(OutputStream out) throws IOException {
        int intIsTerminal = isTerminal ? 1 : 0;
        out.write(intIsTerminal);

        for(int i = 1; i < nextCharSize; i++) {
            if(nextChar[i] != null) {
                out.write(i);
                nextChar[i].serialize(out);
            }
        }

        out.write(0);
    }

    public void deserialize(InputStream in) throws IOException {
        isTerminal = (in.read() == 1) ? true : false;
        if(isTerminal) {
            size++;
        }

        while(true) {
            int i = in.read();
            if(i == 0) {
                break;
            }
            else {
                nextChar[i] = new TrieNode();
                nextChar[i].deserialize(in);
                size += nextChar[i].size;
            }
        }
    }
}
