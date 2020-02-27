package trading.analysis.steps;

import trading.analysis.definition.DataType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CsvFileSource implements Step {
    private String fileName;
    private DataType[] dataTypes;
    private String splitCharacter;

    public CsvFileSource(String fileName, DataType[] dataTypes, String splitCharacter) {
        this.fileName = fileName;
        this.dataTypes = dataTypes;
        this.splitCharacter = splitCharacter;
    }

    public void run(DataFlowTarget dataFlowTarget) {
        System.out.println("Reading CSV... " + this.fileName);

        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(this.fileName));
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Skip header lines

        String line;
        int rowNumber = 0;

        try {
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                rowNumber++;
                String parts[] = line.split(this.splitCharacter);

                Object[] values = new Object[this.dataTypes.length];

                for (int fieldIndex = 0; fieldIndex < this.dataTypes.length; fieldIndex++) {
                    values[fieldIndex] = this.dataTypes[fieldIndex].parseFromString(parts[fieldIndex]);
                }

                dataFlowTarget.processRow(values);

                Reporting.reportProgress(this, rowNumber);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                bufferedReader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            dataFlowTarget.end();
            Reporting.reportCompleted(this, rowNumber);
        }
    }

    @Override
    public String getName() {
        return "CSV file source";
    }
}
