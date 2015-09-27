package ru.spbau.mit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

class TrieNode {
    static final int nextCharSize = 52 + 1;
    TrieNode[] nextChar = new TrieNode[nextCharSize];
    int size = 0;
    boolean isTerminal = false;

    static int charToIndex(char c) {
        if(c >= 'a' && c <= 'z') {
            return c - 'a' + 1;
        }
        if(c >= 'A' && c <= 'Z') {
            return c - 'A' + 26 + 1;
        }
        return -1;
    }

    TrieNode goDown(String element, int i) {
        if(i == element.length()) {
            return this;
        }

        int index = charToIndex(element.charAt(i));
        return (nextChar[index] != null) ? nextChar[index].goDown(element, i + 1) : null;
    }


    boolean add(String element, int i) {
        if(i == element.length()) {
            if(!isTerminal) {
                size++;
                isTerminal = true;
                return true;
            }
            return false;
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

    boolean remove(String element, int i) {
        if(i == element.length()) {
            if(isTerminal) {
                size--;
                isTerminal = false;
                return true;
            }
            return false;
        }
        int curChar = charToIndex(element.charAt(i));

        if(nextChar[curChar] != null && nextChar[curChar].remove(element, i + 1)) {
            size--;
            if(nextChar[curChar].size == 0) {
                nextChar[curChar] = null;
            }
            return true;
        }
        return false;
    }

    void serialize(OutputStream out) throws IOException {
        out.write(isTerminal ? 1 : 0);

        for(int i = 1; i < nextCharSize; i++) {
            if(nextChar[i] != null) {
                out.write(i);
                nextChar[i].serialize(out);
            }
        }

        out.write(0);
    }

    void deserialize(InputStream in) throws IOException {
        isTerminal = in.read() == 1;
        if(isTerminal) {
            size++;
        }

        while(true) {
            int i = in.read();
            if(i == 0) {
                break;
            }
            nextChar[i] = new TrieNode();
            nextChar[i].deserialize(in);
            size += nextChar[i].size;
        }
    }
}
