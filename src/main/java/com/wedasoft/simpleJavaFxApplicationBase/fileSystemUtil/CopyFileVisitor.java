package com.wedasoft.simpleJavaFxApplicationBase.fileSystemUtil;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author davidweber411
 */
public class CopyFileVisitor extends SimpleFileVisitor<Path> {

    private final Path pathToCopy;
    private final Path destinationPath;
    private final boolean overwriteExisting;

    public CopyFileVisitor(
            Path pathToCopy,
            Path destinationPath,
            boolean overwriteExisting) {

        this.pathToCopy = pathToCopy;
        this.destinationPath = destinationPath;
        this.overwriteExisting = overwriteExisting;
    }

    @Override
    public FileVisitResult preVisitDirectory(
            Path dir,
            BasicFileAttributes attrs)
            throws IOException {

        Path targetDir = destinationPath.resolve(pathToCopy.relativize(dir));
        if (Files.notExists(targetDir)) {
            Files.createDirectory(targetDir);
        }
        return FileVisitResult.CONTINUE;
    }

    //	@Override
    //	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    //		Files.copy(file, copyIntoDirPath.resolve(pathOfFileOrDirToCopy.relativize(file)));
    //		return FileVisitResult.CONTINUE;
    //	}

    @Override
    public FileVisitResult visitFile(
            Path file,
            BasicFileAttributes attrs)
            throws IOException {

        if (overwriteExisting) {
            Files.copy(file, destinationPath.resolve(pathToCopy.relativize(file)),
                    StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.copy(file, destinationPath.resolve(pathToCopy.relativize(file)));
        }
        return FileVisitResult.CONTINUE;
    }

}
