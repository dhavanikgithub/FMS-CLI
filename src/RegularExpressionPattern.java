import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionPattern {
    final Pattern helpPattern = Pattern.compile("^help$");
    final Pattern exitPattern = Pattern.compile("^(exit|quit|stop)$");
    final Pattern createHelpTextPattern = Pattern.compile("^(create\s+-h|create)$");
    final Pattern openHelpTextPattern = Pattern.compile("^(open\s+-h|open)$");
    final Pattern renameHelpTextPattern = Pattern.compile("^(rename\s+-h|rename)$");
    final Pattern deleteHelpTextPattern = Pattern.compile("^(del\s+-h|del)$");
    final Pattern copyHelpTextPattern = Pattern.compile("^(cp\s+-h|cp)$");
    final Pattern moveHelpTextPattern = Pattern.compile("^(mv\s+-h|mv)$");
    final Pattern findHelpTextPattern = Pattern.compile("^(find\s+-h|find)$");
    final Pattern propertiesHelpTextPattern = Pattern.compile("^(prp\s+-h|prp)$");
    final Pattern createFilePattern = Pattern.compile("^(create -file )");
    final Pattern createFolderPattern = Pattern.compile("^(create -dir )");
    final Pattern openFolderPattern = Pattern.compile("^(open )");
    final Pattern listFolderPattern = Pattern.compile("^(list)");
    final Pattern backToFolderPattern = Pattern.compile("^(back)");
    final Pattern renameFilePattern = Pattern.compile("rename -file ");
    final Pattern renameFolderPattern = Pattern.compile("rename -dir ");
    final Pattern deleteFilePattern = Pattern.compile("^(del -file )");
    final Pattern deleteFolderPattern = Pattern.compile("^(del -dir )");
    final Pattern copyFilePattern = Pattern.compile("^(cp -file )");
    final Pattern copyFolderPattern = Pattern.compile("^(cp -dir )");
    final Pattern moveFilePattern = Pattern.compile("^(mv -file )");
    final Pattern moveFolderPattern = Pattern.compile("^(mv -dir )");
    final Pattern filePropertyPattern = Pattern.compile("^(prp )");
    final Pattern findFilePattern = Pattern.compile("^(find )");
    final Pattern findFileGlobPattern = Pattern.compile("^(find -g )");
    final Pattern selectFilePattern = Pattern.compile("^(select -file )");
    final Pattern selectFolderPattern = Pattern.compile("^(select -dir )");
    final Pattern selectOperationHelpTextPattern = Pattern.compile("^(select|select -h)");
    final String folderNamePattern = "^[^\\\\/|?:*<>\"]*$";




    public boolean matchPattern(Pattern pattern,String text){
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public boolean isValidFolderName(String folderName) {
        return folderName.matches(this.folderNamePattern);
    }

}
