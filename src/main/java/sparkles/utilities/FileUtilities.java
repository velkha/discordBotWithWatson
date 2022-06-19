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
}
