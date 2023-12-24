public class ColorConsoleProgressBar {

    public static void simulateColorProgressBar(int totalTasks) {
        System.out.println("Colorful Progress:");

        for (int i = 0; i <= totalTasks; i++) {
            int progressPercentage = (i * 100) / totalTasks;
            updateColorProgressBar(progressPercentage);

            // Simulate some task
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\nTask completed!");
    }

    public static void updateColorProgressBar(int progressPercentage) {
        int barLength = 50;
        int numCharsToFill = (progressPercentage * barLength) / 100;

        StringBuilder progressBar = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            if (i < numCharsToFill) {
                // Green color for the filled part
                progressBar.append("\u001B[32m=");
            } else {
                // Red color for the empty part
                progressBar.append("\u001B[31m ");
            }
        }

        // Reset color and add progress percentage
        progressBar.append("\u001B[0m] " + progressPercentage + "%");
        System.out.print("\r" + progressBar.toString());
    }
}
