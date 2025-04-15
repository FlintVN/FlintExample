package java.util;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public final class Spliterators {
    private Spliterators() {}

    @SuppressWarnings("unchecked")
    public static <T> Spliterator<T> emptySpliterator() {
        return (Spliterator<T>) EMPTY_SPLITERATOR;
    }

    private static final Spliterator<Object> EMPTY_SPLITERATOR = new EmptySpliterator.OfRef<>();

    public static Spliterator.OfInt emptyIntSpliterator() {
        return EMPTY_INT_SPLITERATOR;
    }

    private static final Spliterator.OfInt EMPTY_INT_SPLITERATOR = new EmptySpliterator.OfInt();

    public static Spliterator.OfLong emptyLongSpliterator() {
        return EMPTY_LONG_SPLITERATOR;
    }

    private static final Spliterator.OfLong EMPTY_LONG_SPLITERATOR = new EmptySpliterator.OfLong();

    public static Spliterator.OfDouble emptyDoubleSpliterator() {
        return EMPTY_DOUBLE_SPLITERATOR;
    }

    private static final Spliterator.OfDouble EMPTY_DOUBLE_SPLITERATOR = new EmptySpliterator.OfDouble();

    public static <T> Spliterator<T> spliterator(Object[] array, int additionalCharacteristics) {
        return new ArraySpliterator<>(Objects.requireNonNull(array), additionalCharacteristics);
    }

    public static <T> Spliterator<T> spliterator(Object[] array, int fromIndex, int toIndex, int additionalCharacteristics) {
        checkFromToBounds(Objects.requireNonNull(array).length, fromIndex, toIndex);
        return new ArraySpliterator<>(array, fromIndex, toIndex, additionalCharacteristics);
    }

    public static Spliterator.OfInt spliterator(int[] array, int additionalCharacteristics) {
        return new IntArraySpliterator(Objects.requireNonNull(array), additionalCharacteristics);
    }

    public static Spliterator.OfInt spliterator(int[] array, int fromIndex, int toIndex, int additionalCharacteristics) {
        checkFromToBounds(Objects.requireNonNull(array).length, fromIndex, toIndex);
        return new IntArraySpliterator(array, fromIndex, toIndex, additionalCharacteristics);
    }

    public static Spliterator.OfLong spliterator(long[] array, int additionalCharacteristics) {
        return new LongArraySpliterator(Objects.requireNonNull(array), additionalCharacteristics);
    }

    public static Spliterator.OfLong spliterator(long[] array, int fromIndex, int toIndex, int additionalCharacteristics) {
        checkFromToBounds(Objects.requireNonNull(array).length, fromIndex, toIndex);
        return new LongArraySpliterator(array, fromIndex, toIndex, additionalCharacteristics);
    }

    public static Spliterator.OfDouble spliterator(double[] array, int additionalCharacteristics) {
        return new DoubleArraySpliterator(Objects.requireNonNull(array), additionalCharacteristics);
    }

    public static Spliterator.OfDouble spliterator(double[] array, int fromIndex, int toIndex, int additionalCharacteristics) {
        checkFromToBounds(Objects.requireNonNull(array).length, fromIndex, toIndex);
        return new DoubleArraySpliterator(array, fromIndex, toIndex, additionalCharacteristics);
    }

    private static void checkFromToBounds(int arrayLength, int origin, int fence) {
        if(origin > fence)
            throw new ArrayIndexOutOfBoundsException("origin(" + origin + ") > fence(" + fence + ")");
        if(origin < 0)
            throw new ArrayIndexOutOfBoundsException(origin);
        if(fence > arrayLength)
            throw new ArrayIndexOutOfBoundsException(fence);
    }

    public static <T> Spliterator<T> spliterator(Collection<? extends T> c, int characteristics) {
        return new IteratorSpliterator<>(Objects.requireNonNull(c), characteristics);
    }

    public static <T> Spliterator<T> spliterator(Iterator<? extends T> iterator, long size, int characteristics) {
        return new IteratorSpliterator<>(Objects.requireNonNull(iterator), size, characteristics);
    }

    public static <T> Spliterator<T> spliteratorUnknownSize(Iterator<? extends T> iterator, int characteristics) {
        return new IteratorSpliterator<>(Objects.requireNonNull(iterator), characteristics);
    }

    public static Spliterator.OfInt spliterator(PrimitiveIterator.OfInt iterator, long size, int characteristics) {
        return new IntIteratorSpliterator(Objects.requireNonNull(iterator), size, characteristics);
    }

    public static Spliterator.OfInt spliteratorUnknownSize(PrimitiveIterator.OfInt iterator, int characteristics) {
        return new IntIteratorSpliterator(Objects.requireNonNull(iterator), characteristics);
    }

    public static Spliterator.OfLong spliterator(PrimitiveIterator.OfLong iterator, long size, int characteristics) {
        return new LongIteratorSpliterator(Objects.requireNonNull(iterator), size, characteristics);
    }

    public static Spliterator.OfLong spliteratorUnknownSize(PrimitiveIterator.OfLong iterator, int characteristics) {
        return new LongIteratorSpliterator(Objects.requireNonNull(iterator), characteristics);
    }

    public static Spliterator.OfDouble spliterator(PrimitiveIterator.OfDouble iterator, long size, int characteristics) {
        return new DoubleIteratorSpliterator(Objects.requireNonNull(iterator), size, characteristics);
    }

    public static Spliterator.OfDouble spliteratorUnknownSize(PrimitiveIterator.OfDouble iterator, int characteristics) {
        return new DoubleIteratorSpliterator(Objects.requireNonNull(iterator), characteristics);
    }

    public static<T> Iterator<T> iterator(Spliterator<? extends T> spliterator) {
        Objects.requireNonNull(spliterator);
        class Adapter implements Iterator<T>, Consumer<T> {
            boolean valueReady = false;
            T nextElement;

            @Override
            public void accept(T t) {
                valueReady = true;
                nextElement = t;
            }

            @Override
            public boolean hasNext() {
                if(!valueReady)
                    spliterator.tryAdvance(this);
                return valueReady;
            }

            @Override
            public T next() {
                if(!valueReady && !hasNext())
                    throw new NoSuchElementException();
                else {
                    valueReady = false;
                    T t = nextElement;
                    nextElement = null;
                    return t;
                }
            }

            @Override
            public void forEachRemaining(Consumer<? super T> action) {
                Objects.requireNonNull(action);
                if(valueReady) {
                    valueReady = false;
                    T t = nextElement;
                    nextElement = null;
                    action.accept(t);
                }
                spliterator.forEachRemaining(action);
            }
        }

        return new Adapter();
    }

    public static PrimitiveIterator.OfInt iterator(Spliterator.OfInt spliterator) {
        Objects.requireNonNull(spliterator);
        class Adapter implements PrimitiveIterator.OfInt, IntConsumer {
            boolean valueReady = false;
            int nextElement;

            @Override
            public void accept(int t) {
                valueReady = true;
                nextElement = t;
            }

            @Override
            public boolean hasNext() {
                if(!valueReady)
                    spliterator.tryAdvance(this);
                return valueReady;
            }

            @Override
            public int nextInt() {
                if(!valueReady && !hasNext())
                    throw new NoSuchElementException();
                else {
                    valueReady = false;
                    return nextElement;
                }
            }

            @Override
            public void forEachRemaining(IntConsumer action) {
                Objects.requireNonNull(action);
                if(valueReady) {
                    valueReady = false;
                    action.accept(nextElement);
                }
                spliterator.forEachRemaining(action);
            }
        }

        return new Adapter();
    }

    public static PrimitiveIterator.OfLong iterator(Spliterator.OfLong spliterator) {
        Objects.requireNonNull(spliterator);
        class Adapter implements PrimitiveIterator.OfLong, LongConsumer {
            boolean valueReady = false;
            long nextElement;

            @Override
            public void accept(long t) {
                valueReady = true;
                nextElement = t;
            }

            @Override
            public boolean hasNext() {
                if(!valueReady)
                    spliterator.tryAdvance(this);
                return valueReady;
            }

            @Override
            public long nextLong() {
                if(!valueReady && !hasNext())
                    throw new NoSuchElementException();
                else {
                    valueReady = false;
                    return nextElement;
                }
            }

            @Override
            public void forEachRemaining(LongConsumer action) {
                Objects.requireNonNull(action);
                if(valueReady) {
                    valueReady = false;
                    action.accept(nextElement);
                }
                spliterator.forEachRemaining(action);
            }
        }

        return new Adapter();
    }

    public static PrimitiveIterator.OfDouble iterator(Spliterator.OfDouble spliterator) {
        Objects.requireNonNull(spliterator);
        class Adapter implements PrimitiveIterator.OfDouble, DoubleConsumer {
            boolean valueReady = false;
            double nextElement;

            @Override
            public void accept(double t) {
                valueReady = true;
                nextElement = t;
            }

            @Override
            public boolean hasNext() {
                if(!valueReady)
                    spliterator.tryAdvance(this);
                return valueReady;
            }

            @Override
            public double nextDouble() {
                if(!valueReady && !hasNext())
                    throw new NoSuchElementException();
                else {
                    valueReady = false;
                    return nextElement;
                }
            }

            @Override
            public void forEachRemaining(DoubleConsumer action) {
                Objects.requireNonNull(action);
                if(valueReady) {
                    valueReady = false;
                    action.accept(nextElement);
                }
                spliterator.forEachRemaining(action);
            }
        }

        return new Adapter();
    }

    private abstract static class EmptySpliterator<T, S extends Spliterator<T>, C> {
        EmptySpliterator() {

        }

        public S trySplit() {
            return null;
        }

        public boolean tryAdvance(C consumer) {
            Objects.requireNonNull(consumer);
            return false;
        }

        public void forEachRemaining(C consumer) {
            Objects.requireNonNull(consumer);
        }

        public long estimateSize() {
            return 0;
        }

        public int characteristics() {
            return Spliterator.SIZED | Spliterator.SUBSIZED;
        }

        private static final class OfRef<T> extends EmptySpliterator<T, Spliterator<T>, Consumer<? super T>> implements Spliterator<T> {
            OfRef() { }
        }

        @SuppressWarnings("overloads")
        private static final class OfInt extends EmptySpliterator<Integer, Spliterator.OfInt, IntConsumer> implements Spliterator.OfInt {
            OfInt() { }
        }

        @SuppressWarnings("overloads")
        private static final class OfLong extends EmptySpliterator<Long, Spliterator.OfLong, LongConsumer> implements Spliterator.OfLong {
            OfLong() { }
        }

        @SuppressWarnings("overloads")
        private static final class OfDouble extends EmptySpliterator<Double, Spliterator.OfDouble, DoubleConsumer> implements Spliterator.OfDouble {
            OfDouble() { }
        }
    }

    static final class ArraySpliterator<T> implements Spliterator<T> {
        private final Object[] array;
        private int index;
        private final int fence;
        private final int characteristics;
        private long estimatedSize;

        public ArraySpliterator(Object[] array, int additionalCharacteristics) {
            this(array, 0, array.length, additionalCharacteristics);
        }

        public ArraySpliterator(Object[] array, int origin, int fence, int additionalCharacteristics) {
            this.array = array;
            this.index = origin;
            this.fence = fence;
            this.characteristics = additionalCharacteristics | Spliterator.SIZED | Spliterator.SUBSIZED;
            this.estimatedSize = -1;
        }

        private ArraySpliterator(Object[] array, int origin, int fence, int characteristics, long estimatedSize) {
            this.array = array;
            this.index = origin;
            this.fence = fence;
            this.characteristics = characteristics & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
            this.estimatedSize = estimatedSize;
        }

        @Override
        public Spliterator<T> trySplit() {
            int lo = index, mid = (lo + fence) >>> 1;
            if(lo >= mid) return null;
            if(estimatedSize == -1) {
                return new ArraySpliterator<>(array, lo, index = mid, characteristics);
            }
            long prefixEstimatedSize = estimatedSize >>> 1;
            estimatedSize -= prefixEstimatedSize;
            return new ArraySpliterator<>(array, lo, index = mid, characteristics, prefixEstimatedSize);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            Object[] a; int i, hi;
            if(action == null)
                throw new NullPointerException();
            if((a = array).length >= (hi = fence) &&
                (i = index) >= 0 && i < (index = hi)) {
                do {
                    action.accept((T)a[i]);
                } while(++i < hi);
            }
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if(action == null)
                throw new NullPointerException();
            if(index >= 0 && index < fence) {
                @SuppressWarnings("unchecked") T e = (T) array[index++];
                action.accept(e);
                return true;
            }
            return false;
        }

        @Override
        public long estimateSize() {
            return estimatedSize >= 0 ? estimatedSize : (long)(fence - index);
        }

        @Override
        public int characteristics() {
            return characteristics;
        }

        @Override
        public Comparator<? super T> getComparator() {
            if(hasCharacteristics(Spliterator.SORTED))
                return null;
            throw new IllegalStateException();
        }
    }

    static final class IntArraySpliterator implements Spliterator.OfInt {
        private final int[] array;
        private int index;
        private final int fence;
        private final int characteristics;
        private long estimatedSize;

        public IntArraySpliterator(int[] array, int additionalCharacteristics) {
            this(array, 0, array.length, additionalCharacteristics);
        }

        public IntArraySpliterator(int[] array, int origin, int fence, int additionalCharacteristics) {
            this.array = array;
            this.index = origin;
            this.fence = fence;
            this.characteristics = additionalCharacteristics | Spliterator.SIZED | Spliterator.SUBSIZED;
            this.estimatedSize = -1;
        }

        private IntArraySpliterator(int[] array, int origin, int fence, int characteristics, long estimatedSize) {
            this.array = array;
            this.index = origin;
            this.fence = fence;
            this.characteristics = characteristics & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
            this.estimatedSize = estimatedSize;
        }

        @Override
        public OfInt trySplit() {
            int lo = index, mid = (lo + fence) >>> 1;
            if(lo >= mid)
                return null;
            if(estimatedSize == -1)
                return new IntArraySpliterator(array, lo, index = mid, characteristics);
            long prefixEstimatedSize = estimatedSize >>> 1;
            estimatedSize -= prefixEstimatedSize;
            return new IntArraySpliterator(array, lo, index = mid, characteristics, prefixEstimatedSize);
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            int[] a; int i, hi;
            if(action == null)
                throw new NullPointerException();
            if((a = array).length >= (hi = fence) &&
                (i = index) >= 0 && i < (index = hi)) {
                do {
                    action.accept(a[i]);
                } while(++i < hi);
            }
        }

        @Override
        public boolean tryAdvance(IntConsumer action) {
            if(action == null)
                throw new NullPointerException();
            if(index >= 0 && index < fence) {
                action.accept(array[index++]);
                return true;
            }
            return false;
        }

        @Override
        public long estimateSize() {
            return estimatedSize >= 0 ? estimatedSize : (long)(fence - index);
        }

        @Override
        public int characteristics() {
            return characteristics;
        }

        @Override
        public Comparator<? super Integer> getComparator() {
            if(hasCharacteristics(Spliterator.SORTED))
                return null;
            throw new IllegalStateException();
        }
    }

    static final class LongArraySpliterator implements Spliterator.OfLong {
        private final long[] array;
        private int index;
        private final int fence;
        private final int characteristics;
        private long estimatedSize;

        public LongArraySpliterator(long[] array, int additionalCharacteristics) {
            this(array, 0, array.length, additionalCharacteristics);
        }

        public LongArraySpliterator(long[] array, int origin, int fence, int additionalCharacteristics) {
            this.array = array;
            this.index = origin;
            this.fence = fence;
            this.characteristics = additionalCharacteristics | Spliterator.SIZED | Spliterator.SUBSIZED;
            this.estimatedSize = -1;
        }

        private LongArraySpliterator(long[] array, int origin, int fence, int characteristics, long estimatedSize) {
            this.array = array;
            this.index = origin;
            this.fence = fence;
            this.characteristics = characteristics & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
            this.estimatedSize = estimatedSize;
        }

        @Override
        public OfLong trySplit() {
            int lo = index, mid = (lo + fence) >>> 1;
            if(lo >= mid) return null;
            if(estimatedSize == -1) {
                return new LongArraySpliterator(array, lo, index = mid, characteristics);
            }
            long prefixEstimatedSize = estimatedSize >>> 1;
            estimatedSize -= prefixEstimatedSize;
            return new LongArraySpliterator(array, lo, index = mid, characteristics, prefixEstimatedSize);
        }

        @Override
        public void forEachRemaining(LongConsumer action) {
            long[] a; int i, hi;
            if(action == null)
                throw new NullPointerException();
            if((a = array).length >= (hi = fence) &&
                (i = index) >= 0 && i < (index = hi)) {
                do {
                    action.accept(a[i]);
                } while(++i < hi);
            }
        }

        @Override
        public boolean tryAdvance(LongConsumer action) {
            if(action == null)
                throw new NullPointerException();
            if(index >= 0 && index < fence) {
                action.accept(array[index++]);
                return true;
            }
            return false;
        }

        @Override
        public long estimateSize() {
            return estimatedSize >= 0 ? estimatedSize : (long)(fence - index);
        }

        @Override
        public int characteristics() {
            return characteristics;
        }

        @Override
        public Comparator<? super Long> getComparator() {
            if(hasCharacteristics(Spliterator.SORTED))
                return null;
            throw new IllegalStateException();
        }
    }

    static final class DoubleArraySpliterator implements Spliterator.OfDouble {
        private final double[] array;
        private int index;
        private final int fence;
        private final int characteristics;
        private long estimatedSize;

        public DoubleArraySpliterator(double[] array, int additionalCharacteristics) {
            this(array, 0, array.length, additionalCharacteristics);
        }

        public DoubleArraySpliterator(double[] array, int origin, int fence, int additionalCharacteristics) {
            this.array = array;
            this.index = origin;
            this.fence = fence;
            this.characteristics = additionalCharacteristics | Spliterator.SIZED | Spliterator.SUBSIZED;
            this.estimatedSize = -1;
        }

        private DoubleArraySpliterator(double[] array, int origin, int fence, int characteristics, long estimatedSize) {
            this.array = array;
            this.index = origin;
            this.fence = fence;
            this.characteristics = characteristics & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
            this.estimatedSize = estimatedSize;
        }

        @Override
        public OfDouble trySplit() {
            int lo = index, mid = (lo + fence) >>> 1;
            if(lo >= mid) return null;
            if(estimatedSize == -1) {
                return new DoubleArraySpliterator(array, lo, index = mid, characteristics);
            }
            long prefixEstimatedSize = estimatedSize >>> 1;
            estimatedSize -= prefixEstimatedSize;
            return new DoubleArraySpliterator(array, lo, index = mid, characteristics, prefixEstimatedSize);
        }

        @Override
        public void forEachRemaining(DoubleConsumer action) {
            double[] a; int i, hi;
            if(action == null)
                throw new NullPointerException();
            if((a = array).length >= (hi = fence) &&
                (i = index) >= 0 && i < (index = hi)) {
                do {
                    action.accept(a[i]);
                } while(++i < hi);
            }
        }

        @Override
        public boolean tryAdvance(DoubleConsumer action) {
            if(action == null)
                throw new NullPointerException();
            if(index >= 0 && index < fence) {
                action.accept(array[index++]);
                return true;
            }
            return false;
        }

        @Override
        public long estimateSize() {
            return estimatedSize >= 0 ? estimatedSize : (long)(fence - index);
        }

        @Override
        public int characteristics() {
            return characteristics;
        }

        @Override
        public Comparator<? super Double> getComparator() {
            if(hasCharacteristics(Spliterator.SORTED))
                return null;
            throw new IllegalStateException();
        }
    }

    public abstract static class AbstractSpliterator<T> implements Spliterator<T> {
        static final int BATCH_UNIT = 1 << 10;
        static final int MAX_BATCH = 1 << 25;
        private final int characteristics;
        private long est;
        private int batch;

        protected AbstractSpliterator(long est, int additionalCharacteristics) {
            this.est = est;
            this.characteristics = ((additionalCharacteristics & Spliterator.SIZED) != 0)
                                   ? additionalCharacteristics | Spliterator.SUBSIZED
                                   : additionalCharacteristics;
        }

        static final class HoldingConsumer<T> implements Consumer<T> {
            Object value;

            @Override
            public void accept(T value) {
                this.value = value;
            }
        }

        @Override
        public Spliterator<T> trySplit() {
            HoldingConsumer<T> holder = new HoldingConsumer<>();
            long s = est;
            if(s > 1 && tryAdvance(holder)) {
                int n = batch + BATCH_UNIT;
                if(n > s)
                    n = (int) s;
                if(n > MAX_BATCH)
                    n = MAX_BATCH;
                Object[] a = new Object[n];
                int j = 0;
                do {
                    a[j] = holder.value;
                } while(++j < n && tryAdvance(holder));
                batch = j;
                if(est != Long.MAX_VALUE) {
                    est -= j;
                    return new ArraySpliterator<>(a, 0, j, characteristics);
                }
                return new ArraySpliterator<>(a, 0, j, characteristics, Long.MAX_VALUE / 2);
            }
            return null;
        }

        @Override
        public long estimateSize() {
            return est;
        }

        @Override
        public int characteristics() {
            return characteristics;
        }
    }

    public abstract static class AbstractIntSpliterator implements Spliterator.OfInt {
        static final int MAX_BATCH = AbstractSpliterator.MAX_BATCH;
        static final int BATCH_UNIT = AbstractSpliterator.BATCH_UNIT;
        private final int characteristics;
        private long est;
        private int batch;

        protected AbstractIntSpliterator(long est, int additionalCharacteristics) {
            this.est = est;
            this.characteristics = ((additionalCharacteristics & Spliterator.SIZED) != 0)
                                   ? additionalCharacteristics | Spliterator.SUBSIZED
                                   : additionalCharacteristics;
        }

        static final class HoldingIntConsumer implements IntConsumer {
            int value;

            @Override
            public void accept(int value) {
                this.value = value;
            }
        }

        @Override
        public Spliterator.OfInt trySplit() {
            HoldingIntConsumer holder = new HoldingIntConsumer();
            long s = est;
            if(s > 1 && tryAdvance(holder)) {
                int n = batch + BATCH_UNIT;
                if(n > s)
                    n = (int) s;
                if(n > MAX_BATCH)
                    n = MAX_BATCH;
                int[] a = new int[n];
                int j = 0;
                do {
                    a[j] = holder.value;
                } while(++j < n && tryAdvance(holder));
                batch = j;
                if(est != Long.MAX_VALUE) {
                    est -= j;
                    return new IntArraySpliterator(a, 0, j, characteristics);
                }
                return new IntArraySpliterator(a, 0, j, characteristics, Long.MAX_VALUE / 2);
            }
            return null;
        }

        @Override
        public long estimateSize() {
            return est;
        }

        @Override
        public int characteristics() {
            return characteristics;
        }
    }

    public abstract static class AbstractLongSpliterator implements Spliterator.OfLong {
        static final int MAX_BATCH = AbstractSpliterator.MAX_BATCH;
        static final int BATCH_UNIT = AbstractSpliterator.BATCH_UNIT;
        private final int characteristics;
        private long est;
        private int batch;

        protected AbstractLongSpliterator(long est, int additionalCharacteristics) {
            this.est = est;
            this.characteristics = ((additionalCharacteristics & Spliterator.SIZED) != 0)
                                   ? additionalCharacteristics | Spliterator.SUBSIZED
                                   : additionalCharacteristics;
        }

        static final class HoldingLongConsumer implements LongConsumer {
            long value;

            @Override
            public void accept(long value) {
                this.value = value;
            }
        }

        @Override
        public Spliterator.OfLong trySplit() {
            HoldingLongConsumer holder = new HoldingLongConsumer();
            long s = est;
            if(s > 1 && tryAdvance(holder)) {
                int n = batch + BATCH_UNIT;
                if(n > s)
                    n = (int) s;
                if(n > MAX_BATCH)
                    n = MAX_BATCH;
                long[] a = new long[n];
                int j = 0;
                do {
                    a[j] = holder.value;
                } while(++j < n && tryAdvance(holder));
                batch = j;
                if(est != Long.MAX_VALUE) {
                    est -= j;
                    return new LongArraySpliterator(a, 0, j, characteristics);
                }
                return new LongArraySpliterator(a, 0, j, characteristics, Long.MAX_VALUE / 2);
            }
            return null;
        }

        @Override
        public long estimateSize() {
            return est;
        }

        @Override
        public int characteristics() {
            return characteristics;
        }
    }

    public abstract static class AbstractDoubleSpliterator implements Spliterator.OfDouble {
        static final int MAX_BATCH = AbstractSpliterator.MAX_BATCH;
        static final int BATCH_UNIT = AbstractSpliterator.BATCH_UNIT;
        private final int characteristics;
        private long est;
        private int batch;

        protected AbstractDoubleSpliterator(long est, int additionalCharacteristics) {
            this.est = est;
            this.characteristics = ((additionalCharacteristics & Spliterator.SIZED) != 0)
                                   ? additionalCharacteristics | Spliterator.SUBSIZED
                                   : additionalCharacteristics;
        }

        static final class HoldingDoubleConsumer implements DoubleConsumer {
            double value;

            @Override
            public void accept(double value) {
                this.value = value;
            }
        }

        @Override
        public Spliterator.OfDouble trySplit() {
            HoldingDoubleConsumer holder = new HoldingDoubleConsumer();
            long s = est;
            if(s > 1 && tryAdvance(holder)) {
                int n = batch + BATCH_UNIT;
                if(n > s)
                    n = (int) s;
                if(n > MAX_BATCH)
                    n = MAX_BATCH;
                double[] a = new double[n];
                int j = 0;
                do {
                    a[j] = holder.value;
                } while(++j < n && tryAdvance(holder));
                batch = j;
                if(est != Long.MAX_VALUE) {
                    est -= j;
                    return new DoubleArraySpliterator(a, 0, j, characteristics);
                }
                return new DoubleArraySpliterator(a, 0, j, characteristics, Long.MAX_VALUE / 2);
            }
            return null;
        }

        @Override
        public long estimateSize() {
            return est;
        }

        @Override
        public int characteristics() {
            return characteristics;
        }
    }

    static class IteratorSpliterator<T> implements Spliterator<T> {
        static final int BATCH_UNIT = 1 << 10;
        static final int MAX_BATCH = 1 << 25;
        private final Collection<? extends T> collection;
        private Iterator<? extends T> it;
        private final int characteristics;
        private long est;
        private int batch;

        public IteratorSpliterator(Collection<? extends T> collection, int characteristics) {
            this.collection = collection;
            this.it = null;
            this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
                                   ? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
                                   : characteristics;
        }

        public IteratorSpliterator(Iterator<? extends T> iterator, long size, int characteristics) {
            this.collection = null;
            this.it = iterator;
            this.est = size;
            this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
                                   ? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
                                   : characteristics;
        }

        public IteratorSpliterator(Iterator<? extends T> iterator, int characteristics) {
            this.collection = null;
            this.it = iterator;
            this.est = Long.MAX_VALUE;
            this.characteristics = characteristics & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
        }

        @Override
        public Spliterator<T> trySplit() {
            Iterator<? extends T> i;
            long s;
            if((i = it) == null) {
                i = it = collection.iterator();
                s = est = (long)collection.size();
            }
            else
                s = est;
            if(s > 1 && i.hasNext()) {
                int n = batch + BATCH_UNIT;
                if(n > s)
                    n = (int) s;
                if(n > MAX_BATCH)
                    n = MAX_BATCH;
                Object[] a = new Object[n];
                int j = 0;
                do {
                    a[j] = i.next();
                } while(++j < n && i.hasNext());
                batch = j;
                if(est != Long.MAX_VALUE) {
                    est -= j;
                    return new ArraySpliterator<>(a, 0, j, characteristics);
                }
                return new ArraySpliterator<>(a, 0, j, characteristics, Long.MAX_VALUE / 2);
            }
            return null;
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            if(action == null) throw new NullPointerException();
            Iterator<? extends T> i;
            if((i = it) == null) {
                i = it = collection.iterator();
                est = (long)collection.size();
            }
            i.forEachRemaining(action);
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if(action == null) throw new NullPointerException();
            if(it == null) {
                it = collection.iterator();
                est = (long)collection.size();
            }
            if(it.hasNext()) {
                action.accept(it.next());
                return true;
            }
            return false;
        }

        @Override
        public long estimateSize() {
            if(it == null) {
                it = collection.iterator();
                return est = (long)collection.size();
            }
            return est;
        }

        @Override
        public int characteristics() { return characteristics; }

        @Override
        public Comparator<? super T> getComparator() {
            if(hasCharacteristics(Spliterator.SORTED))
                return null;
            throw new IllegalStateException();
        }
    }

    static final class IntIteratorSpliterator implements Spliterator.OfInt {
        static final int BATCH_UNIT = IteratorSpliterator.BATCH_UNIT;
        static final int MAX_BATCH = IteratorSpliterator.MAX_BATCH;
        private final PrimitiveIterator.OfInt it;
        private final int characteristics;
        private long est;
        private int batch;

        public IntIteratorSpliterator(PrimitiveIterator.OfInt iterator, long size, int characteristics) {
            this.it = iterator;
            this.est = size;
            this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
                                   ? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
                                   : characteristics;
        }

        public IntIteratorSpliterator(PrimitiveIterator.OfInt iterator, int characteristics) {
            this.it = iterator;
            this.est = Long.MAX_VALUE;
            this.characteristics = characteristics & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
        }

        @Override
        public OfInt trySplit() {
            PrimitiveIterator.OfInt i = it;
            long s = est;
            if(s > 1 && i.hasNext()) {
                int n = batch + BATCH_UNIT;
                if(n > s)
                    n = (int) s;
                if(n > MAX_BATCH)
                    n = MAX_BATCH;
                int[] a = new int[n];
                int j = 0;
                do {
                    a[j] = i.nextInt();
                } while(++j < n && i.hasNext());
                batch = j;
                if(est != Long.MAX_VALUE) {
                    est -= j;
                    return new IntArraySpliterator(a, 0, j, characteristics);
                }
                return new IntArraySpliterator(a, 0, j, characteristics, Long.MAX_VALUE / 2);
            }
            return null;
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            if(action == null) throw new NullPointerException();
            it.forEachRemaining(action);
        }

        @Override
        public boolean tryAdvance(IntConsumer action) {
            if(action == null) throw new NullPointerException();
            if(it.hasNext()) {
                action.accept(it.nextInt());
                return true;
            }
            return false;
        }

        @Override
        public long estimateSize() {
            return est;
        }

        @Override
        public int characteristics() { return characteristics; }

        @Override
        public Comparator<? super Integer> getComparator() {
            if(hasCharacteristics(Spliterator.SORTED))
                return null;
            throw new IllegalStateException();
        }
    }

    static final class LongIteratorSpliterator implements Spliterator.OfLong {
        static final int BATCH_UNIT = IteratorSpliterator.BATCH_UNIT;
        static final int MAX_BATCH = IteratorSpliterator.MAX_BATCH;
        private final PrimitiveIterator.OfLong it;
        private final int characteristics;
        private long est;
        private int batch;

        public LongIteratorSpliterator(PrimitiveIterator.OfLong iterator, long size, int characteristics) {
            this.it = iterator;
            this.est = size;
            this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
                                   ? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
                                   : characteristics;
        }

        public LongIteratorSpliterator(PrimitiveIterator.OfLong iterator, int characteristics) {
            this.it = iterator;
            this.est = Long.MAX_VALUE;
            this.characteristics = characteristics & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
        }

        @Override
        public OfLong trySplit() {
            PrimitiveIterator.OfLong i = it;
            long s = est;
            if(s > 1 && i.hasNext()) {
                int n = batch + BATCH_UNIT;
                if(n > s)
                    n = (int) s;
                if(n > MAX_BATCH)
                    n = MAX_BATCH;
                long[] a = new long[n];
                int j = 0;
                do {
                    a[j] = i.nextLong();
                } while(++j < n && i.hasNext());
                batch = j;
                if(est != Long.MAX_VALUE) {
                    est -= j;
                    return new LongArraySpliterator(a, 0, j, characteristics);
                }
                return new LongArraySpliterator(a, 0, j, characteristics, Long.MAX_VALUE / 2);
            }
            return null;
        }

        @Override
        public void forEachRemaining(LongConsumer action) {
            if(action == null) throw new NullPointerException();
            it.forEachRemaining(action);
        }

        @Override
        public boolean tryAdvance(LongConsumer action) {
            if(action == null) throw new NullPointerException();
            if(it.hasNext()) {
                action.accept(it.nextLong());
                return true;
            }
            return false;
        }

        @Override
        public long estimateSize() {
            return est;
        }

        @Override
        public int characteristics() { return characteristics; }

        @Override
        public Comparator<? super Long> getComparator() {
            if(hasCharacteristics(Spliterator.SORTED))
                return null;
            throw new IllegalStateException();
        }
    }

    static final class DoubleIteratorSpliterator implements Spliterator.OfDouble {
        static final int BATCH_UNIT = IteratorSpliterator.BATCH_UNIT;
        static final int MAX_BATCH = IteratorSpliterator.MAX_BATCH;
        private final PrimitiveIterator.OfDouble it;
        private final int characteristics;
        private long est;
        private int batch;

        public DoubleIteratorSpliterator(PrimitiveIterator.OfDouble iterator, long size, int characteristics) {
            this.it = iterator;
            this.est = size;
            this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
                                   ? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
                                   : characteristics;
        }

        public DoubleIteratorSpliterator(PrimitiveIterator.OfDouble iterator, int characteristics) {
            this.it = iterator;
            this.est = Long.MAX_VALUE;
            this.characteristics = characteristics & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
        }

        @Override
        public OfDouble trySplit() {
            PrimitiveIterator.OfDouble i = it;
            long s = est;
            if(s > 1 && i.hasNext()) {
                int n = batch + BATCH_UNIT;
                if(n > s)
                    n = (int) s;
                if(n > MAX_BATCH)
                    n = MAX_BATCH;
                double[] a = new double[n];
                int j = 0;
                do {
                    a[j] = i.nextDouble();
                } while(++j < n && i.hasNext());
                batch = j;
                if(est != Long.MAX_VALUE) {
                    est -= j;
                    return new DoubleArraySpliterator(a, 0, j, characteristics);
                }
                return new DoubleArraySpliterator(a, 0, j, characteristics, Long.MAX_VALUE / 2);
            }
            return null;
        }

        @Override
        public void forEachRemaining(DoubleConsumer action) {
            if(action == null) throw new NullPointerException();
            it.forEachRemaining(action);
        }

        @Override
        public boolean tryAdvance(DoubleConsumer action) {
            if(action == null) throw new NullPointerException();
            if(it.hasNext()) {
                action.accept(it.nextDouble());
                return true;
            }
            return false;
        }

        @Override
        public long estimateSize() {
            return est;
        }

        @Override
        public int characteristics() {
            return characteristics;
        }

        @Override
        public Comparator<? super Double> getComparator() {
            if(hasCharacteristics(Spliterator.SORTED))
                return null;
            throw new IllegalStateException();
        }
    }
}
