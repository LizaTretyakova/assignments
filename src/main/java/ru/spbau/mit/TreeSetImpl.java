package ru.spbau.mit;

import java.util.*;

public class TreeSetImpl<E> extends AbstractSet<E> {
    private Node<E> head;
    private int size;
    private Comparator<E> comp;

    public void print(Node<E> node, int i) {
        if(node == null) {
            for(int j = 0; j < i; j++) {
                System.out.print(" ");
            }
            System.out.println("null");
            return;
        }
        print(node.right, i + 1);
        for(int j = 0; j < i; j++) {
            System.out.print(" ");
        }
        System.out.print("(");
        System.out.print(node.value);
        System.out.print(", ");
        System.out.print(node.key);
        System.out.println(")");
        print(node.left, i + 1);
    }

    public void printHead() {
        print(head, 0);
    }

    public TreeSetImpl(Comparator<E> comparator) {
        head = null;
        size = 0;
        comp = comparator;
    }

    public boolean add(E x) {
        if (head == null) {
            head = new Node<E>(x);
            size++;
            return true;
        }
        if(contains(x)) {
            return false;
        }
        Pair<Node<E>, Node<E>> tmp = split(head, x);
        head = merge(merge(tmp.first, new Node(x)), tmp.second);
        if(head != null) {
            head.prev = null;
        }
        size++;
        return true;
    }

    private Node<E> findEl(E el) {
        if (head == null) {
            return null;
        }

        Node<E> node = head;
        while (node != null && comp.compare(node.value, el) != 0) {
            if (comp.compare(el, node.value) > 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return node;
    }

    public boolean remove(Object element) {
        Node<E> node = findEl((E)element);
        if(head == null || node == null) {
            return false;
        }

        Node<E> nextNode = next(node);
        if(nextNode == null) {
            head = split(head, node.value).first;
            if(head != null) {
                head.prev = null;
            }
        }
        else {
            Pair<Node<E>, Node<E>> tmp = split(head, node.value);
            tmp.second = split(tmp.second, nextNode.value).second;
            head = merge(tmp.first, tmp.second);
            if(head != null) {
                head.prev = null;
            }
        }
        size--;
        return true;
    }

    public boolean contains(Object element) {
        return findEl((E)element) != null;
    }

    @Override
    public int size() {
        return size;
    }

    private static class Node<T> {
        public T value;
        private static Random rand = new Random();
        public Integer key;
        public Node<T> left;
        public Node<T> right;
        public Node<T> prev;

        public Node(T val) {
            left = null;
            right = null;
            prev = null;
            value = val;
            key = rand.nextInt();
        }
    }

    public Iterator iterator() {
        return new Iterator(leftmost());
    }

    private class Iterator implements java.util.Iterator {
        public Node<E> consequent;
        public Node<E> underlying;
        private boolean removed = false;

        public Iterator(Node<E> v) {
            underlying = null;
            consequent = v;
        }

        public boolean hasNext() {
            return consequent != null;
        }

        public E next() throws NoSuchElementException {
            if(consequent == null) {
                throw new NoSuchElementException();
            }
            E result = consequent.value;
            underlying = consequent;
            consequent = TreeSetImpl.this.next(consequent);
            removed = false;
            return result;
        }

        public void remove() throws IllegalStateException {
            if(removed || underlying == null) {
                throw new IllegalStateException();
            }
            TreeSetImpl.this.remove(underlying.value);
            removed = true;
            underlying = null;
        }
    }

    private Node<E> leftmost() {
        if(head == null) {
            return null;
        }
        Node<E> node = head;
        while(node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node<E> next(Node<E> val) {
        if(val == null) {
            return null;
        }
        if(val.right != null) {
            Node<E> result = val;
            Node<E> node = val.right;

            while(node.left != null) {
                node = node.left;
            }
            return node;
        }

        Node<E> result = val;
        Node<E> node = val;
        while(node.prev != null) {
            if(node.prev.left != null && node.prev.left.equals(node)) {
                return node.prev;
            }
            node = node.prev;
        }
        return null;
    }

    private Node<E> merge(Node<E> t1, Node<E> t2) {
        if(t1 == null) {
            return t2;
        }
        if(t2 == null) {
            return t1;
        }
        if(t1.key < t2.key) {
            t2.left = merge(t1, t2.left);
            if(t2.left != null) {
                t2.left.prev = t2;
            }
            return t2;
        }
        t1.right = merge(t1.right, t2);
        if(t1.right != null) {
            t1.right.prev = t1;
        }
        return t1;
    }

    private class Pair<T1, T2> {
        public T1 first;
        public T2 second;

        public Pair(T1 f, T2 s) {
            first = f;
            second = s;
        }
    }

    private Pair<Node<E>, Node<E>> split(Node<E> t, E k) {
        if(t == null) {
            return new Pair(null, null);
        }
        if(TreeSetImpl.this.comp.compare(k, t.value) <= 0) {
            Pair<Node<E>, Node<E>> tmp = split(t.left, k);
            t.left = tmp.second;
            if(t.left != null) {
                t.left.prev = t;
            }
            tmp.second = t;
            return tmp;
        }
        Pair<Node<E>, Node<E>> tmp = split(t.right, k);
        t.right = tmp.first;
        if(t.right != null) {
            t.right.prev = t;
        }
        tmp.first = t;
        return tmp;
    }
}
