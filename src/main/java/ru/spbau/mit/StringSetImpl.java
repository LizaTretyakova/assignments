package ru.spbau.mit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StringSetImpl implements StringSet, StreamSerializable {
    public TrieNode head = new TrieNode();

    public boolean add(String element) {
        return head.add(element, 0);
    }


    public boolean remove(String element) {
        return head.remove(element, 0);
    }

    public boolean contains(String element) {
        TrieNode tmp = head.goDown(element, 0);
        return (tmp != null) ? tmp.isTerminal : false;
    }

    public int size() {
        return head.size;
    }

    public int howManyStartsWithPrefix(String prefix) {
        TrieNode tmp = head.goDown(prefix, 0);
        return (tmp != null) ? tmp.size : 0;
    }

    public void serialize(OutputStream out) throws SerializationException {
        try {
            head.serialize(out);
        } catch(IOException e) {
            throw new SerializationException();
        }
    }

    public void deserialize(InputStream in) throws SerializationException {
        head = new TrieNode();
        try {
            head.deserialize(in);
        } catch (IOException e) {
            throw new SerializationException();
        }
    }
}
