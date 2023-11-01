package com.wedasoft.simpleJavaFxApplicationBase.fileSystemUtil;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author davidweber411
 */
public class CopyFileVisitor extends SimpleFileVisitor<Path> {

    private final Path pathOfFileOrDirToCopy;
    private final Path copyIntoDirPath;
    private final boolean overwriteExistingData;

    public CopyFileVisitor(
            Path pathOfFileOrDirToCopy,
            Path copyIntoDirPath,
            boolean overwriteExistingData) {

        this.pathOfFileOrDirToCopy = pathOfFileOrDirToCopy;
        this.copyIntoDirPath = copyIntoDirPath;
        this.overwriteExistingData = overwriteExistingData;
    }

    @Override
    public FileVisitResult preVisitDirectory(
            Path dir,
            BasicFileAttributes attrs)
            throws IOException {

        Path targetDir = copyIntoDirPath.resolve(pathOfFileOrDirToCopy.relativize(dir));
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

        if (overwriteExistingData) {
            Files.copy(file, copyIntoDirPath.resolve(pathOfFileOrDirToCopy.relativize(file)),
                    StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.copy(file, copyIntoDirPath.resolve(pathOfFileOrDirToCopy.relativize(file)));
        }
        return FileVisitResult.CONTINUE;
    }

}
