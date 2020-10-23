package com.github.hafixion.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class FileUtils {

    /**
     * Easy way to create a file
     * @param filename name of the file you want
     * @param path path of that file
     * @return the new file
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public File createFile(String filename, Path path) {
        // if path doesn't exist
        if (!path.getParent().toFile().exists()) {
            path.toFile().mkdirs();
        }
        File file = new File(path.toString(), filename);
        // if file doesn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
