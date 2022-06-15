package sparkles.models;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ReturnApiJsonObject {
    @SuppressWarnings("unused")
    private int id;
    private List<String> data;
    public ReturnApiJsonObject(int id) {
        super();
        this.id = id;
        this.data = new ArrayList<>();
    }
    public void addData(String dato) {
        data.add(dato);
    }
    public static ReturnApiJsonObject fromJson(String json) {
        return new Gson().fromJson(json, ReturnApiJsonObject.class);
    }
}