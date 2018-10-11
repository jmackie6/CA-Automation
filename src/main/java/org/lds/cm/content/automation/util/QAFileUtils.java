package org.lds.cm.content.automation.util;

import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class QAFileUtils {
    private static final String DOCX_MIMETYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    public static void loadTestFiles(List<File> filenames, File dir) throws Exception {
        File[] dirListing = dir.listFiles();
        for (File file : dirListing) {
            if (file.isDirectory()) {
                loadTestFiles(filenames, file);
            } else {
                filenames.add(file);
            }
        }
    }

    public static File getResourceFile(String fileLocation) {
        ClassLoader classLoader = QAFileUtils.class.getClassLoader();
        URL resource = classLoader.getResource(fileLocation);
        File file = null;

        if (resource != null) {
            file = new File(resource.getFile());
            System.out.println("Resource file loaded: " + file.toPath());
        } else {
            System.out.println("No resource file \"" + fileLocation + "\" found!");
        }

        return file;
    }

    public static boolean isDocXFile(File inputFile) {
        try {
            Tika tika = new Tika();
            return (tika.detect(inputFile).equals(DOCX_MIMETYPE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
