package java.util;

import java.util.function.UnaryOperator;

public interface List<E> extends Collection<E> {
    int size();

    boolean isEmpty();

    boolean contains(Object o);

    Iterator<E> iterator();

    Object[] toArray();

    <T> T[] toArray(T[] a);

    boolean add(E e);

    boolean remove(Object o);

    boolean containsAll(Collection<?> c);

    boolean addAll(Collection<? extends E> c);

    boolean addAll(int index, Collection<? extends E> c);

    boolean removeAll(Collection<?> c);

    boolean retainAll(Collection<?> c);

    default void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        final ListIterator<E> li = this.listIterator();
        while (li.hasNext()) {
            li.set(operator.apply(li.next()));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default void sort(Comparator<? super E> c) {
        Object[] a = this.toArray();
        Arrays.sort(a, (Comparator)c);
        ListIterator<E> i = this.listIterator();
        for(Object e : a) {
            i.next();
            i.set((E) e);
        }
    }

    void clear();

    boolean equals(Object o);

    int hashCode();

    E get(int index);

    E set(int index, E element);

    void add(int index, E element);

    E remove(int index);

    int indexOf(Object o);

    int lastIndexOf(Object o);

    ListIterator<E> listIterator();

    ListIterator<E> listIterator(int index);

    List<E> subList(int fromIndex, int toIndex);

    @Override
    default Spliterator<E> spliterator() {
        if(this instanceof RandomAccess)
            return new AbstractList.RandomAccessSpliterator<>(this);
        else
            return Spliterators.spliterator(this, Spliterator.ORDERED);
    }

    default void addFirst(E e) {
        this.add(0, e);
    }

    default void addLast(E e) {
        this.add(e);
    }

    default E getFirst() {
        if(this.isEmpty())
            throw new NoSuchElementException();
        else
            return this.get(0);
    }

    default E getLast() {
        if(this.isEmpty())
            throw new NoSuchElementException();
        else
            return this.get(this.size() - 1);
    }

    default E removeFirst() {
        if(this.isEmpty())
            throw new NoSuchElementException();
        else
            return this.remove(0);
    }

    default E removeLast() {
        if(this.isEmpty())
            throw new NoSuchElementException();
        else 
            return this.remove(this.size() - 1);
    }

    default List<E> reversed() {
        return ReverseOrderListView.of(this, true);
    }

    // TODO
    // @SuppressWarnings("unchecked")
    // static <E> List<E> of() {
    //     return (List<E>) ImmutableCollections.EMPTY_LIST;
    // }

    // TODO
    // static <E> List<E> of(E e1) {
    //     return new ImmutableCollections.List12<>(e1);
    // }

    // TODO
    // static <E> List<E> of(E e1, E e2) {
    //     return new ImmutableCollections.List12<>(e1, e2);
    // }

    // TODO
    // static <E> List<E> of(E e1, E e2, E e3) {
    //     return ImmutableCollections.listFromTrustedArray(e1, e2, e3);
    // }

    // TODO
    // static <E> List<E> of(E e1, E e2, E e3, E e4) {
    //     return ImmutableCollections.listFromTrustedArray(e1, e2, e3, e4);
    // }

    // TODO
    // static <E> List<E> of(E e1, E e2, E e3, E e4, E e5) {
    //     return ImmutableCollections.listFromTrustedArray(e1, e2, e3, e4, e5);
    // }

    // TODO
    // static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
    //     return ImmutableCollections.listFromTrustedArray(e1, e2, e3, e4, e5, e6);
    // }

    // TODO
    // static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
    //     return ImmutableCollections.listFromTrustedArray(e1, e2, e3, e4, e5, e6, e7);
    // }

    // TODO
    // static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
    //     return ImmutableCollections.listFromTrustedArray(e1, e2, e3, e4, e5, e6, e7, e8);
    // }

    // TODO
    // static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
    //     return ImmutableCollections.listFromTrustedArray(e1, e2, e3, e4, e5, e6, e7, e8, e9);
    // }

    // TODO
    // static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
    //     return ImmutableCollections.listFromTrustedArray(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);
    // }

    // TODO
    // @SafeVarargs
    // @SuppressWarnings("varargs")
    // static <E> List<E> of(E... elements) {
    //     switch (elements.length) { // implicit null check of elements
    //         case 0:
    //             @SuppressWarnings("unchecked")
    //             var list = (List<E>) ImmutableCollections.EMPTY_LIST;
    //             return list;
    //         case 1:
    //             return new ImmutableCollections.List12<>(elements[0]);
    //         case 2:
    //             return new ImmutableCollections.List12<>(elements[0], elements[1]);
    //         default:
    //             return ImmutableCollections.listFromArray(elements);
    //     }
    // }

    // TODO
    // static <E> List<E> copyOf(Collection<? extends E> coll) {
    //     return ImmutableCollections.listCopy(coll);
    // }
}
