package sparkles.utilities;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sparkles.models.ReturnApiJsonObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Utilities {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utilities.class);

    /**
     * Devuelve el valor del recurso en un archivo .properties
     * @param resourceName nombre del recurso (key)
     * @param fileURL direccion del archivo del recurso
     * @return valor del recurso (value)
     */
    public String getResource(String resourceName, String propFileName)  {
        String recurso;
        Properties prop = new Properties();

        try(InputStream input = getClass().getClassLoader().getResourceAsStream(propFileName)){
            if(input!=null){

                LOGGER.warn("Recurso Encontrado.\nNombre recurso: "+resourceName+"\nFilePath: "+propFileName);
                prop.load(input);
                System.out.println(java.util.Arrays.asList(prop.propertyNames()));

                for(Object k:prop.keySet()){
                    String key = (String)k;
                    System.out.println(key+": "+prop.getProperty(key));
                }

                recurso=prop.getProperty(resourceName);
            }
            else{
                LOGGER.warn("Recurso no encontrado.\nNombre recurso: "+resourceName+"\nFilePath: "+propFileName);
                recurso=null;
            }
        }catch(IOException ie){
            LOGGER.warn("Error durante la busqueda del recurso.\nNombre recurso: "+resourceName+"\nFilePath: "+propFileName);
            LOGGER.warn("Excepcion:\n"+ie.getMessage());
            ie.printStackTrace();
            recurso=null;
        }
        return recurso;
    }

    /**
     * Convierte un json en un Map
     * @param jsonStr
     * @return
     */
    public Map<String, Object> jsonToMapKV(String jsonStr) {
        Gson gson = new Gson();
        Type strObjgMap=new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object>map=gson.fromJson(jsonStr, strObjgMap);
        return map;
    }

    /**
     * Convierte un json con formato de array en un list
     * @param json
     * @return
     */
    public List<String> jsonToStrList(String json){
        Gson gson = new Gson();
        Type strObjList=new TypeToken<List<String>>(){}.getType();
        List<String> list = gson.fromJson(json, strObjList);
        return list;

    }

    /**
     * Transformacion estandar de json en objeto
     * @param object
     * @return
     */
    public String toJson(Object object) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(object);
    }

    /**
     * Genera un json con datos de error para enviar a apis que exijen formato json
     * @param errorCode valor del error
     * @return
     */
    public String generateErrorData(String errorCode) {
        ReturnApiJsonObject rbo = new ReturnApiJsonObject(1);
        rbo.addData(errorCode);
        return toJson(rbo);
    }

    /**
     * Devuelve el valor de un objeto concreto dentro de un json
     * @param json
     * @param dataName
     * @return
     */
    public String getDataFromJson(String json, String dataName) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        String test = obj.get(dataName).toString();
        Object objs = obj.get(dataName);
        LOGGER.debug("Valor json -> "+test);
        return test;
    }
}
