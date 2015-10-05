package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class PredicateTest {
    static final Predicate<Integer> isZero = new Predicate<Integer>() {
        public Boolean apply(Integer x) {
            return x == 0;
        }
    };

    static final Predicate<Integer> isOdd = new Predicate<Integer>() {
        public Boolean apply(Integer x) {
            return x % 2 == 1;
        }
    };

    static final Predicate<Character> isCapital = new Predicate<Character>() {
        public Boolean apply(Character x) {
            return (x >= 'A') && (x <= 'Z');
        }
    };

    static final Predicate<Integer> badPredicate = new Predicate<Integer>() {
        public Boolean apply(Integer x) {
            if(x >= 0) {
                throw new RuntimeException();
            }
            return true;
        }
    };

    @Test
    public void testApply() {
        assertTrue(isZero.apply(0));
        assertFalse(isZero.apply(1));
        assertFalse(isZero.apply(-1));
    }

    @Test
    public void testOr() {
        assertTrue(isZero.or(isOdd).apply(5));
        assertTrue(isOdd.or(isZero).apply(5));
        assertFalse(isZero.or(isOdd).apply(6));
        assertFalse(isOdd.or(isZero).apply(6));
        assertTrue(isZero.or(isOdd).apply(0));
        assertTrue(isOdd.or(isZero).apply(0));

        assertTrue(isOdd.or(badPredicate).apply(5));
    }

    @Test
    public void testAnd() {
        assertFalse(isZero.and(isOdd).apply(5));
        assertFalse(isOdd.and(isZero).apply(5));
        assertFalse(isZero.and(isOdd).apply(6));
        assertFalse(isOdd.and(isZero).apply(6));
        assertFalse(isZero.and(isOdd).apply(0));
        assertFalse(isOdd.and(isZero).apply(0));
        assertTrue(isZero.and(isOdd.not()).apply(0));
        assertTrue(isOdd.not().and(isZero).apply(0));

        assertFalse(isOdd.and(badPredicate).apply(6));
    }

    @Test
    public void testNot() {
        assertTrue(isZero.not().not().apply(0));
        assertTrue(isZero.not().apply(7));
        assertFalse(isZero.not().not().apply(7));
    }

    @Test
    public void testALWAYS_TRUE() {
        assertTrue(isZero.ALWAYS_TRUE.apply('a'));
        assertTrue(isZero.ALWAYS_TRUE.apply(5));
        assertTrue(isCapital.ALWAYS_TRUE.apply(null));
        assertTrue(isCapital.ALWAYS_TRUE.apply(false));
    }

    @Test
    public void testALWAYS_FALSE() {
        assertFalse(isZero.ALWAYS_FALSE.apply('a'));
        assertFalse(isZero.ALWAYS_FALSE.apply(5));
        assertFalse(isCapital.ALWAYS_FALSE.apply(null));
        assertFalse(isCapital.ALWAYS_FALSE.apply(false));
    }
}