package priv.rdo.trade.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class TestFileUtils {
    private static final String REQUESTS_FOLDER = "src/test/resources/requests/";

    private TestFileUtils() {
    }

    public static String fileToString(String filename) throws IOException {
        return fileToString(REQUESTS_FOLDER, filename);
    }

    public static String fileToString(String basePath, String filename) throws IOException {
        return Files.lines(Paths.get(basePath, filename)).collect(Collectors.joining());
    }
}
