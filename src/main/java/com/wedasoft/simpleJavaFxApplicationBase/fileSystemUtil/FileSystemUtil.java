package com.wedasoft.simpleJavaFxApplicationBase.fileSystemUtil;

import jdk.jfr.Experimental;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * <b style="color: red;">This class is not tested.</b>
 *
 * @author davidweber411
 */
@Experimental
public class FileSystemUtil {

    /*
     * ******************************************************************
     * ************* Exists **************************************
     * ******************************************************************
     */

    /**
     * Checks if a directory exists.
     *
     * @param dirPath The path to the directory to check.
     * @return True, if the directory exists. Otherwise false.
     */
    public boolean dirExists(Path dirPath) {
        return Files.isDirectory(dirPath) && Files.exists(dirPath);
    }

    /**
     * Checks if a file exists.
     *
     * @param filePath The path to the file to check.
     * @return True, if the file exists. Otherwise false.
     */
    public boolean fileExists(Path filePath) {
        return Files.isRegularFile(filePath) && Files.exists(filePath);
    }

    /*
     * ******************************************************************
     * ************* Rename **************************************
     * ******************************************************************
     */

    /**
     * Renames a directory.
     *
     * @param dirPath The directory to rename.
     * @param newName The new directory name.
     * @return The renamed directory.
     * @throws Exception On error.
     */
    public static Path renameDir(
            Path dirPath,
            String newName)
            throws Exception {

        if (dirPath == null) {
            throw new IllegalArgumentException("The directory path is null.");
        }
        if (newName == null) {
            throw new IllegalArgumentException("The new name is null.");
        }
        return Files.move(dirPath, dirPath.resolveSibling(newName));
    }

    /**
     * Renames a file.
     *
     * @param filePath The file to rename.
     * @param newName  The new file name.
     * @return The renamed file.
     * @throws Exception On error.
     */
    public static Path renameFile(
            Path filePath,
            String newName)
            throws Exception {

        if (filePath == null) {
            throw new IllegalArgumentException("The file path is null.");
        }
        if (newName == null) {
            throw new IllegalArgumentException("The new name is null.");
        }
        return Files.move(filePath, filePath.resolveSibling(newName));
    }

    /*
     * ******************************************************************
     * ************* Create **************************************
     * ******************************************************************
     */

    /**
     * Creates a directory, including any necessary parent directories.
     *
     * @param dirPath The path of the directory to create.
     * @return The created directory.
     * @throws Exception On error.
     */
    public static Path createDir(
            Path dirPath)
            throws Exception {

        return Files.createDirectories(dirPath);
    }

    /**
     * Creates a file, including any necessary parent directories.
     *
     * @param filePath The path of the file to create.
     * @return The created file.
     * @throws Exception On error.
     */
    public static Path createFile(
            Path filePath)
            throws Exception {

        Files.createDirectories(filePath.getParent());
        return Files.createFile(filePath);
    }

    /*
     * ******************************************************************
     * ************* Delete **************************************
     * ******************************************************************
     */

    /**
     * Deletes a complete directory.
     *
     * @param dirPath The path to the directory to delete.
     * @throws Exception On error.
     */
    public static void deleteDir(
            Path dirPath)
            throws Exception {

        if (Files.isDirectory(dirPath)) {
            Files.walkFileTree(dirPath, new DeleteFileVisitor());
        } else {
            throw new FileNotFoundException("The directory does not exist.");
        }
    }

    /**
     * Deletes a file.
     *
     * @param filePath The path of the file to delete.
     * @throws Exception On error.
     */
    public static void deleteFile(
            Path filePath)
            throws Exception {

        if (Files.isRegularFile(filePath)) {
            Files.delete(filePath);
        } else {
            throw new FileNotFoundException("The file does not exist.");
        }
    }

    /*
     * ******************************************************************
     * ************* Copy **************************************
     * ******************************************************************
     */

    /**
     * Copies a directory into another directory. Creates needed directories.
     *
     * @param dirToCopy              The directory to copy.
     * @param destinationDir         The destination directory to copy into.
     * @param overwriteExistingFiles Overwrites existing files with the same name.
     * @return The new copied directory.
     * @throws Exception On error.
     */
    public static Path copyDir(
            Path dirToCopy,
            Path destinationDir,
            boolean overwriteExistingFiles)
            throws Exception {

        if (dirToCopy == null) {
            throw new IllegalArgumentException("The path of the directory to copy is null.");
        }
        if (destinationDir == null) {
            throw new IllegalArgumentException("The destination directory is null.");
        }
        if (!Files.isDirectory(dirToCopy)) {
            throw new IllegalArgumentException("The path to copy does not represent a directory or it does not exist.");
        }

        destinationDir = Files.createDirectories(destinationDir.resolve(dirToCopy.getFileName()));
        Files.walkFileTree(dirToCopy, new CopyFileVisitor(dirToCopy, destinationDir, overwriteExistingFiles));

        return destinationDir;
    }

    /**
     * Copies content of a directory into another directory. Creates needed directories.
     *
     * @param sourceDir              The directory containing the content to copy.
     * @param destinationDir         The directory in which the content shall be copied.
     * @param overwriteExistingFiles Overwrites existing files with the same name.
     * @return The destination directory.
     */
    public static Path copyDirContent(
            Path sourceDir,
            Path destinationDir,
            boolean overwriteExistingFiles)
            throws Exception {

        if (sourceDir == null) {
            throw new IllegalArgumentException("The path of the source dir is null.");
        }
        if (destinationDir == null) {
            throw new IllegalArgumentException("The path of the destination dir is null.");
        }
        if (!Files.isDirectory(sourceDir)) {
            throw new IllegalArgumentException("The path of the source dir does not represent a directory or it does not exist.");
        }

        Files.createDirectories(destinationDir);
        Files.walkFileTree(sourceDir, new CopyFileVisitor(sourceDir, destinationDir, overwriteExistingFiles));

        return destinationDir;
    }

    /**
     * Copies a file into another directory. Creates needed directories.
     *
     * @param fileToCopy            The file to copy.
     * @param destinationDir        The directory to copy the file into.
     * @param overwriteExistingFile Overwrites an existing file with the same name.
     * @return The new copied file.
     * @throws Exception On error.
     */
    public static Path copyFile(
            Path fileToCopy,
            Path destinationDir,
            boolean overwriteExistingFile)
            throws Exception {

        if (fileToCopy == null) {
            throw new IllegalArgumentException("The file path is null.");
        }
        if (destinationDir == null) {
            throw new IllegalArgumentException("The destination directory is null.");
        }
        if (!Files.isRegularFile(fileToCopy)) {
            throw new IllegalArgumentException("The path to copy does not represent a file or it does not exist.");
        }

        Files.createDirectories(destinationDir);
        Path newCopiedFile = destinationDir.resolve(fileToCopy.getFileName());
        if (!overwriteExistingFile) {
            Files.copy(fileToCopy, newCopiedFile);
        } else {
            Files.copy(fileToCopy, newCopiedFile, StandardCopyOption.REPLACE_EXISTING);
        }

        return newCopiedFile;
    }

    /*
     * ******************************************************************
     * ************* Move **************************************
     * ******************************************************************
     */

    /**
     * Moves a directory into another directory. Creates needed directories.
     *
     * @param dirToMove              The directory to move.
     * @param destinationDir         The directory in which the given directory shall be moved.
     * @param overwriteExistingFiles Overwrites existing files with the same name.
     * @return The moved directory.
     * @throws Exception On error.
     */
    public static Path moveDir(
            Path dirToMove,
            Path destinationDir,
            boolean overwriteExistingFiles)
            throws Exception {

        if (dirToMove == null) {
            throw new IllegalArgumentException("The directory to move is null.");
        }
        if (destinationDir == null) {
            throw new IllegalArgumentException("The destination directory is null.");
        }
        if (!Files.isDirectory(dirToMove)) {
            throw new IllegalArgumentException("The path to move does not represent a directory or it does not exist.");
        }
        if (!overwriteExistingFiles) {
            File file = destinationDir.toFile();
            file = new File(file, dirToMove.getFileName().toString());
            if (Files.isDirectory(file.toPath())) {
                throw new IllegalArgumentException("A directory with the given name already exists.");
            }
        }

        Path result = copyDir(dirToMove, destinationDir, overwriteExistingFiles);
        deleteDir(dirToMove);
        return result;
    }

    /**
     * Moves a file into another directory. Creates needed directories.
     *
     * @param fileToMove            The file to move.
     * @param destinationDir        The directory to move the file into.
     * @param overwriteExistingFile Overwrites existing files with the same name.
     * @return The moved file.
     * @throws Exception On error.
     */
    public static Path moveFile(
            Path fileToMove,
            Path destinationDir,
            boolean overwriteExistingFile)
            throws Exception {

        if (fileToMove == null) {
            throw new IllegalArgumentException("The path of the file to move is null.");
        }
        if (destinationDir == null) {
            throw new IllegalArgumentException("The path of the destination directory is null.");
        }
        if (!Files.isRegularFile(fileToMove)) {
            throw new IllegalArgumentException("The path of the file to move does not represent a file or it does not exist.");
        }
        if (!overwriteExistingFile) {
            File file = destinationDir.toFile();
            file = new File(file, fileToMove.getFileName().toString());
            if (Files.isRegularFile(file.toPath())) {
                throw new IllegalArgumentException("A file with the given name already exists.");
            }
        }

        Path result = copyFile(fileToMove, destinationDir, overwriteExistingFile);
        deleteFile(fileToMove);
        return result;
    }

    /*
     * ******************************************************************
     * ************* Clear **************************************
     * ******************************************************************
     */

    /**
     * Clears a directory and removes all of its content.
     *
     * @param dirToClear The directory which shall be cleared.
     * @return The cleared directory.
     * @throws Exception On error.
     */
    public static Path clearDir(
            Path dirToClear)
            throws Exception {

        if (dirToClear == null) {
            throw new FileNotFoundException("The path of the directory to clear is null.");
        }
        if (!Files.isDirectory(dirToClear)) {
            throw new FileNotFoundException("The directory to clear does not exist.");
        }
        deleteDir(dirToClear);
        return createDir(dirToClear);
    }

    /**
     * Clears a file and removes all of its content.
     *
     * @param fileToClear The file which shall be cleared.
     * @return The cleared file.
     * @throws Exception On error.
     */
    public static Path clearFile(
            Path fileToClear)
            throws Exception {

        if (fileToClear == null) {
            throw new IllegalArgumentException("The path of the file to clear is null.");
        }
        if (!Files.isRegularFile(fileToClear)) {
            throw new FileNotFoundException("The file to clear does not exist.");
        }
        deleteFile(fileToClear);
        return createFile(fileToClear);
    }

    /*
     * ******************************************************************
     * ************* Write and read **************************************
     * ******************************************************************
     */

    //    public static String readFromFile(
    //            Path pathOfFileToRead) {
    //
    //        // Files.isReadable(pathOfFileToRead)
    //        return null;
    //    }
    //
    //    public static void writeToFile(
    //            Path pathOfFile,
    //            String contentToWrite) {
    //
    //        // Files.isWritable(pathOfFile)
    //    }


    /*
     * ******************************************************************
     * ************* Other **************************************
     * ******************************************************************
     */
    public Path getHomeDir() {
        return Paths.get(System.getProperty("user.home"));
    }

}
