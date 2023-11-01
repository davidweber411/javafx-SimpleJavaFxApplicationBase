package com.wedasoft.simpleJavaFxApplicationBase.fileSystemUtil;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author davidweber411
 */
public class FileSystemUtils {

    /**
     * Creates a file, including any necessary but not existent parent directories.
     *
     * @param pathOfFileToCreate
     * @param overwriteExistingFile
     * @return The path of the created file.
     * @throws Exception
     * @author David Weber
     */
    public static Path createFile(
            Path pathOfFileToCreate,
            boolean overwriteExistingFile)
            throws Exception {

        if (overwriteExistingFile) {
            Files.deleteIfExists(pathOfFileToCreate);
        }
        Files.createDirectories(pathOfFileToCreate.getParent());
        return Files.createFile(pathOfFileToCreate);
    }

    /**
     * Creates a directory, including any necessary but not existent parent
     * directories.
     *
     * @param pathOfDirToCreate
     * @return The path of the created directory.
     * @throws Exception
     * @author David Weber
     */
    public static Path createDir(
            Path pathOfDirToCreate)
            throws Exception {

        return Files.createDirectories(pathOfDirToCreate);
    }

    /**
     * Deletes a file.
     *
     * @param pathOfFileToDelete
     * @param throwIfFileNotExists
     * @return The path of the deleted file.
     * @throws Exception
     * @author David Weber
     */
    public static Path deleteFile(
            Path pathOfFileToDelete,
            boolean throwIfFileNotExists)
            throws Exception {

        if (Files.isRegularFile(pathOfFileToDelete)) {
            Files.delete(pathOfFileToDelete);
        } else {
            if (throwIfFileNotExists) {
                throw new FileNotFoundException("The file could not be deleted because it does not exist.");
            }
        }
        return pathOfFileToDelete;
    }

    /**
     * Deletes a whole directory.
     *
     * @param pathOfDirToDelete
     * @param throwIfDirNotExists
     * @return The path of the deleted directory, but if the directory does not
     * exist, this method will retun null.
     * @throws Exception
     * @author David Weber
     */
    public static Path deleteDir(
            Path pathOfDirToDelete,
            boolean throwIfDirNotExists)
            throws Exception {

        if (Files.isDirectory(pathOfDirToDelete)) {
            return Files.walkFileTree(pathOfDirToDelete, new DeleteFileVisitor());
        } else {
            if (throwIfDirNotExists) {
                throw new FileNotFoundException("The directory could not be deleted because it does not exist.");
            }
        }
        return null;
    }

    /**
     * Copies a file to another directory and creates the directory path if needed.
     *
     * @param pathOfFileToCopy
     * @param copyIntoDirPath
     * @param overwriteExistingFile
     * @return The path of the copied file.
     * @throws Exception
     * @author David Weber
     */
    public static Path copyFile(
            Path pathOfFileToCopy,
            Path copyIntoDirPath,
            boolean overwriteExistingFile)
            throws Exception {

        if (pathOfFileToCopy == null) {
            throw new IllegalArgumentException(
                    "File could not be copied. The path of the file to copy must not be null.");
        }
        if (copyIntoDirPath == null) {
            throw new IllegalArgumentException(
                    "File could not be copied. The path of the directory, in which the file is copied, must not be null.");
        }
        if (!Files.isRegularFile(pathOfFileToCopy)) {
            throw new IllegalArgumentException(
                    "File could not be copied. The path of the file to copy either does not exist or it does not represent a file.");
        }

        Path copiedFilePath = FileSystemUtils.appendToPath(copyIntoDirPath, pathOfFileToCopy.getFileName().toString());
        Files.createDirectories(copyIntoDirPath);
        if (!overwriteExistingFile) {
            Files.copy(pathOfFileToCopy, copiedFilePath);
        } else {
            Files.copy(pathOfFileToCopy, copiedFilePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return copiedFilePath;
    }

    /**
     * Copies a whole directory including its content to another directory and
     * creates the directory path if needed.
     *
     * @param pathOfDirToCopy
     * @param copyIntoDirPath
     * @param overwriteExistingFiles
     * @return The path of the copied directory.
     * @throws Exception
     * @author David Weber
     */
    public static Path copyDir(
            Path pathOfDirToCopy,
            Path copyIntoDirPath,
            boolean overwriteExistingFiles)
            throws Exception {

        if (pathOfDirToCopy == null) {
            throw new IllegalArgumentException(
                    "Directory could not be copied. The path of the directory to copy must not be null.");
        }
        if (copyIntoDirPath == null) {
            throw new IllegalArgumentException(
                    "Directory could not be copied. The path of the directory, in which the directory is copied, must not be null.");
        }
        if (!Files.isDirectory(pathOfDirToCopy)) {
            throw new IllegalArgumentException(
                    "Directory could not be copied. The path of the directory to copy either does not exist or it does not represent a directory.");
        }

        copyIntoDirPath = Files.createDirectories(copyIntoDirPath.resolve(pathOfDirToCopy.getFileName()));
        Files.walkFileTree(pathOfDirToCopy,
                new CopyFileVisitor(pathOfDirToCopy, copyIntoDirPath, overwriteExistingFiles));

        return copyIntoDirPath;
    }

    /**
     * Copies the complete content of a directory into another directory and creates
     * the directory path if needed.
     *
     * @param contentContainingDir
     * @param copyIntoDirPath
     * @param overwriteExistingFiles
     * @return The path of the content containing directory in which the content is
     * copied.
     * @throws Exception
     * @author David Weber
     */
    public static Path copyDirContent(
            Path contentContainingDir,
            Path copyIntoDirPath,
            boolean overwriteExistingFiles)
            throws Exception {

        if (contentContainingDir == null) {
            throw new IllegalArgumentException(
                    "Directory content could not be copied. The path of the content containing directory to copy must not be null.");
        }
        if (copyIntoDirPath == null) {
            throw new IllegalArgumentException(
                    "Directory content could not be copied. The path of the directory, into which the directory content shall be copied, must not be null.");
        }
        if (!Files.isDirectory(contentContainingDir)) {
            throw new IllegalArgumentException(
                    "Directory content could not be copied. The path of the directory which contains the content to copy either does not exist or it does not represent a directory.");
        }

        Files.createDirectories(copyIntoDirPath);
        Files.walkFileTree(contentContainingDir,
                new CopyFileVisitor(contentContainingDir, copyIntoDirPath, overwriteExistingFiles));

        return copyIntoDirPath;
    }

    /**
     * Moves a file to another directory and creates the directory path if needed.
     *
     * @param pathOfFileToMove
     * @param moveIntoDirPath
     * @param overwriteExistingFile
     * @return The path of the moved file.
     * @throws Exception
     * @author David Weber
     */
    public static Path moveFile(
            Path pathOfFileToMove,
            Path moveIntoDirPath,
            boolean overwriteExistingFile)
            throws Exception {

        if (pathOfFileToMove == null) {
            throw new IllegalArgumentException(
                    "File could not be moved. The path of the file to move must not be null.");
        }
        if (moveIntoDirPath == null) {
            throw new IllegalArgumentException(
                    "File could not be moved. The path of the directory, in which the file shall be moved, must not be null.");
        }
        if (!Files.isRegularFile(pathOfFileToMove)) {
            throw new IllegalArgumentException(
                    "File could not be moved. The path of the file to move either does not exist or it does not represent a file.");
        }
        if (!overwriteExistingFile && Files.isRegularFile(
                FileSystemUtils.appendToPath(moveIntoDirPath, pathOfFileToMove.getFileName().toString()))) {
            throw new IllegalArgumentException(
                    "File could not be moved. In the directory, in which the file shall be moved, exists already a file with the given name.");
        }

        Path result = FileSystemUtils.copyFile(pathOfFileToMove, moveIntoDirPath, overwriteExistingFile);
        FileSystemUtils.deleteFile(pathOfFileToMove, false);
        return result;
    }

    /**
     * Moves a directory including its content to another directory and creates the
     * directory path if needed.
     *
     * @param pathOfDirToMove
     * @param moveIntoDirPath
     * @param overwriteExistingFiles
     * @return The path of the moved directory.
     * @throws Exception
     * @author David Weber
     */
    public static Path moveDir(
            Path pathOfDirToMove,
            Path moveIntoDirPath,
            boolean overwriteExistingFiles)
            throws Exception {

        if (pathOfDirToMove == null) {
            throw new IllegalArgumentException(
                    "Directory could not be moved. The path of the directory to move must not be null.");
        }
        if (moveIntoDirPath == null) {
            throw new IllegalArgumentException(
                    "Directory could not be moved. The path of the directory, in which the file shall be moved, must not be null.");
        }
        if (!Files.isDirectory(pathOfDirToMove)) {
            throw new IllegalArgumentException(
                    "Directory could not be moved. The path of the directory to move either does not exist or it does not represent a file.");
        }
        if (!overwriteExistingFiles && Files
                .isDirectory(FileSystemUtils.appendToPath(moveIntoDirPath, pathOfDirToMove.getFileName().toString()))) {
            throw new IllegalArgumentException(
                    "Directory could not be moved. In the directory, in which the file shall be moved, exists already a directory with the given name.");
        }

        FileSystemUtils.copyDir(pathOfDirToMove, moveIntoDirPath, true);
        Path result = FileSystemUtils.copyDir(pathOfDirToMove, moveIntoDirPath, overwriteExistingFiles);
        FileSystemUtils.deleteDir(pathOfDirToMove, false);
        return result;
    }

    /**
     * Renames a file or a directory.
     *
     * @param pathOfFileOrDirToRename : the element to rename
     * @param newName                 : the new name
     * @throws IOException
     * @author David Weber
     */
    public static void rename(
            Path pathOfFileOrDirToRename,
            String newName)
            throws Exception {

        if (pathOfFileOrDirToRename == null) {
            throw new IllegalArgumentException("The path of the element to rename must not be null.");
        }
        if (newName == null) {
            throw new IllegalArgumentException("The new name must not be null.");
        }
        Files.move(pathOfFileOrDirToRename, pathOfFileOrDirToRename.resolveSibling(newName));
    }

    /**
     * Appends a file or dir name to a path.
     *
     * @param pathToExtend          : the path which will be extended
     * @param fileOrDirNameToAppend : the String representation of the element to
     *                              append
     * @return The full merged path.
     * @author David Weber
     */
    public static Path appendToPath(
            Path pathToExtend,
            String fileOrDirNameToAppend) {

        File file = pathToExtend.toFile();
        file = new File(file, fileOrDirNameToAppend);
        return file.toPath();
    }

    /**
     * Appends a path to another path, normalizes the resulting path and converts it
     * to an absolute path.
     *
     * @param pathToExtend
     * @param pathToAppend
     * @return The merged absolute path.
     * @author David Weber
     */
    public static Path appendToPath(
            Path pathToExtend,
            Path pathToAppend) {

        return pathToExtend.resolve(pathToAppend).normalize().toAbsolutePath();
    }

    /**
     * Appends several file or dir names to a path.
     *
     * @param pathToExtend           : the path which will be extended
     * @param fileOrDirNamesToAppend : the String representations of the elements to
     *                               append
     * @return The full merged path.
     * @author David Weber
     */
    public static Path appendToPath(
            Path pathToExtend,
            String... fileOrDirNamesToAppend) {

        File file = pathToExtend.toFile();
        for (String pathString : fileOrDirNamesToAppend) {
            file = new File(file, pathString);
        }
        return file.toPath();
    }

    /**
     * Clears the content of the given file.
     *
     * @param fileToClear
     * @return The path of the cleared file.
     * @throws Exception
     * @author David Weber
     */
    public static Path clearFile(
            Path fileToClear)
            throws Exception {

        if (fileToClear == null) {
            throw new IllegalArgumentException(
                    "File could not be cleared. The path of the file to clear must not be null.");
        }
        if (!Files.isRegularFile(fileToClear)) {
            throw new FileNotFoundException("File could not be cleared. The file to clear does not exist.");
        }
        FileSystemUtils.deleteFile(fileToClear, true);
        return FileSystemUtils.createFile(fileToClear, true);
    }

    /**
     * Clears the content of the given directory.
     *
     * @param dirToClear
     * @return The path of the cleared directory.
     * @throws Exception
     * @author David Weber
     */
    public static Path clearDir(
            Path dirToClear)
            throws Exception {

        if (dirToClear == null) {
            throw new IllegalArgumentException(
                    "Directory could not be cleared. The path of the directory to clear must not be null.");
        }
        if (!Files.isDirectory(dirToClear)) {
            throw new FileNotFoundException("Directory could not be cleared. The directory to clear does not exist.");
        }
        FileSystemUtils.deleteDir(dirToClear, true);
        return FileSystemUtils.createDir(dirToClear);
    }

    /**
     * NOT IMPLEMENTED
     *
     * @param pathOfFileToRead
     * @return
     */
    public static String readFromFile(
            Path pathOfFileToRead) {

        // Files.isReadable(pathOfFileToRead)
        return null;
    }

    /**
     * NOT IMPLEMENTED
     *
     * @param pathOfFile
     * @param contentToWrite
     */
    public static void writeToFile(
            Path pathOfFile,
            String contentToWrite) {

        // Files.isWritable(pathOfFile)
    }

    /**
     * NOT TESTED
     *
     * @return
     */
    public static File getPathToDesktop() {
        return FileSystemView.getFileSystemView().getHomeDirectory();
    }

    /**
     * NOTE TESTED
     *
     * @param string
     * @return
     * @throws IOException
     */
    public static String getParentFolderOfFilePath(
            String string)
            throws IOException {

        return new File(string).getParentFile().getCanonicalPath();
    }

    /**
     * Determines a path of a JAR file by a containing class.
     *
     * @param clazz : The class in the JAR file whose path is to be determined.
     * @return The path of the JAR.
     * @throws URISyntaxException
     * @author David Weber
     */
    public static File getPathOfJarContainingClass(
            Class<?> clazz)
            throws URISyntaxException {

        return new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
    }

    //	/**
    //	 * NOT SAFE
    //	 *
    //	 * @param source
    //	 * @param destination
    //	 */
    //	public static void copyContentOfFolder(File source, File destination) {
    //		if (source.isDirectory()) {
    //			if (!destination.exists()) {
    //				destination.mkdirs();
    //			}
    //
    //			String files[] = source.list();
    //
    //			for (String file : files) {
    //				File srcFile = new File(source, file);
    //				File destFile = new File(destination, file);
    //
    //				copyContentOfFolder(srcFile, destFile);
    //			}
    //		} else {
    //			InputStream in = null;
    //			OutputStream out = null;
    //
    //			try {
    //				in = new FileInputStream(source);
    //				out = new FileOutputStream(destination);
    //
    //				byte[] buffer = new byte[1024];
    //
    //				int length;
    //				while ((length = in.read(buffer)) > 0) {
    //					out.write(buffer, 0, length);
    //				}
    //			} catch (Exception e) {
    //				try {
    //					in.close();
    //				} catch (IOException e1) {
    //					e1.printStackTrace();
    //				}
    //
    //				try {
    //					out.close();
    //				} catch (IOException e1) {
    //					e1.printStackTrace();
    //				}
    //			}
    //		}
    //	}

}
