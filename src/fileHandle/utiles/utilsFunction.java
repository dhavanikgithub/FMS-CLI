package fileHandle.utiles;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class utilsFunction {

    public static String extractSinglePath(String userInput, String pattern) {
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(userInput);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public static ArrayList<String> extractMultiplePaths(String userInput, String pattern) {
        ArrayList<String> result = new ArrayList<>();
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(userInput);

        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.add(matcher.group(i));
            }
        } else {
            System.out.println("Invalid input format.");
        }
        return result;
    }


    public static String[] extractDeleteFileNames(String userInput) {
        String pattern = "del\\s+([\\S]+(\\s+[\\S]+)*)";
        String filenamesGroup = extractSinglePath(userInput, pattern);
        return (filenamesGroup != null) ? filenamesGroup.split("\\s+") : null;
    }

    public static String formatSize(long size) {
        String[] units = {"Bytes", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double sizeInUnits = size;

        while (sizeInUnits > 1024 && unitIndex < units.length - 1) {
            sizeInUnits /= 1024.0;
            unitIndex++;
        }

        return new DecimalFormat("#,##0.#").format(sizeInUnits) + units[unitIndex];
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
