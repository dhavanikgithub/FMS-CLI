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
import java.util.Scanner;

public class FMS_CLI {
    static int copiedFolders = 0;
    static int copiedFiles = 0;
    static int deletedFiles = 0;
    static int deletedFolders = 0;
    static int movedFolders = 0;
    static int movedFiles = 0;

    static int failedOperations = 0;


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
            System.out.println("File successfully renamed.");
        }
        // if renameTo() return false then else block is
        // executed
        else {
            // Check write permission
            if (!Files.isWritable(Paths.get(sourcePath))) {
                System.out.println("Permission denied");
            } else {
                System.out.println("Operation failed");
            }
        }
    }

    public static void renameFolder(String oldFolderPath, String newFolderName) throws IOException{
        Path oldPath = Paths.get(oldFolderPath);
        Path newPath = oldPath.resolveSibling(newFolderName);
        // Rename the folder
        Files.move(oldPath, newPath, StandardCopyOption.ATOMIC_MOVE);
        if(new File(newFolderName).exists())
        {
            System.out.println("Folder renamed successfully.");
        }
        else{
            System.out.println("Folder rename unsuccessful.");
        }
    }


    public static void deleteFolder(String folderPath) {

        long startTime = System.currentTimeMillis();
        failedOperations=0;
        Path folderToDelete = Paths.get(folderPath);
        final boolean[] skipALL = {false};
        final boolean[] terminate = {false};
        // Counter for tracking the number of deleted items
        final int[] deletedItems = {0};

        try{

            // Get the total number of files and subdirectories to delete
            int totalItems = (int) Files.walk(folderToDelete).count();

            // Delete the folder and its contents recursively
            Files.walkFileTree(folderToDelete, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!Files.isWritable(file)) {
                        failedOperations++;
                        System.out.print("\nFile: "+file.toAbsolutePath()+" : Permission denied\n");
                        if(!skipALL[0])
                        {
                            System.out.print("\n1. Skip Once\n");
                            System.out.println("2. SkipAll directories and files related to this");
                            System.out.println("3. Cancel Operation");
                            boolean condition = true;
                            Scanner sc = new Scanner(System.in);
                            while(condition)
                            {
                                System.out.print("Select Options: ");
                                String userOptionSelect = sc.nextLine();

                                switch (userOptionSelect){
                                    case "1" -> {condition=false;}
                                    case "2" -> {skipALL[0] =true;condition=false;}
                                    case "3" -> {terminate[0] = true; updateProgressBar("Deleting: ", deletedItems[0], totalItems); return FileVisitResult.TERMINATE;}
                                    default -> System.out.println("Incorrect option");
                                }
                            }
                            updateProgressBar("Deleting: ", deletedItems[0], totalItems);
                        }
                        return FileVisitResult.CONTINUE;
                    }
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
                    if(terminate[0])
                    {
                        return FileVisitResult.TERMINATE;
                    }
                    if (exc == null) {
                        if(!utilsFunction.isEmpty(dir))
                        {
                            return FileVisitResult.CONTINUE;
                        }
                        if (!Files.isWritable(dir)) {
                            failedOperations++;
                            System.out.print("\nDirectory: "+dir.toAbsolutePath()+" : Permission denied\n");
                            updateProgressBar("Deleting: ", deletedItems[0], totalItems);
                            return FileVisitResult.CONTINUE;
                        }
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
            if(terminate[0])
            {
                System.out.print("\nOperation Terminated.\n");
            }
            else{
                if(!new File(folderPath).exists())
                {
                    System.out.print("\nThe folder was deleted successfully.\n");
                }
                else{
                    System.out.print("\nFolder deletion was unsuccessful.\n");
                }
            }
        }
        catch (AccessDeniedException e) {
            System.out.print("\nAccess was denied while deleting the folder.\n");
        }
        catch (Exception ex)
        {
            System.err.print("\nError: "+ex+"\n");
        }
        finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;


            System.out.println("Deleted files: " + deletedFiles);
            System.out.println("Deleted folders: " + deletedFolders);
            System.out.println("Failed Delete: " + failedOperations);
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
            if(new File(destinationFilePath).exists())
            {
                System.out.println("\nThe file was copied successfully.");
            }
            else{
                System.out.println("\nFile copy was unsuccessful.");
            }
            System.out.println("From: "+sourceFilePath);
            System.out.println("To: "+destinationFilePath);
            System.out.println("Total time taken: " + formatDuration(duration));
        }
    }

    public static void copyFolder(String sourceFolderPath, String destinationFolderPath) throws IOException {
        long startTime = System.currentTimeMillis();
        Path sourcePath = Paths.get(sourceFolderPath);
        Path destinationPath = Paths.get(destinationFolderPath);
        final boolean[] skipALL = {false,false};
        final boolean[] terminate = {false};
        // Get the total number of files and subdirectories to copy
        int totalItems = (int) Files.walk(sourcePath).count();
        failedOperations=0;
        // Counter for tracking the number of copied items
        final int[] copiedItems = {0};

        // Copy the folder and its contents recursively
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (!Files.isReadable(dir)) {
                    failedOperations++;
                    System.out.print("\nFile: "+dir.toAbsolutePath()+" : Permission denied\n");
                    if(!skipALL[0])
                    {
                        Scanner sc = new Scanner(System.in);
                        System.out.print("\n1. Skip Once\n");
                        System.out.println("2. SkipAll directories and files related to this");
                        System.out.println("3. Cancel Operation");
                        boolean condition = true;
                        while(condition)
                        {
                            System.out.print("Select Options: ");

                            String userOptionSelect = sc.nextLine();

                            switch (userOptionSelect){
                                case "1" -> {condition=false;}
                                case "2" -> {skipALL[0] =true;condition=false;}
                                case "3" -> {terminate[0] = true; updateProgressBar("Copying: ",copiedItems[0], totalItems); return FileVisitResult.TERMINATE;}
                                default -> System.out.println("Incorrect option");
                            }
                        }
                        updateProgressBar("Copying: ",copiedItems[0], totalItems);
                    }
                    return FileVisitResult.CONTINUE;
                }
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
                if(terminate[0])
                    return  FileVisitResult.TERMINATE;
                if (!Files.isReadable(file)) {
                    failedOperations++;
                    System.out.print("\nFile: "+file.toAbsolutePath()+" : Permission denied\n");
                    if(!skipALL[0])
                    {
                        Scanner sc = new Scanner(System.in);
                        System.out.print("\n1. Skip Once\n");
                        System.out.println("2. SkipAll directories and files related to this");
                        System.out.println("3. Cancel Operation");
                        boolean condition = true;
                        while(condition)
                        {
                            System.out.print("Select Options: ");

                            String userOptionSelect = sc.nextLine();

                            switch (userOptionSelect){
                                case "1" -> {condition=false;}
                                case "2" -> {skipALL[0] =true;condition=false;}
                                case "3" -> {terminate[0] = true; updateProgressBar("Copying: ",copiedItems[0], totalItems); return FileVisitResult.TERMINATE;}
                                default -> System.out.println("Incorrect option");
                            }
                        }
                        updateProgressBar("Copying: ",copiedItems[0], totalItems);
                    }

                    return FileVisitResult.CONTINUE;
                }
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
        if(terminate[0])
        {
            System.out.println("\nOperation Terminated.");
        }
        else{
            if(new File(destinationFolderPath).exists())
            {
                System.out.println("\nThe folder has been copied successfully.");
            }
            else{
                System.out.println("\n");
            }
        }

        System.out.println("From: " + sourceFolderPath);
        System.out.println("To: " + destinationFolderPath);
        System.out.println("Folders copied: " + copiedFolders);
        System.out.println("Files copied: " + copiedFiles);
        System.out.println("Failed to copy: " + failedOperations);
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
        if(!new File(sourceFilePath).exists() && new File(destinationFilePath).exists())
        {
            System.out.println("File moved successfully.");
        }

        System.out.println("Total time taken: " + formatDuration(duration));
    }

    public static void moveFolder(String sourceFolderPath, String destinationFolderPath) {
        long startTime = System.currentTimeMillis();
        try{
            Path sourcePath = Paths.get(sourceFolderPath);
            Path destinationPath = Paths.get(destinationFolderPath);
            // Get the total number of files and subdirectories to copy
            int totalItems = (int) Files.walk(sourcePath).count();
            failedOperations=0;
            // Counter for tracking the number of copied items
            final int[] movedItems = {0};
            final boolean[] skipALL = {false,false};
            final boolean[] terminate = {false};
            Files.walkFileTree(sourcePath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (!Files.isWritable(dir)) {
                        failedOperations++;
                        System.out.print("\nDirectory: "+dir.toAbsolutePath()+" : Permission denied\n");
                        if(!skipALL[0])
                        {
                            Scanner sc = new Scanner(System.in);
                            System.out.print("\n1. Skip Once\n");
                            System.out.println("2. SkipAll directories and files related to this");
                            System.out.println("3. Cancel Operation");
                            boolean condition = true;
                            while(condition)
                            {
                                System.out.print("Select Options: ");

                                String userOptionSelect = sc.nextLine();

                                switch (userOptionSelect){
                                    case "1" -> {condition=false;}
                                    case "2" -> {skipALL[0] =true;condition=false;}
                                    case "3" -> {terminate[0] = true; updateProgressBar("Moving: ",movedItems[0], totalItems); return FileVisitResult.TERMINATE;}
                                    default -> System.out.println("Incorrect option");
                                }
                            }
                            updateProgressBar("Moving: ",movedItems[0], totalItems);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                    Path targetPath = destinationPath.resolve(sourcePath.relativize(dir));
                    Files.createDirectories(targetPath);
                    movedFolders++;
                    movedItems[0]++;
                    updateProgressBar("Moving: ",movedItems[0], totalItems);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if(terminate[0])
                    {
                        return FileVisitResult.TERMINATE;
                    }
                    if (!Files.isWritable(file)) {
                        failedOperations++;
                        System.out.print("\nFile: "+file.toAbsolutePath()+" : Permission denied\n");
                        if(!skipALL[0])
                        {
                            Scanner sc = new Scanner(System.in);
                            System.out.print("\n1. Skip Once\n");
                            System.out.println("2. SkipAll directories and files related to this");
                            System.out.println("3. Cancel Operation");
                            boolean condition = true;
                            while(condition)
                            {
                                System.out.print("Select Options: ");

                                String userOptionSelect = sc.nextLine();

                                switch (userOptionSelect){
                                    case "1" -> {condition=false;}
                                    case "2" -> {skipALL[0] =true;condition=false;}
                                    case "3" -> {terminate[0] = true; updateProgressBar("Moving: ",movedItems[0], totalItems); return FileVisitResult.TERMINATE;}
                                    default -> System.out.println("Incorrect option");
                                }
                            }
                            updateProgressBar("Moving: ",movedItems[0], totalItems);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                    Path targetPath = destinationPath.resolve(sourcePath.relativize(file));
                    Files.move(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    movedFiles++;
                    movedItems[0]++;
                    updateProgressBar("Moving: ",movedItems[0], totalItems);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    if(terminate[0])
                    {
                        return FileVisitResult.TERMINATE;
                    }
                    // Handle failure to visit a file (e.g., permission issues)
                    System.out.println("Failed to visit file: " + file.toString());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if(terminate[0])
                    {
                        return FileVisitResult.TERMINATE;
                    }
                    if(!utilsFunction.isEmpty(dir))
                    {
                        return FileVisitResult.CONTINUE;
                    }
                    if (!Files.isWritable(dir)) {
                        return FileVisitResult.CONTINUE;
                    }
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            // Move (rename) the folder and its contents recursively
            //Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            if(terminate[0])
            {
                System.out.println("\nOperation terminated.");
            }
            else{
                if(!new File(sourceFolderPath).exists() && new File(destinationFolderPath).exists())
                {
                    System.out.println("\nFolder moved successfully.");
                }
                else{
                    System.out.println("\nFolder move unsuccessful.");
                }
            }

        }
        catch (DirectoryNotEmptyException dne)
        {
            System.out.printf("%nFolder move unsuccessful : [%s]%n",dne);
        }
        catch (FileAlreadyExistsException e) {
            System.out.printf("%nDestination file already exists: [%s]%n",e);
        } catch (AccessDeniedException e) {
            System.out.printf("%nAccess denied: [%s]%n",e);
        } catch (SecurityException e) {
            System.out.printf("%nSecurity exception: [%s]%n",e);
        }
        catch (IOException e) {
            System.out.printf("%nAn I/O error occurred: [%s]%n",e);
        }
        catch (Exception ex)
        {
            System.out.printf("%nUnknown error occurred: [%s]%n",ex);
        }
        finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("From: " + sourceFolderPath);
            System.out.println("To: " + destinationFolderPath);
            System.out.println("Folders moved: " + movedFolders);
            System.out.println("Files moved: " + movedFiles);
            System.out.println("Failed to move: " + failedOperations);
            System.out.printf("Total time taken: %s%n",formatDuration(duration));
        }

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
