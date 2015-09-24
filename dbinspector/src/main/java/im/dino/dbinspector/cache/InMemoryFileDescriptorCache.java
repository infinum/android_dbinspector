package im.dino.dbinspector.cache;

import java.io.FileDescriptor;

public class InMemoryFileDescriptorCache implements Cache<FileDescriptor> {

    private static InMemoryFileDescriptorCache instance;

    private FileDescriptor fileDescriptor;

    private InMemoryFileDescriptorCache() {
    }

    public static InMemoryFileDescriptorCache getInstance() {
        if (instance == null) {
            instance = new InMemoryFileDescriptorCache();
        }
        return instance;
    }

    @Override
    public void store(FileDescriptor objectToStore) {
        fileDescriptor = objectToStore;
    }

    @Override
    public FileDescriptor getFromCache() {
        return fileDescriptor;
    }
}
