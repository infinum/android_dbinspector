package im.dino.dbinspector.cache;

public interface Cache<T> {

    void store(T objectToStore);

    T getFromCache();
}
