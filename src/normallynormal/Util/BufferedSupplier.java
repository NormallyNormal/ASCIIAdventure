package normallynormal.Util;

import java.util.function.Supplier;

public class BufferedSupplier<T extends DeepCopyable<T>> {
    private final Supplier<T> supplier;
    private T bufferedValue;

    public BufferedSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public void buffer() {
        T value = supplier.get();
        if (value != null) {
            bufferedValue = value.deepCopy();
        }
    }

    public T get() {
        return bufferedValue;
    }
}
