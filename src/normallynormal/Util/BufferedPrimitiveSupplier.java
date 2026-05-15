package normallynormal.Util;

import java.util.function.Supplier;

public class BufferedPrimitiveSupplier<T> {
    private final Supplier<T> supplier;
    private volatile T bufferedValue;

    public BufferedPrimitiveSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public void buffer() {
        bufferedValue = supplier.get(); // assume immutability
    }

    public T get() {
        return bufferedValue;
    }
}

