package ru.spbau.mit;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CollectionsTest {
    ArrayList<Integer> list;

    public CollectionsTest() {
        Integer[] tmp = {0, 1, 4, 9, 16};
        list = new ArrayList(Arrays.asList(tmp));
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
        Integer[] tmp = {0, 1, 2, 3, 4};
        ArrayList<Integer> l = (ArrayList<Integer>) Collections.map(Function1Test.square, new ArrayList(Arrays.asList(tmp)));
        assertEquals(list, l);
    }

    @Test
    public void testFilter() {
        ArrayList<Integer> list1 = (ArrayList<Integer>)Collections.filter(PredicateTest.isOdd, list);
        assertEquals(2, list1.size());
        assertEquals(1, (int)list1.get(0));
        assertEquals(9, (int)list1.get(1));
    }

    @Test
    public void testTakeWhile() {
        ArrayList<Integer> list3 = (ArrayList<Integer>)Collections.takeUnless(PredicateTest.isOdd.not(), list);
        ArrayList<Integer> list4 = (ArrayList<Integer>)Collections.takeWhile(PredicateTest.isZero, list);
        ArrayList<Integer> list5 = (ArrayList<Integer>)Collections.takeWhile(PredicateTest.isOdd.not(), list);
        assertEquals(list4, list5);
        assertEquals(0, list3.size());
    }

    @Test
    public void testTakeUnless() {
        ArrayList<Integer> list2 = (ArrayList<Integer>)Collections.takeWhile(PredicateTest.isOdd, list);
        ArrayList<Integer> list3 = (ArrayList<Integer>)Collections.takeUnless(PredicateTest.isOdd.not(), list);
        ArrayList<Integer> list5 = (ArrayList<Integer>)Collections.takeWhile(PredicateTest.isOdd.not(), list);
        ArrayList<Integer> list6 = (ArrayList<Integer>)Collections.takeUnless(PredicateTest.isOdd, list);
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
        ArrayList<Integer> list1 = (ArrayList<Integer>)Collections.filter(PredicateTest.isOdd, list);
        assertEquals(list1, Collections.filter(p, list1));
    }
}