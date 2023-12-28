import utiles.utilsFunction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FMS_CLI {
    static int copiedFolders = 0;
    static int copiedFiles = 0;
    static int deletedFiles = 0;
    static int deletedFolders = 0;
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
    public static void createFolder(String filePath) throws IOException
    {
        Path path = Paths.get(filePath);
        Files.createDirectories(path);
        System.out.println("Directory is created!");
    }

    public static void renameFile(String sourcePath, String destinationPath) {
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
        if (flag) {
            System.out.println("File Successfully Rename");
        }
        // if renameTo() return false then else block is
        // executed
        else {
            // Check write permission
            if (!Files.isWritable(Paths.get(sourcePath))) {
                System.out.println("Permission denied");
            } else {
                System.out.println("Operation Failed");
            }
        }
    }

    public static void renameFolder(String oldFolderPath, String newFolderName) throws IOException{
        Path oldPath = Paths.get(oldFolderPath);
        Path newPath = oldPath.resolveSibling(newFolderName);
        // Rename the folder
        Files.move(oldPath, newPath, StandardCopyOption.ATOMIC_MOVE);
        System.out.println("Folder renamed successfully.");
    }

    /*public static void deleteFolder(String folderPath) throws Exception {
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
    }*/

    public static void deleteFolder(String folderPath) {
        long startTime = System.currentTimeMillis();

        Path folderToDelete = Paths.get(folderPath);

        // Counter for tracking the number of deleted items
        final int[] deletedItems = {0};

        try{
            // Get the total number of files and subdirectories to delete
            int totalItems = (int) Files.walk(folderToDelete).count();

            // Delete the folder and its contents recursively
            Files.walkFileTree(folderToDelete, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    deletedFiles++;
                    deletedItems[0]++;
                    updateProgressBar("Deleting: ", deletedItems[0], totalItems);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // Handle access denied or other exceptions here
                    System.out.println("\nError deleting file: " + file + ", " + exc.getMessage());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc == null) {
                        Files.delete(dir);
                        deletedFolders++;
                        deletedItems[0]++;
                        updateProgressBar("Deleting: ", deletedItems[0], totalItems);
                        return FileVisitResult.CONTINUE;
                    } else {
                        // Directory iteration failed
                        throw exc;
                    }
                }
            });
            System.out.println("\rFolder deleted successfully.");
        }
        catch (AccessDeniedException e) {
            System.out.println("\rAccess denied while deleting the folder");
        }
        catch (Exception ex)
        {
            System.out.println("\rInvalid input found");
        }
        finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;


            System.out.println("Deleted files: " + deletedFiles);
            System.out.println("Deleted folders: " + deletedFolders);
            System.out.println("Total time taken: " + formatDuration(duration));
        }



    }

    public static void copyFile(String sourceFilePath, String destinationFilePath) throws IOException {
        long startTime = System.currentTimeMillis();
        Path sourcePath = Paths.get(sourceFilePath);
        Path destinationPath = Paths.get(destinationFilePath);

        // Get the file size for progress calculation
        long fileSize = Files.size(sourcePath);

        try (InputStream inputStream = Files.newInputStream(sourcePath);
             OutputStream outputStream = Files.newOutputStream(destinationPath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytesRead = 0;

            System.out.println("Copying file...");

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                // Calculate progress percentage
                int progress = (int) ((totalBytesRead * 100) / fileSize);

                // Update progress bar
                updateProgressBar(progress);
            }
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("\nFile copied successfully.");
            System.out.println("From: "+sourceFilePath);
            System.out.println("To: "+destinationFilePath);
            System.out.println("Total time taken: " + formatDuration(duration));
        }
    }


    /*public static void copyFile(String sourceFilePath, String destinationFilePath) throws IOException {
        Path sourcePath = Paths.get(sourceFilePath);
        Path destinationPath = Paths.get(destinationFilePath);

        // Copy the file
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File copied successfully.");
    }*/

    /*public static void copyFolder(String sourceFolderPath, String destinationFolderPath) throws IOException
    {
        Path sourcePath = Paths.get(sourceFolderPath);
        Path destinationPath = Paths.get(destinationFolderPath);

        // Copy the folder and its contents recursively
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<>() {
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
        System.out.println("From: "+sourceFolderPath);
        System.out.println("To: "+destinationFolderPath);
    }*/

    public static void copyFolder(String sourceFolderPath, String destinationFolderPath) throws IOException {
        long startTime = System.currentTimeMillis();
        Path sourcePath = Paths.get(sourceFolderPath);
        Path destinationPath = Paths.get(destinationFolderPath);

        // Get the total number of files and subdirectories to copy
        int totalItems = (int) Files.walk(sourcePath).count();

        // Counter for tracking the number of copied items
        final int[] copiedItems = {0};

        // Copy the folder and its contents recursively
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path relativePath = sourcePath.relativize(dir);
                Path targetPath = destinationPath.resolve(relativePath);
                Files.createDirectories(targetPath);
                copiedFolders++;
                copiedItems[0]++;
                updateProgressBar("Copying: ",copiedItems[0], totalItems);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path relativePath = sourcePath.relativize(file);
                Path targetPath = destinationPath.resolve(relativePath);
                Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                copiedFiles++;
                copiedItems[0]++;
                updateProgressBar("Copying: ",copiedItems[0], totalItems);
                return FileVisitResult.CONTINUE;
            }
        });
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("\nFolder copied successfully.");
        System.out.println("From: " + sourceFolderPath);
        System.out.println("To: " + destinationFolderPath);
        System.out.println("Folders copied: " + copiedFolders);
        System.out.println("Files copied: " + copiedFiles);
        System.out.println("Total time taken: " + formatDuration(duration));
    }

    public static void moveFile(String sourceFilePath, String destinationFilePath) throws IOException {
        long startTime = System.currentTimeMillis();
        Path sourcePath = Paths.get(sourceFilePath);
        Path destinationPath = Paths.get(destinationFilePath);

        // Move (rename) the file
        Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("File moved successfully.");
        System.out.println("Total time taken: " + formatDuration(duration));
    }

    public static void moveFolder(String sourceFolderPath, String destinationFolderPath) throws IOException {
        long startTime = System.currentTimeMillis();
        Path sourcePath = Paths.get(sourceFolderPath);
        Path destinationPath = Paths.get(destinationFolderPath);
        // Move (rename) the folder and its contents recursively
        Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Folder moved successfully.");
        System.out.println("Total time taken: " + formatDuration(duration));
    }

    public static void printFileInfo(String filePath) throws Exception {
        Path path = Paths.get(filePath);
        // Get basic file attributes
        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);

        // Print file properties
        System.out.println("File Name: " + path.getFileName());
        System.out.println("Absolute Path: " + path.toAbsolutePath());
        System.out.println("Creation Time: " + formatTime(attributes.creationTime()));
        System.out.println("Last Modified Time: " + formatTime(attributes.lastModifiedTime()));
        System.out.println("Last Access Time: " + formatTime(attributes.lastAccessTime()));
        System.out.println("Owner: " + Files.getOwner(path).getName());

        // Check if the path is a directory
        if (Files.isDirectory(path)) {
            // Display additional information for directories
            displayDirectoryInfo(path);
        }
        else{
            System.out.println("Size: " + utilsFunction.formatSize(attributes.size()));
        }
    }

    private static void displayDirectoryInfo(Path directoryPath) throws IOException {
        // Count files and directories within the specified directory
        long fileCount = Files.list(directoryPath).filter(Files::isRegularFile).count();
        long directoryCount = Files.list(directoryPath).filter(Files::isDirectory).count();


        System.out.println("Number of Files: " + fileCount);
        System.out.println("Number of Directories: " + directoryCount);

        // Calculate and display total size of the directory
        long totalSize = Files.walk(directoryPath)
                .mapToLong(p -> {
                    try {
                        return Files.size(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return 0L;
                    }
                })
                .sum();

        System.out.println("Size: " + utilsFunction.formatSize(totalSize));
    }

    public static List<Path> findFiles(String rootPath, String fileName, String fileExtension) throws IOException {
        List<Path> foundFiles = new ArrayList<>();

        Files.walkFileTree(Paths.get(rootPath), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if ((fileName == null || file.getFileName().toString().contains(fileName))
                        && (fileExtension == null || file.toString().endsWith("." + fileExtension))) {
                    foundFiles.add(file);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                // Handle error while visiting a file
                //System.err.println("Error visiting file: " + file.toString() + " - " + exc.getMessage());
                return FileVisitResult.CONTINUE;
            }
        });

        return foundFiles;
    }
    private static void updateProgressBar(int progress) {
        int width = 50; // width of the progress bar
        System.out.print("\r[");

        int i = 0;
        for (; i <= (progress * width) / 100; i++) {
            System.out.print("=");
        }

        for (; i < width; i++) {
            System.out.print(" ");
        }

        System.out.print("] " + progress + "%");
    }
    private static void updateProgressBar(String progressBarText,int copiedItems, int totalItems) {
        int progress = (int) ((double) copiedItems / totalItems * 100);
        System.out.print("\r"+ progressBarText + progress + "%");
        if (copiedItems == totalItems) {
            System.out.println(); // Move to the next line after completion
        }
    }
    public static String formatTime(FileTime fileTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(fileTime.toMillis()));
    }


    public static String formatDuration(long duration) {
        long milliseconds = duration % 1000;
        long seconds = (duration / 1000) % 60;
        long minutes = (duration / (1000 * 60)) % 60;
        //long hours = duration / (1000 * 60 * 60);

        return String.format("%02d:%02d:%04d", minutes, seconds, milliseconds);
    }
}
