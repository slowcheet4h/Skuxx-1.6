/*
 * Created by Erik!
 */

package xyz.erik.api.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    public static void write(File outputFile, List<String> writeContent, boolean overrideContent) {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
            for (String outputLine : writeContent) {
                out.write(outputLine + System.getProperty("line.separator"));
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRunningPath() {
        Path currentRelativePath = Paths.get("", new String[0]);
        String s = currentRelativePath.toAbsolutePath().toString();
        return s;
    }

    public static void copyDirectory(File sourceLocation, File targetLocation)
            throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
            }
        } else {
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            byte[] buf = new byte['?'];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    public static void hide(String target)
            throws Exception {
        Path path = Paths.get(target, new String[0]);

        DosFileAttributes dos = (DosFileAttributes) Files.readAttributes(path, DosFileAttributes.class, new LinkOption[0]);

        Files.setAttribute(path, "dos:hidden", Boolean.valueOf(true), new LinkOption[0]);
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    }

    public static void untouchable(String target)
            throws Exception {
        hide(target);
        readOnly(target);
    }

    public static void readOnly(String path) {
        File file = new File(path);
        file.setReadOnly();
    }

    public static void setWriteable(File file) {
        file.setWritable(true, false);
    }

    public static List<String> readFile(String path) {
        List<String> array = new ArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Throwable localThrowable3 = null;
            try {
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    array.add(sCurrentLine);
                }
            } catch (Throwable localThrowable1) {
                localThrowable3 = localThrowable1;
                throw localThrowable1;
            } finally {
                if (br != null) {
                    if (localThrowable3 != null) {
                        try {
                            br.close();
                        } catch (Throwable localThrowable2) {
                            localThrowable3.addSuppressed(localThrowable2);
                        }
                    } else {
                        br.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }
}