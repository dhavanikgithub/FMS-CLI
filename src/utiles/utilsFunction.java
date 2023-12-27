package utiles;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class utilsFunction {


    /*public static ArrayList<String> extractMultiplePaths(String userInput, String pattern) {
        ArrayList<String> result = new ArrayList<>();
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(userInput);

        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.add(matcher.group(i));
            }
        }
        return result;
    }*/

    public static ArrayList<String> parseDeleteFilesCommand(String input){
        String regex = "\"([^\"]+)\"|\\S+";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        ArrayList<String> filesName = new ArrayList<>();

        while (matcher.find()) {
            // Group 1 captures the file name if it's in double quotes
            // Group 0 captures the file name if it's not in double quotes
            String fileName = matcher.group(1) != null ? matcher.group(1) : matcher.group();
            filesName.add(fileName);
        }
        return filesName;
    }

    public static boolean isValidFileName(String fileName) {
        // Regular expression for a valid file name
        String regex = "^[^\\\\/:*?\"<>|]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.matches()) {
            return true;
        } else {
            System.out.println("Characters are not valid in name: \\ / : * ? \" < > |");
            return false;
        }
    }

    public static boolean isValidFolderName(String folderName) {
        // Regular expression for a valid folder name
        String regex = "^[^\\\\/:*?\"<>|]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(folderName);

        if (matcher.matches()) {
            return true;
        } else {
            System.out.println("Characters are not valid in name: \\ / : * ? \" < > |");
            return false;
        }
    }

    public static String formatSize(long size) {
        String[] units = {"Bytes", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double sizeInUnits = size;

        while (sizeInUnits > 1024 && unitIndex < units.length - 1) {
            sizeInUnits /= 1024.0;
            unitIndex++;
        }

        return new DecimalFormat("#,##0.#").format(sizeInUnits) +" "+ units[unitIndex];
    }

    public static String getPermissions(File file) {
        StringBuilder sb = new StringBuilder("---------");

        if (file.canRead()) {
            sb.setCharAt(0, 'r');
        }
        if (file.canWrite()) {
            sb.setCharAt(1, 'w');
        }
        if (file.canExecute()) {
            sb.setCharAt(2, 'x');
        }

        return sb.toString();
    }

}
