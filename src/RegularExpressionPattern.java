import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionPattern {
    final Pattern helpPattern = Pattern.compile("^help$");
    final Pattern exitPattern = Pattern.compile("^(exit)$");
    final Pattern createHelpTextPattern = Pattern.compile("^(create\s+-h|create)$");
    final Pattern openHelpTextPattern = Pattern.compile("^(open\s+-h|open)$");
    final Pattern renameHelpTextPattern = Pattern.compile("^(rename\s+-h|rename)$");
    final Pattern deleteHelpTextPattern = Pattern.compile("^(del\s+-h|del)$");
    final Pattern copyHelpTextPattern = Pattern.compile("^(cp\s+-h|cp)$");
    final Pattern moveHelpTextPattern = Pattern.compile("^(mv\s+-h|mv)$");
    final Pattern findHelpTextPattern = Pattern.compile("^(find\s+-h|find)$");
    final Pattern propertiesHelpTextPattern = Pattern.compile("^(prp\s+-h|prp)$");
    final Pattern selectOperationHelpTextPattern = Pattern.compile("^(select|select\s+-h)$");

    final Pattern createFilePattern = Pattern.compile("^create\s+-file\s+");
    final Pattern createFolderPattern = Pattern.compile("^create\s+-dir\s+");
    final Pattern openFolderPattern = Pattern.compile("^open\s+");
    final Pattern listFolderPattern = Pattern.compile("^list$");
    final Pattern backToFolderPattern = Pattern.compile("^back$");
    final Pattern renameFilePattern = Pattern.compile("^rename\s+-file\s+");
    final Pattern renameFolderPattern = Pattern.compile("^rename\s+-dir\s+");
    final Pattern deleteFilePattern = Pattern.compile("^del\s+-file\s+");
    final Pattern deleteFolderPattern = Pattern.compile("^del\s+-dir\s+");
    final Pattern copyFilePattern = Pattern.compile("^cp\s+-file\s+");
    final Pattern copyFolderPattern = Pattern.compile("^cp\s+-dir\s+");
    final Pattern moveFilePattern = Pattern.compile("^mv\s+-file\s+");
    final Pattern moveFolderPattern = Pattern.compile("^mv\s+-dir\s+");
    final Pattern filePropertyPattern = Pattern.compile("^prp\s+");
    final Pattern findFilePattern = Pattern.compile("^find\s+");
    final Pattern findFileGlobPattern = Pattern.compile("^find\s+-g\s+");
    final Pattern selectFilePattern = Pattern.compile("^select\s+-file\s+");
    final Pattern selectFolderPattern = Pattern.compile("^select\s+-dir\s+");
    final String folderNamePattern = "^[^\\\\/|?:*<>\"]*$";




    public boolean matchPattern(Pattern pattern,String text){
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public boolean isValidFolderName(String folderName) {
        return folderName.matches(this.folderNamePattern);
    }

}
