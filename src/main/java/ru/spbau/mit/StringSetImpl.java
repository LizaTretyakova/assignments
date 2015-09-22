package ru.spbau.mit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by liza on 21.09.15.
 */
public class StringSetImpl implements StringSet, StreamSerializable {
    TrieNode head = new TrieNode();

    /**
     * Expected complexity: O(|element|)
     * @return <tt>true</tt> if this set did not already contain the specified
     *         element
     */
    public boolean add(String element) {
        return head.add(element, 0);
    }


    /**
     * Expected complexity: O(|element|)
     * @return <tt>true</tt> if this set contained the specified element
     */
    public boolean remove(String element) {
        return head.remove(element, 0);
    }

    /**
     * Expected complexity: O(|element|)
     */
    public boolean contains(String element) {
        TrieNode tmp = head.goDown(element, 0);
        return (tmp != null) ? tmp.isTerminal : false;
    }

    /**
     * Expected complexity: O(1)
     */
    public int size() {
        return head.size;
    }

    /**
     * Expected complexity: O(|prefix|)
     */
    public int howManyStartsWithPrefix(String prefix) {
        TrieNode tmp = head.goDown(prefix, 0);
        return (tmp != null) ? tmp.size : 0;
    }

    /**
     * @throws SerializationException in case of IOException during serialization
     */
    public void serialize(OutputStream out) throws SerializationException {
        try {
            head.serialize(out);
        } catch(IOException e) {
            //System.out.println("Failed to serialize.");
            //System.out.flush();
            throw new SerializationException();
        }
    }

    /**
     * Replace current state with data from input stream containing serialized data
     * @throws SerializationException in case of IOException during deserialization
     */
    public void deserialize(InputStream in) throws SerializationException {
        head = new TrieNode();
        try {
            head.deserialize(in);
        } catch (IOException e) {
            //System.out.println("Failed to deserialize.");
            //System.out.flush();
            throw new SerializationException();
        }
    }
}
