package com.wedasoft.simpleJavaFxApplicationBase.fileSystemUtil;

import java.io.File;

/**
 * @author davidweber411
 */
public class FileUtils {

    /**
     * This method is used for deleting a file or a directory, including directories
     * with content. Directories are handles with the File class too.
     *
     * @param fileOrDirectory <br>
     *                        e.g.1: System.getProperty("user.home") +
     *                        File.separator + "Desktop" + File.separator + "TEST" +
     *                        File.separator + "dir2") <br>
     *                        e.g.2: System.getProperty("user.home") +
     *                        File.separator + "Desktop" + File.separator + "TEST" +
     *                        File.separator + "dir2" + File.separator +
     *                        "dir2file1.txt")
     * @return true, if the file or directory is deleted, otherwise false.
     */
    public static boolean deleteFileOrDirectoryIfExists(
            File fileOrDirectory) {

        if (fileOrDirectory == null || (!fileOrDirectory.isFile() && !fileOrDirectory.isDirectory())) {
            return false;
        }
        if (fileOrDirectory.isDirectory()) {
            File[] children = fileOrDirectory.listFiles();
            for (File child : children) {
                deleteFileOrDirectoryIfExists(child);
            }
        }
        return fileOrDirectory.delete();
    }

    /**
     * @param fileNamePath
     * @return the file
     * @throws Exception
     * @implNote For platform independency, please use File.separator instead of
     * /.<br>
     * <br>
     * fileNameWithPath examples:<br>
     * file.txt -> creates the file in the project root folder<br>
     * /file.txt -> creates the file in the folder, in which eclipse ide
     * is placed<br>
     * System.getProperty("user.home")/file.txt -> creates the file in the
     * user home directory
     */
    public static File createFileIfNotExist(
            String fileNamePath)
            throws Exception {

        File file = new File(fileNamePath);
        if (file.createNewFile()) {
            System.out.println("File created: " + file.getName());
            return file;
        } else {
            System.out.println("File already exists: " + file.getName());
            return file;
        }
    }

    /**
     * @param directoryPathString (e. g.: "abc")
     * @return the directory path
     * @implNote For platform independency, please use File.separator instead of
     * /.<br>
     * <br>
     * directoryPathString examples for not in user home:<br>
     * hello -> creates the directory "hello" in the project root
     * folder<br>
     * /hello -> creates the directory "hello" in the folder, in which
     * eclipse ide is placed<br>
     * System.getProperty("user.home")/hello/world -> creates the
     * directory path "hello/world" in the user home directory <br>
     * <br>
     * directoryPathString examples for in user home:<br>
     * hello -> creates the directory "hello" in the user home
     * directory<br>
     * hello/world -> creates the directory path "hello/world" in the user
     * home directory
     */
    public static String createDirectoryPathIfNotExist(
            boolean createInUserHomeDir,
            String directoryPathString) {

        String filePath = "";
        if (createInUserHomeDir) {
            filePath += System.getProperty("user.home");
            filePath += File.separator;
        }
        filePath += directoryPathString;

        File directoryPath = new File(filePath);
        if (!directoryPath.exists()) {
            directoryPath.mkdirs();
            System.out.println("Directory path created: " + filePath);
        } else {
            System.out.println("Directory path was not created, because it already existed: " + filePath);
        }
        return filePath;
    }

    /**
     * @param createInUserHomeDir
     * @param directoryPath
     * @param fileName
     * @return
     * @throws Exception
     * @implNote For platform independency, please use File.separator instead of
     * /.<br>
     * <br>
     * directoryPathString examples for not in user home:<br>
     * hello/abc.txt -> creates "hello/abc.txt" in the project root
     * folder<br>
     * /hello/abc.txt -> creates "hello/abc.txt" in the folder, in which
     * eclipse ide is placed<br>
     * <br>
     * directoryPathString examples for in user home:<br>
     * hello/abc.txt -> creates "hello/abc.txt" in the user home
     * directory<br>
     * hello/world/abc.txt -> creates "hello/world/abc.txt" in the user
     * home directory
     */
    public static File createFileWithDirectoryPathIfNotExist(
            boolean createInUserHomeDir,
            String directoryPath,
            String fileName)
            throws Exception {

        String fileNameWithDirectoryPath = createDirectoryPathIfNotExist(createInUserHomeDir, directoryPath);
        return createFileIfNotExist(fileNameWithDirectoryPath + File.separator + fileName);
    }

}
