package sparkles.utilities;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sparkles.models.ReturnApiJsonObject;

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
     * @param propFileName direccion del archivo del recurso
     * @return valor del recurso (value)
     */
    public String getResource(String resourceName, String propFileName)  {
        String recurso;
        Properties prop = new Properties();

        try(InputStream input = getClass().getClassLoader().getResourceAsStream(propFileName)){
            if(input!=null){

                LOGGER.warn("Recurso Encontrado.\nNombre recurso: "+resourceName+"\nFilePath: "+propFileName);
                prop.load(input);
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
     * @param jsonStr json a convertir en map
     * @return devuelve un map del json indicado
     *
     */
    public Map<String, ?> jsonToMapKV(String jsonStr) {
        Gson gson = new Gson();
        Type strObjgMap=new TypeToken<Map<String, ?>>(){}.getType();
        return gson.fromJson(jsonStr, strObjgMap);
    }

    /**
     * Convierte un json con formato de array en un list
     * @param json a convertir en lista
     * @return devuelve la lista creada a traves del json otorgado
     */
    public List<String> jsonToStrList(String json){
        Gson gson = new Gson();
        Type strObjList=new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(json, strObjList);

    }

    /**
     * Transformacion estandar de json en objeto
     * @param object objecto a transformar en json
     * @return valor en json del objeto
     */
    public String toJson(Object object) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(object);
    }

    /**
     * Genera un json con datos de error para enviar a apis que exijen formato json
     * @param errorCode valor del error
     * @return string con el error en formato json para sistemas que solo aceptan json
     */
    public String generateErrorData(String errorCode) {
        ReturnApiJsonObject rbo = new ReturnApiJsonObject(1);
        rbo.addData(errorCode);
        return toJson(rbo);
    }

    /**
     * Devuelve el valor de un objeto concreto dentro de un json
     * @param json json en el que se encuentra el valor a buscar
     * @param dataName nombre del valor a buscar
     * @return valor pedido
     */
    public Object getDataFromJson(String json, String dataName) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        Object objs = obj.get(dataName);
        LOGGER.debug("Valor json -> "+obj);
        return objs;
    }

    public String getSingleDataFromJson(String json, String dataName) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        String objs = obj.get(dataName).getAsString();
        LOGGER.debug("Valor json -> "+obj);
        return objs;
    }

    public Properties getProperties(String fileName) throws IOException {
        Properties properties;
        properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream(fileName+".properties"));
        return properties;

    }

}
