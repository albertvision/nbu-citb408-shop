package me.yasen.storages;

import me.yasen.exceptions.SerializationException;

import java.io.*;

public class FileStorage implements DataStorage {
    @Override
    public void serialize(Serializable object, String path) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream outputStream = new ObjectOutputStream(fos)
        ) {
            outputStream.writeObject(object);
        } catch (IOException ex) {
            throw new SerializationException("Could not serialize.", ex);
        }
    }

    @Override
    public <T extends Serializable> T deserialize(String path) {
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream inputStream = new ObjectInputStream(fis);) {

            return (T) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException("Could not deserialize.", e);
        }
    }
}
