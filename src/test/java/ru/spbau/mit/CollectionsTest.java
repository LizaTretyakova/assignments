package ru.spbau.mit;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CollectionsTest {
    ArrayList<Integer> list = new ArrayList<Integer>();
    ArrayList<Integer> list1;
    ArrayList<Integer> list2;
    ArrayList<Integer> list3;
    ArrayList<Integer> list4;
    ArrayList<Integer> list5;
    ArrayList<Integer> list6;

    public CollectionsTest() {
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list = (ArrayList<Integer>) Collections.map(Function1Test.square, list);
        list1 = (ArrayList<Integer>)Collections.filter(PredicateTest.isOdd, list);
        list2 = (ArrayList<Integer>)Collections.takeWhile(PredicateTest.isOdd, list);
        list3 = (ArrayList<Integer>)Collections.takeUnless(PredicateTest.isOdd.not(), list);
        list4 = (ArrayList<Integer>)Collections.takeWhile(PredicateTest.isZero, list);
        list5 = (ArrayList<Integer>)Collections.takeWhile(PredicateTest.isOdd.not(), list);
        list6 = (ArrayList<Integer>)Collections.takeUnless(PredicateTest.isOdd, list);
    }

    static final Function2<Integer, Integer, Integer> f = new Function2<Integer, Integer, Integer>() {
        public Integer apply(Integer x, Integer y) {
            return x - y;
        }
    };

    static final Predicate<Object> p = new Predicate<Object>() {
        public Boolean apply(Object x) {
            return true;
        }
    };

    @Test
    public void testMap() {
        assertEquals(0, (int) list.get(0));
        assertEquals(1, (int)list.get(1));
        assertEquals(4, (int)list.get(2));
        assertEquals(9, (int)list.get(3));
        assertEquals(16, (int)list.get(4));
    }

    @Test
    public void testFilter() {
        assertEquals(2, list1.size());
        assertEquals(1, (int)list1.get(0));
        assertEquals(9, (int)list1.get(1));
    }

    @Test
    public void testTakeWhile() {
        assertEquals(list4, list5);
        assertEquals(0, list3.size());
    }

    @Test
    public void testTakeUnless() {
        assertEquals(list2, list3);
        assertEquals(list5, list6);
    }

    @Test
    public void testFoldl() {
        assertEquals(-30, (int)Collections.foldl(f, 0, list));
    }

    @Test
    public void testFoldr() {
        assertEquals(10, (int)Collections.foldr(f, 0, list));
        assertEquals(Collections.foldl(Function2Test.sum, 0, list), Collections.foldr(Function2Test.sum, 0, list));
    }

    @Test
    public void testGenerics() {
        assertEquals(list1, Collections.filter(p, list1));
    }
}