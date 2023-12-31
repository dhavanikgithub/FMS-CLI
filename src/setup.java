import org.apache.commons.lang3.SystemUtils;
import utiles.utilsFunction;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class setup{
    public static String mainDirPath = "root";
    public static String currentDirPath;
    public static RegularExpressionPattern rep = null;
    public static Scanner userInput = null;
    public static File[] currentListedDirectories;
    public static File[] currentListedFiles;
    public static String pathSeparator = "/";

    public static void main(String[] args){
        if(!SystemUtils.IS_OS_WINDOWS && !SystemUtils.IS_OS_LINUX && SystemUtils.IS_OS_MAC)
        {
            System.out.println("This application may not work properly in your OS");
            System.out.println("It is Designed for Windows | Linux | MAC OS\n");
        }
        currentDirPath = mainDirPath;
        userInput = new Scanner(System.in);
        rep = new RegularExpressionPattern();
        listFilesAndFolders();

        for(;;)
        {
            System.out.print(currentDirPath.toLowerCase()+">: ");
            String op = (userInput.nextLine()).strip();
            if(!Objects.equals(op, ""))
            {

                if (rep.matchPattern(rep.exitPattern,op)) {
                    System.exit(0);
                }
                else if(rep.matchPattern(rep.createFolderPattern,op))
                {
                    operationCreateFolder(op);
                }
                else if(rep.matchPattern(rep.createFilePattern,op))
                {
                    operationCreateFile(op);
                }
                else if(rep.matchPattern(rep.openFolderPattern,op))
                {
                    operationOpenFolder(op);
                }
                else if (rep.matchPattern(rep.listFolderPattern,op))
                {
                    listFilesAndFolders();
                }
                else if (rep.matchPattern(rep.backToFolderPattern,op))
                {
                    backToPath();
                }
                else if (rep.matchPattern(rep.renameFolderPattern,op))
                {
                    operationRenameFolderBasic(op);
                }
                else if (rep.matchPattern(rep.renameFilePattern,op))
                {
                    operationRenameFileBasic(op);
                }
                else if(rep.matchPattern(rep.deleteFolderPattern,op))
                {
                    operationDeleteFolder(op);
                }
                else if(rep.matchPattern(rep.deleteFilePattern,op))
                {
                    operationDeleteFiles(op);
                }
                else if(rep.matchPattern(rep.copyFolderPattern,op))
                {
                    operationCopyAndNavPastFolder(op);
                }
                else if(rep.matchPattern(rep.copyFilePattern,op))
                {
                    operationCopyAndNavPastFile(op);
                }
                else if(rep.matchPattern(rep.moveFolderPattern,op))
                {
                    operationMoveAndNavPastFolder(op);
                }
                else if(rep.matchPattern(rep.moveFilePattern,op))
                {
                    operationMoveAndNavPastFile(op);
                }
                else if(rep.matchPattern(rep.filePropertyPattern,op))
                {
                    viewFileProperties(op);
                }
                else if(rep.matchPattern(rep.findFileGlobPattern,op))
                {
                    operationFindFiles(op,true);
                }
                else if(rep.matchPattern(rep.findFilePattern,op))
                {
                    operationFindFiles(op,false);
                }
                else if (rep.matchPattern(rep.selectFilePattern,op)) {
                    operationSelectFile(op);

                }
                else if (rep.matchPattern(rep.selectFolderPattern,op)) {
                    operationSelectFolder(op);
                }
                else{
                    HelpText.matchCommand(op);
                }

            }
        }
    }


    public static void operationSelectFile(String op)
    {
        if(currentListedFiles==null || currentListedFiles.length==0)
        {
            System.out.println("No any files found");
            return;
        }
        int extractedFileNumber;
        try{
            extractedFileNumber = Integer.parseInt(op.replaceFirst(rep.selectFilePattern.toString(),"").strip());
        }
        catch (Exception ex)
        {
            extractedFileNumber = 0;
        }
        if(extractedFileNumber>currentListedFiles.length || extractedFileNumber<=0)
        {
            System.out.println("Invalid file number found");
            return;
        }
        File filePath = currentListedFiles[extractedFileNumber-1];
        if(!filePath.exists() || !filePath.isFile())
        {
            System.out.println("File not found");
            return;
        }
        System.out.println("File selected");
        System.out.print(filePath.getName()+"> ");
        String fileOperation = userInput.nextLine().strip().toLowerCase(Locale.ENGLISH);
        switch (fileOperation) {
            case "cp" -> operationCopyAndNavPastFile("cp -file " + filePath.getName());
            case "mv" -> operationMoveAndNavPastFile("mv -file " + filePath.getName());
            case "del" -> operationDeleteFiles("del -file \"" + filePath.getName()+"\"");
            case "rename" -> operationRenameFileBasic("rename -file " + filePath.getName());
            case "prp" -> viewFileProperties("prp " + filePath.getName());
            case "exit" ->  System.exit(0);
        }
    }

    public static void operationSelectFolder(String op)
    {
        if(currentListedDirectories==null || currentListedDirectories.length==0)
        {
            System.out.println("No any folder found");
            return;
        }
        int extractedFolderNumber;
        try{
            extractedFolderNumber = Integer.parseInt(op.replaceFirst(rep.selectFolderPattern.toString(),"").strip());
        }
        catch (Exception ex)
        {
            extractedFolderNumber = 0;
        }
        if(extractedFolderNumber>currentListedDirectories.length || extractedFolderNumber<=0)
        {
            System.out.println("Invalid folder number found");
            return;
        }
        File folderPath = currentListedDirectories[extractedFolderNumber-1];
        if(!folderPath.exists() || !folderPath.isDirectory())
        {
            System.out.println("Folder not found");
            return;
        }
        System.out.println("Folder selected");
        System.out.print(folderPath.getName()+"> ");
        String fileOperation = userInput.nextLine().strip();
        switch (fileOperation) {
            case "cp" -> operationCopyAndNavPastFolder("cp -dir " + folderPath.getName());
            case "mv" -> operationMoveAndNavPastFolder("mv -dir " + folderPath.getName());
            case "del" -> operationDeleteFolder("del -dir " + folderPath.getName());
            case "rename" -> operationRenameFolderBasic("rename -dir " + folderPath.getName());
            case "prp" -> viewFileProperties("prp " + folderPath.getName());
            case "open" -> operationOpenFolder("open " + folderPath.getName());
            case "list" -> {
                currentDirPath = folderPath.getPath();
                listFilesAndFolders();
            }
            case "exit" ->  System.exit(0);
        }
    }
    public static void operationFindFiles(String op, Boolean isGloble){
        try {
            ProgressBarRotating pb1 = new ProgressBarRotating();
            pb1.start();
            long startTime = System.currentTimeMillis();
            //System.out.println("Finding....");
            File[] rootDrive = File.listRoots();
            List<Path> foundFiles=null;

            if(isGloble)
            {
                String fileName = op.replace("find -g ","").strip();
                for(File sysDrive : rootDrive){
                    if (foundFiles==null)
                    {
                        foundFiles = FMS_CLI.findFiles(sysDrive.getPath(), fileName, null);
                    }
                    else{
                        foundFiles.addAll(FMS_CLI.findFiles(sysDrive.getPath(), fileName, null));
                    }
                }
            }
            else{
                String fileName = op.replace("find ","").strip();
                if(currentDirPath.equals(mainDirPath))
                {
                    for(File sysDrive : rootDrive){
                        if (foundFiles==null)
                        {
                            foundFiles = FMS_CLI.findFiles(sysDrive.getPath(), fileName, null);
                        }
                        else{
                            foundFiles.addAll(FMS_CLI.findFiles(sysDrive.getPath(), fileName, null));
                        }
                    }
                }
                else{
                    foundFiles = FMS_CLI.findFiles(currentDirPath, fileName, null);
                }
            }
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            pb1.showProgress = false;
            if (!Objects.requireNonNull(foundFiles).isEmpty()) {
                System.out.println("\rFound files:");
                for (Path file : foundFiles) {
                    System.out.println(file);
                }
                System.out.println("Total "+foundFiles.size()+" files found");
                System.out.println("Total time taken: " + FMS_CLI.formatDuration(duration));;
            } else {
                System.out.println("\rNo matching files found.");
            }
        }
        catch (Exception ex)
        {
            System.out.println("\rInvalid Input");
        }
    }

    public static void backToPath(){
        try{
            if(currentDirPath.length()>0)
            {
                if(!(currentDirPath.equals(mainDirPath)))
                {
                    File path = new File(currentDirPath);

                    if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC)
                    {
                        if (currentDirPath.equals("/"))
                        {
                            currentDirPath = mainDirPath;
                        }
                        else{
                            currentDirPath = path.getParent();
                        }
                    }
                    else{
                        if(path.getParent() == null)
                        {
                            currentDirPath = mainDirPath;
                        }
                        else{
                            currentDirPath = path.getParent();
                        }
                    }
                }
                listFilesAndFolders();
            }
            else{
                System.out.println("Empty Current Path");
            }
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            System.out.println("Array index out of bound");
        }
    }
    public static void listFilesAndFolders()
    {
        if (currentDirPath.equals(mainDirPath))
        {
            currentDirPath = mainDirPath;
            File[] rootDrive = File.listRoots();

            for(File sysDrive : rootDrive){
                System.out.println("Drive : " + sysDrive);
            }
            System.out.println("\nhelp (Show the command list)");
            System.out.println("exit : Terminate application\n");
            return;
        }
        try {
            File mainDir = new File(currentDirPath);

            if (!(mainDir.exists() && mainDir.isDirectory())) {
                System.out.println("Folder not found");
                return;
            }

            System.out.println("**********************************************");
            System.out.println("Files and directories from directory : " + mainDir);
            System.out.println("**********************************************\n");

            File[] filesAndFolders = mainDir.listFiles();

            if (filesAndFolders != null) {
                // Separate directories and files
                currentListedDirectories = Arrays.stream(filesAndFolders)
                        .filter(File::isDirectory)
                        .toArray(File[]::new);

                currentListedFiles = Arrays.stream(filesAndFolders)
                        .filter(File::isFile)
                        .toArray(File[]::new);
                if(currentListedDirectories.length == 0 && currentListedFiles.length == 0)
                {
                    System.out.println("Empty folder");
                }
                else{
                    if(currentListedDirectories.length>1){
                        // Sort directories
                        Arrays.sort(currentListedDirectories, Comparator.comparing(File::getName));
                    }
                    if(currentListedFiles.length>1)
                    {
                        // Sort files
                        Arrays.sort(currentListedFiles, Comparator.comparing(File::getName));
                    }

                    System.out.println("------Directory------");

                    if(currentListedDirectories.length==0)
                    {
                        System.out.println("No any directories found");
                    }
                    else{
                        int counter = 1;
                        // Print directories
                        for (File directory : currentListedDirectories) {
                            String permissions = utilsFunction.getPermissions(directory);
                            String formattedName = directory.getName();
                            System.out.printf("%-5d %s%s %-9s %-20s%n", counter++, "d", permissions, "", formattedName);
                        }
                    }

                    System.out.println("------Files------");
                    // Print files
                    if(currentListedFiles.length==0)
                    {
                        System.out.println("No any files found");
                    }
                    else{
                        int counter = 1;
                        for (File file : currentListedFiles) {
                            String fileType = "-";
                            String permissions = utilsFunction.getPermissions(file);
                            String size = utilsFunction.formatSize(file.length());
                            String formattedName = file.getName();
                            System.out.printf("%-5d %s%s %-10s %-20s%n", counter++,fileType, permissions, size, formattedName);

                        }
                    }
                }

            } else {
                System.out.println("Empty Folder");
            }

            System.out.println("\nhelp (Show the command list)");
            System.out.println("exit : Terminate application\n");
        } catch (Exception ex) {
            System.out.println("Something went wrong");
        }
    }

    private static void operationCreateFolder(String op)
    {
        try{
            String extractedFolderName = op.replaceFirst(rep.createFolderPattern.toString(),"").strip();
            if(!utilsFunction.isValidFolderName(extractedFolderName))
            {
                return;
            }
            File file = new File(extractedFolderName);
            if (!file.isAbsolute())
            {
                file = new File(currentDirPath+pathSeparator+file.getPath());
            }
            if(rep.isValidFolderName(file.getName()))
            {
                if (!Files.isWritable(Paths.get(file.getParent())))
                {
                    System.out.println("Permission denied to create folder");
                    return;
                }
                FMS_CLI.createFolder(file.getPath());
            }
            else{
                System.out.println("folder name not contain / \\ : ? * < > \" |");
            }
        }
        catch (Exception ex)
        {
            System.out.println("Invalid Input");
        }
    }

    private static void operationCreateFile(String op)
    {
        try{
            String extractedFileName = op.replaceFirst(rep.createFilePattern.toString(),"").strip();
            ArrayList<String> fileNames = utilsFunction.parseCommand(extractedFileName);
            for(String filename : fileNames)
            {
                if(!utilsFunction.isValidFileName(filename))
                {
                    continue;
                }
                File file = new File(Objects.requireNonNull(filename));
                if (!file.isAbsolute())
                {
                    file = new File(currentDirPath+pathSeparator+filename);
                }
                if (!Files.isWritable(Paths.get(file.getParent())))
                {
                    System.out.println("Permission denied to create the file.");
                    return;
                }
                FMS_CLI.createFile(file.getPath());
            }
            /*if(!utilsFunction.isValidFileName(extractedFileName))
            {
                return;
            }
            File file = new File(Objects.requireNonNull(extractedFileName));
            if (!file.isAbsolute()) {
                file = new File(currentDirPath+pathSeparator+extractedFileName);
            }
            if (!Files.isWritable(Paths.get(file.getParent()))) {
                System.out.println("Permission denied to create file");
                return;
            }
            FMS_CLI.createFile(file.getPath());*/
        }
        catch (Exception ex)
        {
            System.out.println("Invalid FileName");
        }
    }

    private static void operationOpenFolder(String op)
    {
        String extractedFolderName = op.replaceFirst(rep.openFolderPattern.toString(),"").strip();

        File file = new File(extractedFolderName);
        if(file.getPath().equalsIgnoreCase(currentDirPath))
        {
            return;
        }
        if(extractedFolderName.equals(mainDirPath))
        {
            currentDirPath = mainDirPath;
            listFilesAndFolders();
            return;
        }
        if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC)
        {
            if(currentDirPath.equals("/"))
            {
                file = new File("/"+extractedFolderName);
            }
            else{
                if (!file.isAbsolute()) {
                    file = new File(currentDirPath + pathSeparator + file.getPath());
                }
            }
        }
        else{
            if(!SystemUtils.IS_OS_WINDOWS)
            {
                System.out.println("This app may be not work properly because it's not designed for this os\n");
            }
            if (!file.isAbsolute()) {
                file = new File(currentDirPath + pathSeparator + file.getPath());
            }
        }
        if(file.exists() && file.isDirectory())
        {
            currentDirPath = file.getPath();
            listFilesAndFolders();
        }
        else{
            System.out.println("Folder not found");
        }

    }

    private static void operationRenameFolderBasic(String op){
        try{
            String extractedFolderName = op.replaceFirst(rep.renameFolderPattern.toString(),"").strip();
            File sourcePath = new File(extractedFolderName);
            File destinationPath;
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath+pathSeparator+sourcePath.getPath());
            }
            if(!sourcePath.exists() || sourcePath.isFile() || sourcePath.getPath().equals(currentDirPath) || sourcePath.getName().equals("."))
            {
                System.out.println("Folder not found");
                return;
            }
            if (!Files.isWritable(Paths.get(sourcePath.getPath()))) {
                System.out.println(sourcePath.getName()+" : Permission denied");
                return;
            }
            System.out.print("New folder name: ");
            String newFolderName = userInput.nextLine().strip();
            if(!utilsFunction.isValidFolderName(newFolderName))
            {
                return;
            }
            destinationPath = new File(sourcePath.getParentFile()+pathSeparator+newFolderName);
            if(destinationPath.exists() && destinationPath.isFile())
            {
                System.out.println(newFolderName + " : Folder name already exist");
                return;
            }
            FMS_CLI.renameFolder(sourcePath.getPath(),destinationPath.getPath());
        }
        catch (Exception ex)
        {
            System.out.println("Due to invalid input failed to rename");
        }
    }

    private static void operationRenameFileBasic(String op)
    {
        try{
            String extractedFolderName = op.replaceFirst(rep.renameFilePattern.toString(),"").strip();
            File sourcePath = new File(extractedFolderName);
            File destinationPath;
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath+pathSeparator+sourcePath.getPath());
            }
            if(!sourcePath.exists())
            {
                System.out.println(sourcePath.getName()+" : File not found");
                return;
            }
            if (!Files.isWritable(Paths.get(sourcePath.getPath()))) {
                System.out.println(sourcePath.getName()+" : Permission denied");
                return;
            }
            System.out.print("New file name: ");
            String newFileName = userInput.nextLine().strip();
            if(!utilsFunction.isValidFileName(newFileName))
            {
                return;
            }
            destinationPath = new File(sourcePath.getParentFile()+pathSeparator+newFileName);
            if(destinationPath.exists() && destinationPath.isFile())
            {
                System.out.println(newFileName + " : File name already exist");
                return;
            }

            FMS_CLI.renameFile(sourcePath.getPath(),destinationPath.getPath());

        }
        catch (Exception ex)
        {
            System.out.println("Failed to rename the file. Invalid input");
        }
    }

    private static void operationDeleteFolder(String op)
    {
        try{
            while (true)
            {
                System.out.print("Are you sure you want to delete the folder? (YES|NO): ");
                String deleteFolderConfirmation = userInput.nextLine().strip();
                if(deleteFolderConfirmation.equalsIgnoreCase("no"))
                {
                    break;
                }
                else if(deleteFolderConfirmation.equalsIgnoreCase("yes"))
                {
                    String extractedFolderName = op.replaceFirst(rep.deleteFolderPattern.toString(),"").strip();

                    File folderPath = new File(extractedFolderName);
                    if(!folderPath.isAbsolute())
                    {
                        folderPath = new File(currentDirPath+"//"+folderPath.getPath());
                    }
                    if(!folderPath.exists() || folderPath.isFile() || folderPath.getPath().equals(currentDirPath) || folderPath.getName().equals("."))
                    {
                        System.out.println("Folder not found");
                        return;
                    }
                    if (!Files.isWritable(Paths.get(folderPath.getParent()))) {
                        System.out.println("Permission denied to delete the folder.");
                        return;
                    }
                    FMS_CLI.deleteFolder(folderPath.getPath());
                    return;
                }
            }

        }
        catch (Exception ex)
        {
            System.out.println("\nInvalid input detected.");
        }
    }

    private static void operationDeleteFiles(String op)
    {
        try{
            while (true)
            {
                System.out.print("Are you sure you want to delete the files? (YES|NO): ");
                String deleteFileConfirmation = userInput.nextLine().strip();
                if(deleteFileConfirmation.equalsIgnoreCase("no"))
                {
                    break;
                }
                else if(deleteFileConfirmation.equalsIgnoreCase("yes"))
                {
                    String extractedFileName = op.replaceFirst(rep.deleteFilePattern.toString(),"");
                    ArrayList<String> fileNames = utilsFunction.parseCommand(extractedFileName);
                    for(String fileName : fileNames){

                        File fileObject = new File(fileName);
                        if(!fileObject.isAbsolute())
                        {
                            fileObject = new File(currentDirPath+"//"+fileName);
                        }
                        if(fileObject.exists() && fileObject.isFile())
                        {
                            if (!Files.isWritable(Paths.get(fileObject.getPath()))) {
                                System.out.println(fileObject.getName()+" : Permission denied");
                                continue;
                            }
                            if (fileObject.delete()) {
                                System.out.println(fileObject.getName()+" : Deleted");
                            } else {
                                System.out.println(fileObject.getName()+" : Failed to Delete");
                            }
                        }
                        else{
                            System.out.println(fileObject.getName()+" : file not found");
                        }
                    }
                    return;
                }
            }

        }
        catch (Exception ex)
        {
            System.out.println("Invalid Input");
        }
    }

    private static void operationCopyAndNavPastFolder(String op)
    {
        try{
            String extractedFolderName = op.replaceFirst(rep.copyFolderPattern.toString(),"").strip();
            File sourcePath = new File(extractedFolderName);
            File destinationPath;
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath+"//"+sourcePath.getPath());
            }

            if(!sourcePath.exists() || sourcePath.isFile())
            {
                System.out.println("Folder not found");
                return;
            }

            while (true){
                System.out.print("["+currentDirPath.toLowerCase()+"] Paste (Y|N) or Navigate: ");
                String newOperation = userInput.nextLine();
                if(!newOperation.isEmpty())
                {

                    if(newOperation.equalsIgnoreCase("y"))
                    {
                        if(currentDirPath.equals(mainDirPath))
                        {
                            System.out.println("Unable to paste.");
                            continue;
                        }
                        destinationPath = new File(currentDirPath+"//"+sourcePath.getName());
                        if(destinationPath.exists() && destinationPath.isDirectory())
                        {
                            System.out.println("A folder name already exists at the destination.");
                            break;
                        }
                        if (!Files.isReadable(Paths.get(sourcePath.getPath()))) {
                            System.out.println(sourcePath.getPath()+" : Permission denied to read folder");
                            break;
                        }
                        if (!Files.isWritable(Paths.get(destinationPath.getParent())))
                        {
                            System.out.println(destinationPath.getPath()+" : Permission denied to write folder");
                            break;
                        }
                        FMS_CLI.copyFolder(sourcePath.getPath(),destinationPath.getPath());
                        break;
                    }
                    else if(newOperation.equalsIgnoreCase("n"))
                    {
                        break;
                    } else if (rep.matchPattern(rep.openFolderPattern,newOperation)) {
                        operationOpenFolder(newOperation);
                    }
                    else if(rep.matchPattern(rep.backToFolderPattern,newOperation))
                    {
                        backToPath();
                    } else if (newOperation.equalsIgnoreCase("exit")) {
                        System.exit(0);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            System.out.println("Invalid Input");
        }
    }

    private static void operationCopyAndNavPastFile(String op)
    {
        try{
            String extractedFileName = op.replaceFirst(rep.copyFilePattern.toString(),"").strip();
            File sourcePath = new File(extractedFileName);
            File destinationPath;

            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath+"//"+sourcePath.getPath());
            }
            if(sourcePath.exists() && sourcePath.isFile())
            {
                while (true){
                    System.out.print("["+currentDirPath.toLowerCase()+"] Paste (Y|N) or Navigate: ");
                    String newOperation = userInput.nextLine();
                    if(!newOperation.isEmpty())
                    {
                        if(newOperation.equalsIgnoreCase("y"))
                        {
                            if(currentDirPath.equals(mainDirPath))
                            {
                                System.out.println("Not possible to past");
                                continue;
                            }
                            destinationPath = new File(currentDirPath+"//"+sourcePath.getName());
                            if(destinationPath.exists() && destinationPath.isFile())
                            {
                                System.out.println("A file name already exists at the destination.");
                                break;
                            }
                            if (!Files.isReadable(Paths.get(sourcePath.getPath()))) {
                                System.out.println(sourcePath.getPath()+" : Permission denied to read file");
                                break;
                            }
                            if(!Files.isWritable(Paths.get(destinationPath.getParent())))
                            {
                                System.out.println(destinationPath.getPath()+" : Permission denied to write file");
                                break;
                            }
                            FMS_CLI.copyFile(sourcePath.getPath(),destinationPath.getPath());
                            break;
                        }
                        else if(newOperation.equalsIgnoreCase("n"))
                        {
                            break;
                        } else if (rep.matchPattern(rep.openFolderPattern,newOperation)) {
                            operationOpenFolder(newOperation);
                        }
                        else if(rep.matchPattern(rep.backToFolderPattern,newOperation))
                        {
                            backToPath();
                        }
                        else if (newOperation.equalsIgnoreCase("exit")) {
                            System.exit(0);
                        }
                    }
                }
            }
            else{
                System.out.println("File not found");
            }
        }
        catch (Exception ex)
        {
            System.out.println("Invalid Input");
        }
    }

    private static void operationMoveAndNavPastFolder(String op)
    {
        try{
            String extractedFileName = op.replaceFirst(rep.moveFolderPattern.toString(),"").strip();
            File sourcePath = new File(extractedFileName);
            File destinationPath;
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath+"//"+sourcePath.getPath());
            }

            if(!sourcePath.exists() || sourcePath.isFile() || sourcePath.getPath().equals(currentDirPath) || sourcePath.getName().equals("."))
            {
                System.out.println("Folder not found");
                return;
            }

            while (true){
                System.out.print("["+currentDirPath.toLowerCase()+"] Paste (Y|N) or Navigate: ");
                String newOperation = userInput.nextLine();
                if(!newOperation.isEmpty())
                {
                    if(newOperation.equalsIgnoreCase("y"))
                    {
                        if(currentDirPath.equals(mainDirPath))
                        {
                            System.out.println("Unable to paste.");
                            continue;
                        }
                        destinationPath = new File(currentDirPath+"//"+sourcePath.getName());
                        if(destinationPath.exists() && destinationPath.isDirectory())
                        {
                            System.out.println("A folder name already exists at the destination.");
                            break;
                        }
                        if (!Files.isWritable(Paths.get(sourcePath.getPath()))) {
                            System.out.println(sourcePath.getPath()+" : Permission denied at source");
                            break;
                        }
                        if(!Files.isWritable(Paths.get(destinationPath.getParent())))
                        {
                            System.out.println(destinationPath.getPath()+" : Permission denied at destination");
                            break;
                        }
                        FMS_CLI.moveFolder(sourcePath.getPath(),destinationPath.getPath());
                        break;
                    }
                    else if(newOperation.equalsIgnoreCase("n"))
                    {
                        break;
                    } else if (rep.matchPattern(rep.openFolderPattern,newOperation)) {
                        operationOpenFolder(newOperation);
                    }
                    else if(rep.matchPattern(rep.backToFolderPattern,newOperation))
                    {
                        backToPath();
                    }
                    else if (newOperation.equalsIgnoreCase("exit")) {
                        System.exit(0);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }

    }

    private static void operationMoveAndNavPastFile(String op)
    {
        try{
            String extractedFileName = op.replaceFirst(rep.moveFilePattern.toString(),"").strip();
            File sourcePath = new File(extractedFileName);
            File destinationPath;
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath+"//"+sourcePath.getPath());
            }

            if(!sourcePath.exists() || sourcePath.isDirectory())
            {
                System.out.println("File not found");
                return;
            }
            while (true){
                System.out.print("["+currentDirPath.toLowerCase()+"] Paste (Y|N) or Navigate: ");
                String newOperation = userInput.nextLine();
                if(!newOperation.isEmpty())
                {
                    if(newOperation.equalsIgnoreCase("y"))
                    {
                        if(currentDirPath.equals(mainDirPath))
                        {
                            System.out.println("Unable to paste.");
                            continue;
                        }
                        destinationPath = new File(currentDirPath+"//"+sourcePath.getName());
                        if(destinationPath.exists() && destinationPath.isFile())
                        {
                            System.out.println("A file name already exists at the destination.");
                            break;
                        }
                        if (!Files.isWritable(Paths.get(sourcePath.getPath()))) {
                            System.out.println(sourcePath.getPath()+" : Permission denied at source");
                            break;
                        }
                        if(!Files.isWritable(Paths.get(destinationPath.getParent())))
                        {
                            System.out.println(destinationPath.getPath()+" : Permission denied at destination");
                            break;
                        }
                        FMS_CLI.moveFile(sourcePath.getPath(),destinationPath.getPath());
                        break;
                    }
                    else if(newOperation.equalsIgnoreCase("n"))
                    {
                        break;
                    } else if (rep.matchPattern(rep.openFolderPattern,newOperation)) {
                        operationOpenFolder(newOperation);
                    }
                    else if(rep.matchPattern(rep.backToFolderPattern,newOperation))
                    {
                        backToPath();
                    }
                    else if (newOperation.equalsIgnoreCase("exit")) {
                        System.exit(0);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            System.out.println("Invalid Input");
        }

    }

    private static void viewFileProperties(String op)
    {
        try{
            String extractedFileName = op.replaceFirst(rep.filePropertyPattern.toString(),"");
            File filePath = new File(extractedFileName);
            if(!filePath.isAbsolute())
            {
                filePath = new File(currentDirPath+"//"+filePath.getPath());
            }
            if(!filePath.exists())
            {
                System.out.println("Item not found");
                return;
            }
            FMS_CLI.printFileInfo(filePath.getPath());
        }
        catch (Exception ex)
        {
            System.out.println("Invalid Input");
        }

    }

}