package sparkles.spelling_corrector;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Map<String, Double> dic;
    private static Map<String, Double> extraDic;
    private static ISpellingCorrector sc;
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        sc = new SpellingCorrector(10000);
        generateDic();
        for (Map.Entry<String, Double> entry : dic.entrySet( )) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
            sc.putWord((entry.getKey()));
        }
        sc.putWord("saber");
        String str;
        do{
            str = input.nextLine();
            ask(str);
        }while(str.charAt(0)!='n');


    }
    private static void ask(String str){
        StringBuilder corrStr = new StringBuilder();
        List<String> palabras;
        for (String word: str.split(" ")) {
            if(extraDic.containsKey(word)){
                System.out.println("Palabra encontrada como excepcion: "+word);
                corrStr.append(word);
            }
            else{
                palabras=sc.wordEdits(word);
                checkPalabras(palabras);
                corrStr.append(sc.correct(word));
            }

            corrStr.append(" ");
        }
        System.out.println(corrStr);
    }

    private static void checkPalabras(List<String> palabras) {
        String strProx=null;
        for (String palabra: palabras) {
            if(dic.containsKey(palabra)){
                if(strProx!=null&&dic.get(strProx)>dic.get(palabra)){
                    System.out.println("Mantenemos la palabra: "+strProx+" || - || "+palabra);
                }else{
                    strProx=palabra;
                    System.out.println("Cambiamos a la palabra: "+strProx);
                }
            }
        }
        System.out.println("");
    }

    public static void generateDic() {
        try {
            if(dic==null)
                dic = (Map<String, Double>) jsonToMapKV(getJsonFromFile("spellchecker_extra_dic.json"));
            if(extraDic==null)
                extraDic= (Map<String, Double>) jsonToMapKV(getJsonFromFile("spellchecker_exceptions.json"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    public static Map<String, ?> jsonToMapKV(String jsonStr) {
        Gson gson = new Gson();
        Type strObjgMap = new TypeToken<Map<String, ?>>() {
        }.getType();
        return gson.fromJson(jsonStr, strObjgMap);
    }

    public static String getJsonFromFile(String fileName) throws IOException, URISyntaxException {
        String json;
        json = readFileAsString(fileName);
        return json;
    }

    public static String readFileAsString(String fileName) throws IOException, URISyntaxException {
        String str;
        Path path = Path.of(ClassLoader.getSystemResource(fileName).toURI());
        str = new String(Files.readAllBytes(path));
        return str;
    }
}