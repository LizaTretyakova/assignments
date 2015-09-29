package ru.spbau.mit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Collections {
    public static <T, R> Iterable<R> map(final Function1<? super T, R> f, final Iterable<T> collection) {
        List<R> result = new ArrayList<R>();
        for (T object : collection) {
            result.add(f.apply(object));
        }
        return result;
    }

    public static <T> Iterable<T> filter(final Predicate<? super T> p, final Iterable<T> collection) {
        List<T> result = new ArrayList<T>();
        Iterator<T> it = collection.iterator();
        while(it.hasNext()) {
            T object = it.next();
            if(p.apply(object)) {
                result.add(object);
            }
        }
        return result;
    }

    public static <T> Iterable<T> takeWhile(final Predicate<? super T> p, final Iterable<T> collection) {
        List<T> result = new ArrayList<T>();
        Iterator<T> it = collection.iterator();
        while(it.hasNext()) {
            T object = it.next();
            if(!p.apply(object)) {
                return result;
            }
            result.add(object);
        }
        return result;
    }

    public static <T> Iterable<T> takeUnless(final Predicate<? super T> p, final Iterable<T> collection) {
        List<T> result = new ArrayList<T>();
        Iterator<T> it = collection.iterator();
        while(it.hasNext()) {
            T object =it.next();
            if(p.apply(object)) {
                return result;
            }
            result.add(object);
        }
        return result;
    }

    public static <T, R> R foldl(final Function2<? super R, ? super T, R> f, final R start, final Iterable<T> collection) {
        R result = start;
        Iterator<T> it = collection.iterator();
        while(it.hasNext()) {
            T object = it.next();
            result = f.apply(result, object);
        }
        return result;
    }

    private static <T, R> R foldrWithIterator(final Function2<? super T, ? super R, R> f, final R start, final Iterator<T> it) {
        if (it.hasNext()) {
            return f.apply(it.next(), foldrWithIterator(f, start, it));
        }
        return start;
    }

    public static <T, R> R foldr(final Function2<? super T, ? super R, R> f, final R start, final Iterable<T> collection) {
        return foldrWithIterator(f, start, collection.iterator());
    }
}
