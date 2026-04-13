package storage;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonFileManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void save(Object data, String filePath) throws Exception {
        FileWriter writer = new FileWriter(filePath);
        gson.toJson(data, writer);
        writer.close();
    }

    public static <T> T load(String filePath, Class<T> classType) throws Exception {
        FileReader reader = new FileReader(filePath);
        T data = gson.fromJson(reader, classType);
        reader.close();
        return data;
    }

    public static <T> T loadList(String filePath, Type type) throws Exception {
        FileReader reader = new FileReader(filePath);
        T data = gson.fromJson(reader, type);
        reader.close();
        return data;
    }
}