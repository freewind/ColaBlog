package freewind.colablog.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class IO {

    public static List<String> readLines(String text) {
        try {
            return IOUtils.readLines(new StringReader(text));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readLines(File file) {
        try {
            return FileUtils.readLines(file, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFileToString(File file) {
        try {
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeStringToFile(File file, String text) {
        try {
            FileUtils.writeStringToFile(file, text, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void cleanDir(File targetDir) {
        try {
            FileUtils.cleanDirectory(targetDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
