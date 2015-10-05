package ru.spbau.mit;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CollectionsTest {
    List<Integer> list = Arrays.asList(0, 1, 4, 9, 16);

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
        List<Integer> l = (List<Integer>)Collections.map(Function1Test.square,
                                                         Arrays.asList(0, 1, 2, 3, 4));
        assertEquals(list, l);
    }

    @Test
    public void testFilter() {
        List<Integer> list1 = (List<Integer>)Collections.filter(PredicateTest.isOdd, list);
        assertEquals(2, list1.size());
        assertEquals(1, (int) list1.get(0));
        assertEquals(9, (int) list1.get(1));
    }

    @Test
    public void testTakeWhile() {
        List<Integer> list4 = (List<Integer>)Collections.takeWhile(PredicateTest.isZero, list);
        List<Integer> list3 = (List<Integer>)Collections.takeUnless(PredicateTest.isOdd.not(), list);
        List<Integer> list5 = (List<Integer>)Collections.takeWhile(PredicateTest.isOdd.not(), list);
        assertEquals(list4, list5);
        assertEquals(0, list3.size());
    }

    @Test
    public void testTakeUnless() {
        List<Integer> list2 = (List<Integer>)Collections.takeWhile(PredicateTest.isOdd, list);
        List<Integer> list3 = (List<Integer>)Collections.takeUnless(PredicateTest.isOdd.not(), list);
        List<Integer> list5 = (List<Integer>)Collections.takeWhile(PredicateTest.isOdd.not(), list);
        List<Integer> list6 = (List<Integer>)Collections.takeUnless(PredicateTest.isOdd, list);
        assertEquals(list2, list3);
        assertEquals(list5, list6);
    }

    @Test
    public void testFoldl() {
        assertEquals(-30, (int) Collections.foldl(f, 0, list));
    }

    @Test
    public void testFoldr() {
        assertEquals(10, (int) Collections.foldr(f, 0, list));
        assertEquals(Collections.foldl(Function2Test.sum, 0, list), Collections.foldr(Function2Test.sum, 0, list));
    }

    @Test
    public void testGenerics() {
        List<Integer> list1 = (List<Integer>)Collections.filter(PredicateTest.isOdd, list);
        assertEquals(list1, Collections.filter(p, list1));
    }
}