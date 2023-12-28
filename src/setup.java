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


    public static void main(String[] args){
        currentDirPath = mainDirPath;
        userInput = new Scanner(System.in);
        rep = new RegularExpressionPattern();
        listFilesAndFolders();

        while(true)
        {
            System.out.print(currentDirPath+">: ");
            String op = (userInput.nextLine()).strip();
            if(!Objects.equals(op, ""))
            {
                if (rep.matchPattern(rep.exitPattern,op)) {
                    return;
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
                else{
                    HelpText.matchCommand(op);
                }

            }
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
            if(currentDirPath.length()>1)
            {
                if(!(currentDirPath.equals(mainDirPath)))
                {
                    File path = new File(currentDirPath);
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
            System.out.println("exit|quit|stop : Terminate application\n");
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
                File[] directories = Arrays.stream(filesAndFolders)
                        .filter(File::isDirectory)
                        .toArray(File[]::new);

                File[] files = Arrays.stream(filesAndFolders)
                        .filter(File::isFile)
                        .toArray(File[]::new);

                // Sort directories and files
                Arrays.sort(directories, Comparator.comparing(File::getName));
                Arrays.sort(files, Comparator.comparing(File::getName));

                System.out.println("------Directory------");
                // Print directories
                for (File directory : directories) {
                    String permissions = utilsFunction.getPermissions(directory);
                    String formattedName = directory.getName();
                    System.out.printf("%s%s %-9s %-20s%n", "d", permissions, "", formattedName);
                }
                System.out.println("------Files------");
                // Print files
                for (File file : files) {
                    String fileType = "-";
                    String permissions = utilsFunction.getPermissions(file);
                    String size = utilsFunction.formatSize(file.length());
                    String formattedName = file.getName();
                    System.out.printf("%s%s %-10s %-20s%n", fileType, permissions, size, formattedName);
                }
            } else {
                System.out.println("Folder is empty");
            }

            System.out.println("\nhelp (Show the command list)");
            System.out.println("exit|quit|stop : Terminate application\n");
        } catch (Exception ex) {
            System.out.println("Something went wrong");
        }
    }

    private static void operationCreateFolder(String op)
    {
        try{
            String extractedFolderName = op.replace("create -dir ","").strip();
            if(!utilsFunction.isValidFolderName(extractedFolderName))
            {
                return;
            }
            File file = new File(extractedFolderName);
            if (!file.isAbsolute()) {
                file = new File(currentDirPath+"\\"+file.getPath());
            }
            if(rep.isValidFolderName(file.getName()))
            {
                if (!Files.isWritable(Paths.get(file.getParent()))) {
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
            String extractedFileName = op.replace("create -file ","").strip();
            if(!utilsFunction.isValidFileName(extractedFileName))
            {
                return;
            }
            File file = new File(Objects.requireNonNull(extractedFileName));
            if (!file.isAbsolute()) {
                file = new File(currentDirPath+"\\"+extractedFileName);
            }
            if (!Files.isWritable(Paths.get(file.getParent()))) {
                System.out.println("Permission denied to create file");
                return;
            }
            FMS_CLI.createFile(file.getPath());
        }
        catch (Exception ex)
        {
            System.out.println("Invalid FileName");
        }
    }

    private static void operationOpenFolder(String op)
    {
        String extractedFolderName = op.replace("open ","").strip();

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
        if (!file.isAbsolute()) {
            file = new File(currentDirPath + "\\" + file.getPath());
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
            String extractedFolderName = op.replace("rename -dir ","").strip();
            File sourcePath = new File(extractedFolderName);
            File destinationPath;
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath+"\\"+sourcePath.getPath());
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
            destinationPath = new File(sourcePath.getParentFile()+"\\"+newFolderName);
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

    /*private static void operationRenameFolder(String op)
    {
        try{
            ArrayList<String> filePaths = utilsFunction.extractMultiplePaths(op,"rename\\s+-dir\\s+(\\S+)\\s+(\\S+)");
            File sourcePath = new File(filePaths.get(0));
            File destinationPath = new File(filePaths.get(1));
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath.get(0)+"\\"+sourcePath.getPath());
            }
            if(!destinationPath.isAbsolute())
            {
                destinationPath = new File(currentDirPath.get(0)+"\\"+destinationPath.getPath());
            }
            if(!sourcePath.exists() || sourcePath.isFile() || sourcePath.getPath().equals(currentDirPath.get(0)) || sourcePath.getName().equals("."))
            {
                System.out.println("Folder not found");
                return;
            }
            if(destinationPath.exists() && destinationPath.isDirectory())
            {
                System.out.println("Folder name already exist");
                return;
            }
            if (!Files.isWritable(Paths.get(sourcePath.getPath()))) {
                System.out.println(sourcePath.getName()+" : Permission denied");
                return;
            }
            FMS_CLI.renameFolder(sourcePath.getPath(),destinationPath.getPath());
        }
        catch (Exception ex)
        {
            operationRenameFolderBasic(op);
            //System.out.println("Due to invalid input failed to rename");
        }
    }*/

    private static void operationRenameFileBasic(String op)
    {
        try{
            String extractedFolderName = op.replace("rename -file ","").strip();
            File sourcePath = new File(extractedFolderName);
            File destinationPath;
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath+"\\"+sourcePath.getPath());
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
            destinationPath = new File(sourcePath.getParentFile()+"\\"+newFileName);
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

    /*private static void operationRenameFile(String op)
    {
        try{
            ArrayList<String> filePaths = utilsFunction.extractMultiplePaths(op,"rename\\s+(\\S+)\\s+(\\S+)");
            File sourcePath = new File(filePaths.get(0));
            File destinationPath = new File(filePaths.get(1));
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath.get(0)+"\\"+sourcePath.getPath());
            }
            if(!destinationPath.isAbsolute())
            {
                destinationPath = new File(currentDirPath.get(0)+"\\"+destinationPath.getPath());
            }
            if(destinationPath.exists() && destinationPath.isFile())
            {
                System.out.println("File name already exist");
                return;
            }
            if(!sourcePath.exists())
            {
                System.out.println("File not found to rename");
                return;
            }
            if (!Files.isWritable(Paths.get(sourcePath.getPath()))) {
                System.out.println(sourcePath.getName()+" : Permission denied");
                return;
            }
            FMS_CLI.renameFile(sourcePath.getPath(),destinationPath.getPath());

        }
        catch (Exception ex)
        {
            operationRenameFileBasic(op);
        }
    }*/

    private static void operationDeleteFolder(String op)
    {
        try{
            while (true)
            {
                System.out.print("Are you want sure delete folder (YES|NO): ");
                String deleteFolderConfirmation = userInput.nextLine().strip();
                if(deleteFolderConfirmation.equalsIgnoreCase("no"))
                {
                    break;
                }
                else if(deleteFolderConfirmation.equalsIgnoreCase("yes"))
                {
                    String extractedFolderName = op.replace("del -dir ","").strip();
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
                    FMS_CLI.deleteFolder(folderPath.getPath());
                    break;
                }
            }

        }
        catch (Exception ex)
        {
            System.out.println("\nInvalid input found");
        }
    }

    private static void operationDeleteFiles(String op)
    {
        try{
            String extractedFileName = op.replace("del -file ","");
            ArrayList<String> fileNames = utilsFunction.parseDeleteFilesCommand(extractedFileName);
            for(String fileName : fileNames){
                File fileObject = new File(currentDirPath+"//"+fileName);
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
        }
        catch (Exception ex)
        {
            System.out.println("Invalid Input");
        }
    }

    private static void operationCopyAndNavPastFolder(String op)
    {
        try{
            String extractedFolderName = op.replace("cp -dir ","").strip();
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

            int flag = 0;
            while (true){
                System.out.print("Paste (Y|N) or Navigate: ");
                String newOperation = userInput.nextLine();
                if(!newOperation.isEmpty())
                {
                    if(newOperation.equals("y") || newOperation.equals("Y"))
                    {
                        if(currentDirPath.equals(mainDirPath))
                        {
                            System.out.println("Not possible to past");
                            continue;
                        }
                        destinationPath = new File(currentDirPath+"//"+sourcePath.getName());
                        if(destinationPath.exists() && destinationPath.isDirectory())
                        {
                            System.out.println("Folder already exist on destination");
                            break;
                        }
                        if (!Files.isReadable(Paths.get(sourcePath.getPath()))) {
                            System.out.println(sourcePath.getName()+" : Permission denied");
                            break;
                        }
                        FMS_CLI.copyFolder(sourcePath.getPath(),destinationPath.getPath());
                        break;
                    }
                    else if(newOperation.equals("n") || newOperation.equals("N"))
                    {
                        break;
                    } else if (rep.matchPattern(rep.openFolderPattern,newOperation)) {
                        flag++;
                        operationOpenFolder(newOperation);
                    }
                    else if(rep.matchPattern(rep.backToFolderPattern,newOperation))
                    {
                        backToPath();
                        flag--;
                    }
                }
            }
            //listFilesAndFolders();
        }
        catch (Exception ex)
        {
            System.out.println("Invalid Input");
        }
    }

    /*private static void operationCopyFolder(String op)
    {
        try{
            ArrayList<String> extractedPaths = utilsFunction.extractMultiplePaths(op,"cp\\s+-dir\\s+(\\S+)\\s+(\\S+)");
            File sourcePath = new File(extractedPaths.get(0));
            File destinationPath = new File(extractedPaths.get(1));
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath+"//"+sourcePath.getPath());
            }

            if(!sourcePath.exists() || !rep.isValidPath(sourcePath.getPath()) || sourcePath.isFile() || sourcePath.getPath().equals(currentDirPath) || sourcePath.getName().equals("."))
            {
                System.out.println("Folder not found");
                return;
            }

            if(!destinationPath.isAbsolute())
            {
                destinationPath = new File(currentDirPath+"//"+destinationPath.getPath());
            }

            if(destinationPath.exists() && destinationPath.isDirectory())
            {
                System.out.println("Folder already exist on destination");
                return;
            }
            if (!Files.isReadable(Paths.get(sourcePath.getPath()))) {
                System.out.println(sourcePath.getName()+" : Permission denied");
                return;
            }
            FMS_CLI.copyFolder(sourcePath.getPath(),destinationPath.getPath());
        }
        catch (Exception ex)
        {
            operationCopyAndNavPastFolder(op);
            //System.out.println("Invalid Input");
        }
    }*/

    private static void operationCopyAndNavPastFile(String op)
    {
        try{
            String extractedFileName = op.replace("cp -file ","").strip();
            File sourcePath = new File(extractedFileName);
            File destinationPath;

            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath+"//"+sourcePath.getPath());
            }
            if(sourcePath.exists() && sourcePath.isFile())
            {
                int flag = 0;
                while (true){
                    System.out.print("Paste (Y|N) or Navigate: ");
                    String newOperation = userInput.nextLine();
                    if(!newOperation.isEmpty())
                    {
                        if(newOperation.equals("y") || newOperation.equals("Y"))
                        {
                            if(currentDirPath.equals(mainDirPath))
                            {
                                System.out.println("Not possible to past");
                                continue;
                            }
                            destinationPath = new File(currentDirPath+"//"+sourcePath.getName());
                            if(destinationPath.exists() && destinationPath.isFile())
                            {
                                System.out.println("File already exist on destination");
                                break;
                            }
                            if (!Files.isReadable(Paths.get(sourcePath.getPath()))) {
                                System.out.println(sourcePath.getName()+" : Permission denied");
                                break;
                            }
                            FMS_CLI.copyFile(sourcePath.getPath(),destinationPath.getPath());
                            break;
                        }
                        else if(newOperation.equals("n") || newOperation.equals("N"))
                        {
                            break;
                        } else if (rep.matchPattern(rep.openFolderPattern,newOperation)) {
                            flag++;
                            operationOpenFolder(newOperation);
                        }
                        else if(rep.matchPattern(rep.backToFolderPattern,newOperation))
                        {
                            backToPath();
                            flag--;
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

    /*private static void operationCopyFile(String op)
    {
        try{
            ArrayList<String> extractedPaths = utilsFunction.extractMultiplePaths(op,"cp\\s+(\\S+)\\s+(\\S+)");
            File sourcePath = new File(extractedPaths.get(0));
            File destinationPath = new File(extractedPaths.get(1));
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath+"//"+sourcePath.getPath());
            }
            if(!destinationPath.isAbsolute())
            {
                destinationPath = new File(currentDirPath+"//"+destinationPath.getPath());
            }
            if(!sourcePath.exists() || sourcePath.isDirectory())
            {
                System.out.println("File not found");
                return;
            }
            if(destinationPath.exists() && destinationPath.isFile())
            {
                System.out.println("File already exist on destination");
                return;
            }
            if (!Files.isReadable(Paths.get(sourcePath.getPath()))) {
                System.out.println(sourcePath.getName()+" : Permission denied");
                return;
            }
            FMS_CLI.copyFile(sourcePath.getPath(),destinationPath.getPath());
        }
        catch (Exception ex)
        {
            operationCopyAndNavPastFile(op);
        }

    }*/

    private static void operationMoveAndNavPastFolder(String op)
    {
        try{
            String extractedFileName = op.replace("mv -dir ","").strip();
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
            int flag = 0;
            while (true){
                System.out.print("Paste (Y|N) or Navigate: ");
                String newOperation = userInput.nextLine();
                if(!newOperation.isEmpty())
                {
                    if(newOperation.equals("y") || newOperation.equals("Y"))
                    {
                        if(currentDirPath.equals(mainDirPath))
                        {
                            System.out.println("Not possible to past");
                            continue;
                        }
                        destinationPath = new File(currentDirPath+"//"+sourcePath.getName());
                        if(destinationPath.exists() && destinationPath.isDirectory())
                        {
                            System.out.println("Folder already exist on destination");
                            break;
                        }
                        if (!Files.isWritable(Paths.get(sourcePath.getPath()))) {
                            System.out.println(sourcePath.getName()+" : Permission denied");
                            break;
                        }
                        FMS_CLI.moveFolder(sourcePath.getPath(),destinationPath.getPath());
                        break;
                    }
                    else if(newOperation.equals("n") || newOperation.equals("N"))
                    {
                        break;
                    } else if (rep.matchPattern(rep.openFolderPattern,newOperation)) {
                        flag++;
                        operationOpenFolder(newOperation);
                    }
                    else if(rep.matchPattern(rep.backToFolderPattern,newOperation))
                    {
                        backToPath();
                        flag--;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            System.out.println("Invalid Input");
        }

    }

    private static void operationMoveAndNavPastFile(String op)
    {
        try{
            String extractedFileName = op.replace("mv -file ","").strip();
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
            int flag = 0;
            while (true){
                System.out.print("Paste (Y|N) or Navigate: ");
                String newOperation = userInput.nextLine();
                if(!newOperation.isEmpty())
                {
                    if(newOperation.equals("y") || newOperation.equals("Y"))
                    {
                        if(currentDirPath.equals(mainDirPath))
                        {
                            System.out.println("Not possible to past");
                            continue;
                        }
                        destinationPath = new File(currentDirPath+"//"+sourcePath.getName());
                        if(destinationPath.exists() && destinationPath.isFile())
                        {
                            System.out.println("File already exist on destination");
                            break;
                        }
                        if (!Files.isWritable(Paths.get(sourcePath.getPath()))) {
                            System.out.println(sourcePath.getName()+" : Permission denied");
                            break;
                        }
                        FMS_CLI.moveFile(sourcePath.getPath(),destinationPath.getPath());
                        break;
                    }
                    else if(newOperation.equals("n") || newOperation.equals("N"))
                    {
                        break;
                    } else if (rep.matchPattern(rep.openFolderPattern,newOperation)) {
                        flag++;
                        operationOpenFolder(newOperation);
                    }
                    else if(rep.matchPattern(rep.backToFolderPattern,newOperation))
                    {
                        backToPath();
                        flag--;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            System.out.println("Invalid Input");
        }

    }

    /*private static void operationMoveFolder(String op)
    {
        try{
            ArrayList<String> extractedPaths = utilsFunction.extractMultiplePaths(op,"mv\\s+-dir\\s+(\\S+)\\s+(\\S+)");
            File sourcePath = new File(extractedPaths.get(0));
            File destinationPath = new File(extractedPaths.get(1));
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath+"//"+sourcePath.getPath());
            }
            if(!destinationPath.isAbsolute())
            {
                destinationPath = new File(currentDirPath+"//"+destinationPath.getPath());
            }
            if(!sourcePath.exists() || !rep.isValidPath(sourcePath.getPath()) || sourcePath.isFile() || sourcePath.getPath().equals(currentDirPath) || sourcePath.getName().equals("."))
            {
                System.out.println("Folder not found");
                return;
            }
            if (destinationPath.exists() && destinationPath.isDirectory())
            {
                System.out.println("Can't move folder already exist");
                return;
            }
            if (!Files.isWritable(Paths.get(sourcePath.getPath()))) {
                System.out.println(sourcePath.getName()+" : Permission denied");
                return;
            }
            FMS_CLI.moveFolder(sourcePath.getPath(),destinationPath.getPath());
        }
        catch (Exception ex)
        {
            System.out.println("Invalid Input");
        }

    }

    private static void operationMoveFile(String op)
    {
        try{
            ArrayList<String> extractedPaths = utilsFunction.extractMultiplePaths(op,"mv\\s+(\\S+)\\s+(\\S+)");
            File sourcePath = new File(extractedPaths.get(0));
            File destinationPath = new File(extractedPaths.get(1));
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath+"//"+sourcePath.getPath());
            }
            if(!destinationPath.isAbsolute())
            {
                destinationPath = new File(currentDirPath+"//"+destinationPath.getPath());
            }
            if(!sourcePath.exists() || sourcePath.isDirectory())
            {
                System.out.println("File not found");
                return;
            }
            if(destinationPath.exists() && destinationPath.isFile())
            {
                System.out.println("File already exist");
                return;
            }
            if (!Files.isWritable(Paths.get(sourcePath.getPath()))) {
                System.out.println(sourcePath.getName()+" : Permission denied");
                return;
            }
            FMS_CLI.moveFile(sourcePath.getPath(),destinationPath.getPath());
        }
        catch (Exception ex)
        {
            System.out.println("Invalid Input");
        }

    }*/

    private static void viewFileProperties(String op)
    {
        try{
            String extractedFileName = op.replace("prp ","");
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