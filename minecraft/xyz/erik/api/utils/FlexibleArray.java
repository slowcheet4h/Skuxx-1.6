package xyz.erik.api.utils;

import java.util.Iterator;


    public class FlexibleArray<T>
            implements Iterable<T> {
        private T[] elements;

        public FlexibleArray(T[] array) {
            this.elements = array;
        }

        public FlexibleArray() {
            this.elements = (T[]) new Object[0];
        }

        public void add(T t) {
            if (t != null) {
                Object[] array = new Object[size() + 1];
                for (int i = 0; i < array.length; i++) {
                    if (i < size()) {
                        array[i] = get(i);
                    } else {
                        array[i] = t;
                    }
                }
                set((T[]) array);
            }
        }

        public void remove(T t) {
            if (contains(t)) {
                Object[] array = new Object[size() - 1];
                boolean b = true;
                for (int i = 0; i < size(); i++) {
                    if ((b) && (get(i).equals(t))) {
                        b = false;
                    } else {
                        array[(b ? i : i - 1)] = get(i);
                    }
                }
                set((T[]) array);
            }
        }

        public boolean contains(T t) {
            Object[] arrayOfObject;
            int j = (arrayOfObject = array()).length;
            for (int i = 0; i < j; i++) {
                T entry = (T) arrayOfObject[i];
                if (entry.equals(t)) {
                    return true;
                }
            }
            return false;
        }

        private void set(T[] array) {
            this.elements = array;
        }

        public void clear() {
            this.elements = (T[]) new Object[0];
        }

        public T get(int index) {
            return (T) array()[index];
        }

        public int size() {
            return array().length;
        }

        public T[] array() {
            return this.elements;
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public Iterator<T> iterator() {
           return new Iterator() {
                private int index = 0;

                public boolean hasNext() {
                    return (this.index < FlexibleArray.this.size()) && (FlexibleArray.this.get(this.index) != null);
                }

                public T next() {
                    return (T) FlexibleArray.this.get(this.index++);
                }

                public void remove() {
                    FlexibleArray.this.remove(FlexibleArray.this.get(this.index));
                }
            };

    }
}
