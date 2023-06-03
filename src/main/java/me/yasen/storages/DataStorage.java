package me.yasen.storages;

import java.io.Serializable;

public interface DataStorage {
    public void serialize(Serializable object, String path);

    public <T extends Serializable> T deserialize(String path);
}
