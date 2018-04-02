package trading.analysis.steps;

import trading.analysis.definition.DataType;

import java.io.*;

public class BinaryFileSink implements Step, DataFlowTarget {
    private String fileName;
    private DataType[] dataTypes;
    private int rowNumber = 0;
    private DataOutputStream dataOutputStream;

    public BinaryFileSink(String fileName, DataType[] dataTypes) {
        this.fileName = fileName;
        this.dataTypes = dataTypes;

        try {
            this.dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void processRow(Object[] data) {
        this.rowNumber++;

        for(int fieldIndex = 0; fieldIndex < this.dataTypes.length; fieldIndex++) {
            DataType dataType = this.dataTypes[fieldIndex];

            try {
                dataType.writeToStream(this.dataOutputStream, data[fieldIndex]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Reporting.reportProgress(this, this.rowNumber);
    }

    @Override
    public void end() {
        try {
            this.dataOutputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Reporting.reportCompleted(this, this.rowNumber);
    }

    @Override
    public String getName() {
        return "Binary file sink";
    }
}
