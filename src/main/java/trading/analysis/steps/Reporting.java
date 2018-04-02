package trading.analysis.steps;

public class Reporting {
    public static void reportProgress(Step step, int rowsProcessed) {
        if (rowsProcessed % 100000 == 0) {
            System.out.println(step.getName() + ": " + String.format("%,d", rowsProcessed) + " rows processed.");
        }
    }

    public static void reportCompleted(Step step, int rowsProcessed) {
        System.out.println(step.getName() + " completed: " + String.format("%,d", rowsProcessed) + " rows processed.");
    }
}
