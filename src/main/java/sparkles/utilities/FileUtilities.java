package sparkles.utilities;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtilities {
    public String getJsonFromFile(String fileName) throws IOException, URISyntaxException {
        String json;
        json=this.readFileAsString(fileName);
        return json;
    }
    public String readFileAsString(String fileName) throws IOException, URISyntaxException {
        String str;

        Path path=Path.of(ClassLoader.getSystemResource(fileName).toURI());
        str= new String(Files.readAllBytes(path));
        return str;
    }
    public void addStringToFile(String str, String filePath) throws IOException, URISyntaxException {
        Path path=Path.of(ClassLoader.getSystemResource(filePath).toURI());
        Files.write(path, str.getBytes());
    }

    public String checkIfFileExistThenWriteString(String str, String filepath) throws IOException, URISyntaxException {
        String json;
        if(this.checkIfFileExist(filepath)){
            json=this.readFileAsString(filepath);
        }
        else{
            this.addStringToFile(str, filepath);
            json=str;
        }
        return json;
    }

    private boolean checkIfFileExist(String filepath) {
        boolean exist=false;
        try{
            Path path=Path.of(ClassLoader.getSystemResource(filepath).toURI());
            exist=Files.exists(path);
        }
        catch(Exception e){
            exist=false;
        }
        return exist;
    }


}
