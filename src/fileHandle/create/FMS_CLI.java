package fileHandle.create;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FMS_CLI {
    public static void createFile(String filePath) throws Exception{
        File file = new File(filePath);
        boolean result;
        result = file.createNewFile();
        if(result)
        {
            System.out.println("file created "+file.getCanonicalPath());
        }
        else
        {
            System.out.println("File already exist at location: "+file.getCanonicalPath());
        }
    }
    public static void createFolder(String filePath)
    {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path);
            System.out.println("Directory is created!");

        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());

        }
    }

    public static void renameFile(String sourcePath, String destinationPath) throws Exception
    {
        // Create an object of the File class
        // Replace the file path with path of the directory
        File file = new File(sourcePath);

        // Create an object of the File class
        // Replace the file path with path of the directory
        File rename = new File(destinationPath);

        // store the return value of renameTo() method in
        // flag
        boolean flag = file.renameTo(rename);

        // if renameTo() return true then if block is
        // executed
        if (flag == true) {
            System.out.println("File Successfully Rename");
        }
        // if renameTo() return false then else block is
        // executed
        else {
            System.out.println("Operation Failed");
        }
    }

    public static void renameFolder(String oldFolderPath, String newFolderName) throws IOException{
        Path oldPath = Paths.get(oldFolderPath);
        Path newPath = oldPath.resolveSibling(newFolderName);
        // Rename the folder
        Files.move(oldPath, newPath, StandardCopyOption.ATOMIC_MOVE);
        System.out.println("Folder renamed successfully.");
    }

    public static void deleteFolder(String folderPath) throws Exception {
        Path folderToDelete = Paths.get(folderPath);

        // Delete the folder and its contents recursively
        Files.walkFileTree(folderToDelete, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    // Directory iteration failed
                    throw exc;
                }
            }
        });

        System.out.println("Folder deleted successfully.");
    }

    public static void copyFile(String sourceFilePath, String destinationFilePath) {
        Path sourcePath = Paths.get(sourceFilePath);
        Path destinationPath = Paths.get(destinationFilePath);

        try {
            // Copy the file
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully.");
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
        }
    }

    public static void copyFolder(String sourceFolderPath, String destinationFolderPath) {
        Path sourcePath = Paths.get(sourceFolderPath);
        Path destinationPath = Paths.get(destinationFolderPath);

        try {
            // Copy the folder and its contents recursively
            Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path relativePath = sourcePath.relativize(dir);
                    Path targetPath = destinationPath.resolve(relativePath);
                    Files.createDirectories(targetPath);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path relativePath = sourcePath.relativize(file);
                    Path targetPath = destinationPath.resolve(relativePath);
                    Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println("Folder copied successfully.");
        } catch (IOException e) {
            System.err.println("Error copying folder: " + e.getMessage());
        }
    }

    public static void moveFile(String sourceFilePath, String destinationFilePath) {
        Path sourcePath = Paths.get(sourceFilePath);
        Path destinationPath = Paths.get(destinationFilePath);

        try {
            // Move (rename) the file
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved successfully.");
        } catch (IOException e) {
            System.err.println("Error moving file: " + e.getMessage());
        }
    }

    public static void moveFolder(String sourceFolderPath, String destinationFolderPath) {
        Path sourcePath = Paths.get(sourceFolderPath);
        Path destinationPath = Paths.get(destinationFolderPath);

        try {
            // Move (rename) the folder and its contents recursively
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Folder moved successfully.");
        } catch (IOException e) {
            System.err.println("Error moving folder: " + e.getMessage());
        }
    }

    public static void printFileInfo(String filePath) {
        Path path = Paths.get(filePath);

        try {
            // Get basic file attributes
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);

            // Print file properties
            System.out.println("File Name: " + path.getFileName());
            System.out.println("Absolute Path: " + path.toAbsolutePath());
            System.out.println("Size: " + attributes.size() + " bytes");
            System.out.println("Creation Time: " + formatTime(attributes.creationTime()));
            System.out.println("Last Modified Time: " + formatTime(attributes.lastModifiedTime()));
            System.out.println("Last Access Time: " + formatTime(attributes.lastAccessTime()));
            System.out.println("Owner: " + Files.getOwner(path).getName());

        } catch (IOException e) {
            System.err.println("Error reading file attributes: " + e.getMessage());
        }
    }

    private static String formatTime(FileTime fileTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(fileTime.toMillis()));
    }

    public static List<Path> findFiles(String rootPath, String fileName, String fileExtension) {
        List<Path> foundFiles = new ArrayList<>();

        try {
            Files.walkFileTree(Paths.get(rootPath), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if ((fileName == null || file.getFileName().toString().contains(fileName))
                            && (fileExtension == null || file.toString().endsWith("." + fileExtension))) {
                        foundFiles.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // Handle error while visiting a file
                    System.err.println("Error visiting file: " + file.toString() + " - " + exc.getMessage());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("Error walking file tree: " + e.getMessage());
        }

        return foundFiles;
    }

}
