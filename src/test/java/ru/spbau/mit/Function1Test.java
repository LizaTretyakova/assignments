package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class Function1Test {
    static final Function1<Integer, Integer> fact = new Function1<Integer, Integer>() {
        public Integer apply(Integer x) {
            int result = 1;
            for (int i = 1; i <= x; i++) {
                result *= i;
            }
            return result;
        }
    };

    static final Function1<Integer, Integer> square = new Function1<Integer, Integer>() {
        public Integer apply(Integer x) {
            return x * x;
        }
    };

    @Test
    public void testApply() {
        assertEquals((Integer)6, fact.apply(3));
        assertEquals((Integer)720, fact.apply(6));
    }

    @Test
    public void testCompose() {
        assertEquals((Integer)36, fact.compose(square).apply(3));
    }
}