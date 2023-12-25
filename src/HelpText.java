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
        }
        else{
            System.out.println("Invalid command `"+command+"`");
        }
    }
    public static void helpText(){
        StringBuilder helpText = new StringBuilder();
        helpText.append("create : `create` command for create new file or folder\n");
        helpText.append("open   : `open` command for read file or open folder and list it's files and folder\n");
        helpText.append("rename : `rename` command for rename the files or folder\n");
        helpText.append("del    : `del` command for delete the files or folder\n");
        helpText.append("cp     : `cp` command for copy the files or folder\n");
        helpText.append("mv     : `mv` command for move the files or folder\n");
        helpText.append("find   : `find` command for search files by using name or extension\n");
        helpText.append("prp    : `prp` command for show file details\n");
        System.out.println(helpText);
    }

    public static void createHelpText(){
        StringBuilder helpText = new StringBuilder();
        helpText.append("create or create -h\n-> for help\n");
        helpText.append("create [fileName] \n-> for create empty file\n");
        helpText.append("create -dir [folderName]\n-> for create empty folder\n");
        System.out.println(helpText);
    }

    public static void openHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("open or open -h\n-> for help\n");
        helpText.append("open [folderName]\n-> for navigate folder and list files and folders\n");
        System.out.println(helpText);
    }
    public static void renameHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("rename or rename -h\n-> for help\n");
        helpText.append("rename [oldFileName] [newFileName]\n-> for rename file\n");
        helpText.append("rename -dir [oldName] [newName]\n-> for rename folder\n");
        System.out.println(helpText);
    }
    public static void deleteHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("del or del -h\n-> for help\n");
        helpText.append("del [fileName1] [fileName2] ...\n-> for delete multiple files\n");
        helpText.append("del -dir [folderName]\n-> for delete folder\n");
        System.out.println(helpText);
    }
    public static void copyHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("cp or cp -h\n-> for help\n");
        helpText.append("cp [filename]\n-> for copy file\n");
        helpText.append("cp -dir [foldername]\n-> for copy folder\n");
        System.out.println(helpText);
    }
    public static void moveHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("mv or mv -h\n-> for help\n");
        helpText.append("mv [source] [destination]\n-> for move file\n");
        helpText.append("mv -dir [source] [destination]\n-> for move folder\n");
        System.out.println(helpText);
    }
    public static void findHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("find or find -h\n-> for help\n");
        helpText.append("find [fileName or extension]\n-> for search the file by name\n");
        System.out.println(helpText);
    }
    public static void propertiesHelpText()
    {
        StringBuilder helpText = new StringBuilder();
        helpText.append("prp or prp -h\n-> for help\n");
        helpText.append("prp [fileName or folderName]\n-> for show files details like size, createDate, etc\n");
        System.out.println(helpText);
    }
}
