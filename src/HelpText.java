public class HelpText {

    public static void matchCommand(String command){
        RegularExpressionPattern rep = new RegularExpressionPattern();
        if(rep.matchPattern(rep.helpPattern,command))
        {
            helpText();
        }
        else if (rep.matchPattern(rep.createHelpTextPattern,command)) {
            createHelpText();
        }
        else if (rep.matchPattern(rep.openHelpTextPattern,command)) {
            openHelpText();
        }
        else if (rep.matchPattern(rep.renameHelpTextPattern,command)) {
            renameHelpText();
        }
        else if (rep.matchPattern(rep.deleteHelpTextPattern,command)) {
            deleteHelpText();
        }
        else if (rep.matchPattern(rep.copyHelpTextPattern,command)) {
            copyHelpText();
        }
        else if (rep.matchPattern(rep.moveHelpTextPattern,command)) {
            moveHelpText();
        }
        else if (rep.matchPattern(rep.findHelpTextPattern,command)) {
            findHelpText();
        }
        else if (rep.matchPattern(rep.propertiesHelpTextPattern,command)) {
            propertiesHelpText();
        } else if (rep.matchPattern(rep.selectOperationHelpTextPattern,command)) {
            selectHelpText();
        } else{
            System.out.println("Invalid command `"+command+"`");
        }
    }
    public static void helpText(){
        StringBuilder helpText = new StringBuilder();
        helpText.append("`create`  : Create a new file or folder.\n");
        helpText.append("`open`    : Open a folder, and list its files and folders.\n");
        helpText.append("`rename`  : Rename files or folders.\n");
        helpText.append("`del`     : Delete files or folders.\n");
        helpText.append("`cp`      : Copy files or folders.\n");
        helpText.append("`mv`      : Move files or folders.\n");
        helpText.append("`find`    : Search for files by name or extension.\n");
        helpText.append("`prp`     : Show file details.\n");
        helpText.append("`select`  : Select a file or folder based on the serial number and perform the above operations on it.\n");
        helpText.append("`list`    : List out files and folders in the current path.\n");
        helpText.append("`back`    : Go back to the parent path.\n");

        System.out.println(helpText);
    }

    public static void createHelpText(){
        StringBuilder helpText = new StringBuilder();
        helpText.append("create or create -h\n-> For help\n");
        helpText.append("create -file [fileName1] [fileName2] ... \n-> To create an empty file.\n");
        helpText.append("create -dir [folderName]\n-> To create an empty folder.\n");
        System.out.println(helpText);
    }

    public static void openHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("open or open -h\n-> For help.\n");
        helpText.append("open [folderName]\n-> For navigating the folder and listing files and folders.\n");
        System.out.println(helpText);
    }
    public static void renameHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("rename or rename -h\n-> For help\n");
        helpText.append("rename -file [filename]\n-> To rename the file.\n");
        helpText.append("rename -dir [foldername]\n-> To rename the folder.\n");
        System.out.println(helpText);
    }
    public static void deleteHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("del or del -h\n-> For help\n");
        helpText.append("del -file [fileName1] [fileName2] ...\n-> To delete multiple files.\n");
        helpText.append("del -dir [folderName]\n-> To delete the folder.\n");
        System.out.println(helpText);
    }
    public static void copyHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("cp or cp -h\n-> For help\n");
        helpText.append("cp -file [filename]\n-> For copying the file.\n");
        helpText.append("cp -dir [foldername]\n-> For copying the folder.\n");
        System.out.println(helpText);
    }
    public static void moveHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("mv or mv -h\n-> For help\n");
        helpText.append("mv -file [filename]\n-> For moving the file.\n");
        helpText.append("mv -dir [foldername]\n-> For moving the folder.\n");
        System.out.println(helpText);
    }
    public static void findHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("find or find -h\n-> For help\n");
        helpText.append("find [fileName or extension]\n-> For searching the file in the current path.\n");
        helpText.append("find -g [fileName or extension]\n-> For searching the file globally.\n");
        System.out.println(helpText);
    }
    public static void propertiesHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("prp or prp -h\n-> For help\n");
        helpText.append("prp [fileName or folderName]\n-> For displaying file details such as size, creation date, etc.\n");
        System.out.println(helpText);
    }

    public static void selectHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("select or select -h\n-> For help\n");
        helpText.append("select -file fileSerialNumber\n-> For selecting the file and performing an operation, choose from the options: [cp, rename, mv, del, prp].\n");
        helpText.append("select -dir folderSerialNumber\n-> For selecting the folder and performing operations, choose from the following options: [cp, rename, mv, del, prp, open, list].\n");
        System.out.println(helpText);
    }
}
