package java.util;

import java.util.function.Consumer;
import java.util.function.IntFunction;
// import java.util.stream.Stream;
// import java.util.stream.StreamSupport;
import jdk.internal.util.ArraysSupport;

class ReverseOrderDequeView<E> implements Deque<E> {
    final Deque<E> base;

    private ReverseOrderDequeView(Deque<E> deque) {
        base = deque;
    }

    public static <T> Deque<T> of(Deque<T> deque) {
        if(deque instanceof ReverseOrderDequeView<T> rodv)
            return rodv.base;
        else
            return new ReverseOrderDequeView<>(deque);
    }

    public void forEach(Consumer<? super E> action) {
        for(E e : this)
            action.accept(e);
    }

    public Iterator<E> iterator() {
        return base.descendingIterator();
    }

    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.ORDERED);
    }

    public boolean add(E e) {
        base.addFirst(e);
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for(E e : c) {
            base.addFirst(e);
            modified = true;
        }
        return modified;
    }

    public void clear() {
        base.clear();
    }

    public boolean contains(Object o) {
        return base.contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return base.containsAll(c);
    }

    public boolean isEmpty() {
        return base.isEmpty();
    }
    public boolean remove(Object o) {
        Iterator<E> it = iterator();
        if(o==null) {
            while(it.hasNext()) {
                if(it.next()==null) {
                    it.remove();
                    return true;
                }
            }
        }
        else {
            while(it.hasNext()) {
                if(o.equals(it.next())) {
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<?> it = iterator();
        while(it.hasNext()) {
            if(c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> it = iterator();
        while(it.hasNext()) {
            if(!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    public int size() {
        return base.size();
    }

    // TODO
    // public Stream<E> stream() {
    //     return StreamSupport.stream(spliterator(), false);
    // }

    // TODO
    // public Stream<E> parallelStream() {
    //     return StreamSupport.stream(spliterator(), true);
    // }

    public Object[] toArray() {
        return ArraysSupport.reverse(base.toArray());
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        return ArraysSupport.toArrayReversed(base, a);
    }

    public <T> T[] toArray(IntFunction<T[]> generator) {
        return ArraysSupport.reverse(base.toArray(generator));
    }

    public String toString() {
        Iterator<E> it = iterator();
        if(! it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if(! it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }

    public void addFirst(E e) {
        base.addLast(e);
    }

    public void addLast(E e) {
        base.addFirst(e);
    }

    public Iterator<E> descendingIterator() {
        return base.iterator();
    }

    public E element() {
        return base.getLast();
    }

    public E getFirst() {
        return base.getLast();
    }

    public E getLast() {
        return base.getFirst();
    }

    public boolean offer(E e) {
        return base.offerFirst(e);
    }

    public boolean offerFirst(E e) {
        return base.offerLast(e);
    }

    public boolean offerLast(E e) {
        return base.offerFirst(e);
    }

    public E peek() {
        return base.peekLast();
    }

    public E peekFirst() {
        return base.peekLast();
    }

    public E peekLast() {
        return base.peekFirst();
    }

    public E poll() {
        return base.pollLast();
    }

    public E pollFirst() {
        return base.pollLast();
    }

    public E pollLast() {
        return base.pollFirst();
    }

    public E pop() {
        return base.removeLast();
    }

    public void push(E e) {
        base.addLast(e);
    }

    public E remove() {
        return base.removeLast();
    }

    public E removeFirst() {
        return base.removeLast();
    }

    public E removeLast() {
        return base.removeFirst();
    }

    public boolean removeFirstOccurrence(Object o) {
        return base.removeLastOccurrence(o);
    }

    public boolean removeLastOccurrence(Object o) {
        return base.removeFirstOccurrence(o);
    }
}
