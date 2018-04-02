package trading.analysis.steps;

import trading.analysis.definition.DataType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvFileSink implements Step, DataFlowTarget {
    private String[] fieldNames;
    private DataType[] dataTypes;
    private int processedRows = 0;
    private BufferedWriter bufferedWriter;

    public CsvFileSink(String fileName, String[] fieldNames, DataType[] dataTypes) {
        if(fieldNames.length != dataTypes.length) {
            throw new RuntimeException("The number of field names and data types must match.");
        }

        this.fieldNames = fieldNames;
        this.dataTypes = dataTypes;

        try {
            this.bufferedWriter = new BufferedWriter(new FileWriter(fileName));
            this.writeHeaderRow();
        }
        catch(IOException e) {
            if(this.bufferedWriter != null) {
                try {
                    this.bufferedWriter.close();
                }
                catch (IOException closeException) {
                    closeException.printStackTrace();
                }
            }

            throw new RuntimeException(e);
        }
    }

    private void writeHeaderRow() throws IOException {
        for (int fieldIndex = 0; fieldIndex < this.fieldNames.length; fieldIndex++) {
            if (fieldIndex > 0) {
                this.bufferedWriter.write(";");
            }

            this.bufferedWriter.write(this.fieldNames[fieldIndex]);
        }

        this.bufferedWriter.newLine();
    }

    @Override
    public void processRow(Object[] data) {
        try {
            for (int fieldIndex = 0; fieldIndex < this.dataTypes.length; fieldIndex++) {
                if(fieldIndex > 0) {
                    this.bufferedWriter.write(";");
                }

                DataType dataType = this.dataTypes[fieldIndex];
                this.bufferedWriter.write(dataType.convertToString(data[fieldIndex]));
            }

            this.bufferedWriter.newLine();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.processedRows++;
        Reporting.reportProgress(this, processedRows);
    }

    @Override
    public void end() {
        try {
            this.bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Reporting.reportCompleted(this, processedRows);
    }

    @Override
    public String getName() {
        return "CSV file sink";
    }
}
