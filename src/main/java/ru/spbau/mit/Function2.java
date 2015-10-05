package ru.spbau.mit;

public abstract class Function2<T1, T2, R> {
    public abstract R apply(final T1 x, final T2 y);

    public Function1<T2, R> bind1(final T1 x) {
        return new Function1<T2, R> () {
            public R apply(final T2 y) {
                return Function2.this.apply(x, y);
            }
        };
    }

    public Function1<T1, R> bind2(final T2 y) {
        return new Function1<T1, R> () {
            public R apply(final T1 x) {
                return Function2.this.apply(x, y);
            }
        };
    }

    public <E> Function2<T1, T2, E> compose(final Function1<? super R, E> g) {
        return new Function2<T1, T2, E> () {
            public E apply(final T1 x, final T2 y) {
                return g.apply(Function2.this.apply(x, y));
            }
        };
    }

    public Function1<T1, Function1<T2, R>> curry() {
        return new Function1<T1, Function1<T2, R>> () {
            public Function1<T2, R> apply(final T1 x) {
                return Function2.this.bind1(x);
            }
        };
    }
}
