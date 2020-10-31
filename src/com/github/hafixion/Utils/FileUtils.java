package com.github.hafixion.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class FileUtils {

    /**
     * Easy way to create a file
     * @param filename name of the file you want
     * @param path path of that file
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Deprecated
    public static void createYAMLFile(String filename, Path path) {
        // if path doesn't exist
        String fileyamlname = filename + ".yml";
        File file = new File(path.toString(), fileyamlname);
        // if file doesn't exist
        try {
            file.mkdir();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
