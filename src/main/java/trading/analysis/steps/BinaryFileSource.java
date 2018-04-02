package trading.analysis.steps;

import trading.analysis.definition.DataType;

import java.io.*;

public class BinaryFileSource implements Step {
    private String fileName;
    private DataType[] dataTypes;

    public BinaryFileSource(String fileName, DataType[] dataTypes) {
        this.fileName = fileName;
        this.dataTypes = dataTypes;
    }

    public void run(DataFlowTarget dataFlowTarget) {
        DataInputStream dataInputStream = null;

        try {
            dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int rowNumber = 0;

        try {
            for (rowNumber = 1; ; rowNumber++) {
                Object[] values = new Object[this.dataTypes.length];

                try {
                    for (int fieldIndex = 0; fieldIndex < this.dataTypes.length; fieldIndex++) {
                        values[fieldIndex] = this.dataTypes[fieldIndex].readFromStream(dataInputStream);
                    }
                }
                catch (IOException exception) {
                    break;
                }

                dataFlowTarget.processRow(values);
                Reporting.reportProgress(this, rowNumber);
            }
        }
        finally {
            try {
                dataInputStream.close();
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
        return "Binary file source";
    }
}
