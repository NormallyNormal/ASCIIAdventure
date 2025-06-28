package normallynormal.Util;

public interface DeepCopyable<T> {
    T deepCopy();
    T deepCopy(T t);
}
