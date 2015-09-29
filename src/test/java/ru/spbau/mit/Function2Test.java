package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class Function2Test {
    static final Function2<Integer, Integer, Integer> sum = new Function2<Integer, Integer, Integer>() {
        public Integer apply(Integer x, Integer y) {
            return x + y;
        }
    };

    static final Function2<Integer, Integer, Integer> mult = new Function2<Integer, Integer, Integer>() {
        public Integer apply(Integer x, Integer y) {
            return x * y;
        }
    };

    static final Function2<Integer, Integer, Integer> frac = new Function2<Integer, Integer, Integer>() {
        public Integer apply(Integer x, Integer y) {
            return x / y;
        }
    };

    static final Function1<Object, Integer> g = new Function1<Object, Integer>() {
        public Integer apply(Object a) {
            System.out.println(a);
            return 1;
        }
    };

    static final Function1<Integer, Integer> frac60 = frac.bind1(60);
    static final Function1<Integer, Integer> frac5 = frac.bind2(5);
    static final Function1<Integer, Function1<Integer, Integer> > curryFrac = frac.curry();

    @Test
    public void testApply() {
        assertEquals((Integer)4, sum.apply((Integer) 2, (Integer) 2));
        assertEquals(sum.apply((Integer)2, (Integer)2), sum.apply((Integer) 1, (Integer) 3));
        assertEquals(mult.apply((Integer)3, (Integer)5), sum.apply((Integer) 1, (Integer) 14));
    }

    @Test
    public void testBind1() {
        assertEquals((Integer)12, frac60.apply(5));
    }

    @Test
    public void testBind2() {
        assertEquals((Integer)12, frac5.apply(60));
    }

    @Test
    public void testCurry() {
        assertEquals(frac60.apply(12), curryFrac.apply(60).apply(12));
    }

    @Test
    public void testCompose() {
        assertEquals((Integer) 144, mult.compose(frac5).apply(144, 5));
    }

    @Test
    public void testGenerics() {
        assertEquals(1, (int)mult.compose(g).apply(3, 5));
    }
}