import utiles.utilsFunction;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

class setup{
    public static String mainDirPath = "root";
    public static ArrayList<String> currentDirPath = new ArrayList<>();

    public static RegularExpressionPattern rep = null;

    public static Scanner userInput = null;


    public static void main(String[] args){
        currentDirPath.add(0,mainDirPath);
        userInput = new Scanner(System.in);
        rep = new RegularExpressionPattern();
        listFilesAndFolders();




        while(true)
        {
            System.out.print(currentDirPath.get(0)+">: ");
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
                    try{
                        if(currentDirPath.size()>1)
                        {
                            currentDirPath.remove(0);
                        }
                        listFilesAndFolders();
                    }
                    catch (ArrayIndexOutOfBoundsException ex)
                    {
                        System.out.println("Array index out of bound");
                    }
                }
                else if (rep.matchPattern(rep.renameFolderPattern,op))
                {
                    operationRenameFolder(op);
                }
                else if (rep.matchPattern(rep.renameFilePattern,op))
                {
                    operationRenameFile(op);
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
                else if(rep.matchPattern(rep.findFilePattern,op))
                {
                    try {
                        String fileName = utilsFunction.extractSinglePath(op,"find\\s+(\\S+)");
                        List<Path> foundFiles = FMS_CLI.findFiles(mainDirPath, fileName, null);

                        if (!foundFiles.isEmpty()) {
                            System.out.println("Found files:");
                            for (Path file : foundFiles) {
                                System.out.println(file);
                            }
                        } else {
                            System.out.println("No matching files found.");
                        }
                    }
                    catch (Exception ex)
                    {
                        System.out.println("Invalid Input");
                    }

                }
                else{
                    HelpText.matchCommand(op);
                }

            }
        }
    }

    public static void listFilesAndFolders()
    {
        if (currentDirPath.get(0).equals("root"))
        {
            File[] rootDrive = File.listRoots();

            for(File sysDrive : rootDrive){
                System.out.println("Drive : " + sysDrive);
            }
            return;
        }
        try{
            File mainDir = new File(currentDirPath.get(0));

            if (!(mainDir.exists() && mainDir.isDirectory())) {
                System.out.println("Folder not found");
                return;
            }
            System.out.println("**********************************************");
            System.out.println("Files from directory : " + mainDir);
            System.out.println("**********************************************\n");

            File[] filesAndFolders = mainDir.listFiles();

            if (filesAndFolders != null) {
                for (File file : filesAndFolders) {
                    String fileType = file.isDirectory() ? "d" : "-";
                    String permissions = utilsFunction.getPermissions(file);
                    String size = file.isFile() ? utilsFunction.formatSize(file.length()) : "";
                    String formattedName = file.getName();

                    System.out.printf("%s%s %-9s %-20s%n", fileType, permissions, size, formattedName);
                }
            }
            else{
                System.out.println("Folder was empty");
            }
            System.out.println("\nhelp (Show the command list)");
            System.out.println("exit|quit|stop : Terminate application\n");
        }
        catch (Exception ex)
        {
            System.out.println("Something wrong");
        }
    }

    private static void operationCreateFolder(String op)
    {
        try{
            File file = new File(Objects.requireNonNull(utilsFunction.extractSinglePath(op, "create\s+-dir\s+(\\S+)")));
            if (!file.isAbsolute()) {
                file = new File(currentDirPath.get(0)+"\\"+file.getPath());
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
            String filePath = utilsFunction.extractSinglePath(op,"create\\s+(\\S+)");
            File file = new File(Objects.requireNonNull(filePath));
            if (!file.isAbsolute()) {
                file = new File(currentDirPath.get(0)+"\\"+filePath);
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
        File file = new File(Objects.requireNonNull(utilsFunction.extractSinglePath(op, "open\\s+(\\S+)")));
        if (!file.isAbsolute()) {
            file = new File(currentDirPath.get(0) + "\\" + file.getPath());
        }
        if(file.exists() && file.isDirectory())
        {
            currentDirPath.add(0,file.getPath());
            listFilesAndFolders();
        }
        else{
            System.out.println("Folder not found");
        }
    }

    //Rename
    private static void operationRenameFolderBasic(String op){
        try{
            String userInputPath = utilsFunction.extractSinglePath(op,"rename\\s+-dir\\s+(\\S+)");
            if(userInputPath==null)
            {
                System.out.println("Input Path is null");
                return;
            }
            File sourcePath = new File(userInputPath);
            File destinationPath;
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath.get(0)+"\\"+sourcePath.getPath());
            }
            if(!sourcePath.exists() || !rep.isValidPath(sourcePath.getPath()) || sourcePath.isFile() || sourcePath.getPath().equals(currentDirPath.get(0)) || sourcePath.getName().equals("."))
            {
                System.out.println("Folder not found");
                return;
            }
            if (!Files.isWritable(Paths.get(sourcePath.getPath()))) {
                System.out.println(sourcePath.getName()+" : Permission denied");
                return;
            }
            System.out.print("New folder name: ");
            String newFolderName = userInput.nextLine();
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

    private static void operationRenameFolder(String op)
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
            if(!sourcePath.exists() || !rep.isValidPath(sourcePath.getPath()) || sourcePath.isFile() || sourcePath.getPath().equals(currentDirPath.get(0)) || sourcePath.getName().equals("."))
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
    }

    private static void operationRenameFileBasic(String op)
    {
        try{
            File sourcePath = new File(Objects.requireNonNull(utilsFunction.extractSinglePath(op, "rename\\s+(\\S+)")));
            File destinationPath;
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath.get(0)+"\\"+sourcePath.getPath());
            }
            if(!sourcePath.exists())
            {
                System.out.println(sourcePath.getName()+" : File not found to rename");
                return;
            }
            if (!Files.isWritable(Paths.get(sourcePath.getPath()))) {
                System.out.println(sourcePath.getName()+" : Permission denied");
                return;
            }
            System.out.print("New file name: ");
            String newFileName = userInput.nextLine();
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

    private static void operationRenameFile(String op)
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
    }

    //Delete
    private static void operationDeleteFolder(String op)
    {
        try{
            File folderPath = new File(Objects.requireNonNull(utilsFunction.extractSinglePath(op, "del\\s+-dir\\s+(\\S+)")));
            if(!folderPath.isAbsolute())
            {
                folderPath = new File(currentDirPath.get(0)+"//"+folderPath.getPath());
            }
            if(!folderPath.exists() || folderPath.isFile() || !rep.isValidPath(folderPath.getPath()) || folderPath.getPath().equals(currentDirPath.get(0)) || folderPath.getName().equals("."))
            {
                System.out.println("Folder not found");
                return;
            }
            FMS_CLI.deleteFolder(folderPath.getPath());
        }
        catch (Exception ex)
        {
            System.out.println("Invalid input found");
        }
    }

    private static void operationDeleteFiles(String op)
    {
        try{
            String[] fileNames = utilsFunction.extractDeleteFileNames(op);
            if(fileNames!=null)
            {
                for(String fileName : fileNames){
                    File fileObject = new File(currentDirPath.get(0)+"//"+fileName);
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
        }
        catch (Exception ex)
        {
            System.out.println("Invalid Input");
        }
    }


    //Copy
    private static void operationCopyAndNavPastFolder(String op)
    {
        try{
            File sourcePath = new File(Objects.requireNonNull(utilsFunction.extractSinglePath(op, "cp\\s+-dir\\s+(\\S+)")));
            File destinationPath;
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath.get(0)+"//"+sourcePath.getPath());
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
                        destinationPath = new File(currentDirPath.get(0)+"//"+sourcePath.getName());
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
                        if(currentDirPath.size()>1)
                        {
                            currentDirPath.remove(0);
                        }
                        listFilesAndFolders();
                        flag--;
                    }
                }
            }
            for (int i=1;i<=flag;i++) {
                currentDirPath.remove(0);
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
                sourcePath = new File(currentDirPath.get(0)+"//"+sourcePath.getPath());
            }

            if(!sourcePath.exists() || !rep.isValidPath(sourcePath.getPath()) || sourcePath.isFile() || sourcePath.getPath().equals(currentDirPath.get(0)) || sourcePath.getName().equals("."))
            {
                System.out.println("Folder not found");
                return;
            }

            if(!destinationPath.isAbsolute())
            {
                destinationPath = new File(currentDirPath.get(0)+"//"+destinationPath.getPath());
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
            File sourcePath = new File(Objects.requireNonNull(utilsFunction.extractSinglePath(op, "cp\\s+(\\S+)")));
            File destinationPath;

            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath.get(0)+"//"+sourcePath.getPath());
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
                            destinationPath = new File(currentDirPath.get(0)+"//"+sourcePath.getName());
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
                            if(currentDirPath.size()>1)
                            {
                                currentDirPath.remove(0);
                            }
                            listFilesAndFolders();
                            flag--;
                        }
                    }
                }
                for (int i=1;i<=flag;i++) {
                    currentDirPath.remove(0);
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
                sourcePath = new File(currentDirPath.get(0)+"//"+sourcePath.getPath());
            }
            if(!destinationPath.isAbsolute())
            {
                destinationPath = new File(currentDirPath.get(0)+"//"+destinationPath.getPath());
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
            File sourcePath = new File(Objects.requireNonNull(utilsFunction.extractSinglePath(op, "mv\\s+-dir\\s+(\\S+)")));
            File destinationPath;
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath.get(0)+"//"+sourcePath.getPath());
            }

            if(!sourcePath.exists() || !rep.isValidPath(sourcePath.getPath()) || sourcePath.isFile() || sourcePath.getPath().equals(currentDirPath.get(0)) || sourcePath.getName().equals("."))
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
                        destinationPath = new File(currentDirPath.get(0)+"//"+sourcePath.getName());
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
                        if(currentDirPath.size()>1)
                        {
                            currentDirPath.remove(0);
                        }
                        listFilesAndFolders();
                        flag--;
                    }
                }
            }
            for (int i=1;i<=flag;i++) {
                currentDirPath.remove(0);
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
            File sourcePath = new File(Objects.requireNonNull(utilsFunction.extractSinglePath(op, "mv\\s+(\\S+)")));
            File destinationPath;
            if(!sourcePath.isAbsolute())
            {
                sourcePath = new File(currentDirPath.get(0)+"//"+sourcePath.getPath());
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
                        destinationPath = new File(currentDirPath.get(0)+"//"+sourcePath.getName());
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
                        if(currentDirPath.size()>1)
                        {
                            currentDirPath.remove(0);
                        }
                        listFilesAndFolders();
                        flag--;
                    }
                }
            }
            for (int i=1;i<=flag;i++) {
                currentDirPath.remove(0);
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
                sourcePath = new File(currentDirPath.get(0)+"//"+sourcePath.getPath());
            }
            if(!destinationPath.isAbsolute())
            {
                destinationPath = new File(currentDirPath.get(0)+"//"+destinationPath.getPath());
            }
            if(!sourcePath.exists() || !rep.isValidPath(sourcePath.getPath()) || sourcePath.isFile() || sourcePath.getPath().equals(currentDirPath.get(0)) || sourcePath.getName().equals("."))
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
                sourcePath = new File(currentDirPath.get(0)+"//"+sourcePath.getPath());
            }
            if(!destinationPath.isAbsolute())
            {
                destinationPath = new File(currentDirPath.get(0)+"//"+destinationPath.getPath());
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
            File filePath = new File(Objects.requireNonNull(utilsFunction.extractSinglePath(op, "prp\\s+(\\S+)")));
            if(!filePath.isAbsolute())
            {
                filePath = new File(currentDirPath.get(0)+"//"+filePath.getPath());
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