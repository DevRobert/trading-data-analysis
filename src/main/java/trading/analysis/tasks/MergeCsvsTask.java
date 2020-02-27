package trading.analysis.tasks;

import trading.analysis.definition.DataType;
import trading.analysis.steps.CsvFileSink;
import trading.analysis.steps.CsvFileSource;
import trading.analysis.steps.DataFlowTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeCsvsTask {
    private File[] getSourceFiles(String sourceDirectoryName) {
        File folder = new File(sourceDirectoryName);
        return folder.listFiles();
    }

    public void run(String sourceDirectoryName, String targetFileName, String[] fieldNames, DataType[] dataTypes, String sourceSplitCharacter) {
        List<String> targetFieldNames = new ArrayList<>();
        targetFieldNames.add("source_file");
        targetFieldNames.addAll(Arrays.asList(fieldNames));

        List<DataType> targetDataTypes = new ArrayList<>();
        targetDataTypes.add(DataType.String);
        targetDataTypes.addAll(Arrays.asList(dataTypes));

        CsvFileSink csvFileSink = new CsvFileSink(
                targetFileName,
                targetFieldNames.toArray(new String[0]),
                targetDataTypes.toArray(new DataType[0])
        );

        File[] sourceFiles = this.getSourceFiles(sourceDirectoryName);

        for(int sourceFileIndex = 0; sourceFileIndex < sourceFiles.length; sourceFileIndex++) {
            File sourceFile = sourceFiles[sourceFileIndex];

            CsvFileSource csvFileSource = new CsvFileSource(sourceFile.getPath(), dataTypes, sourceSplitCharacter);
            csvFileSource.run(new DataFlowTarget() {
                @Override
                public void processRow(Object[] sourceRow) {
                    Object[] targetRow = new Object[sourceRow.length + 1];
                    targetRow[0] = sourceFile.getName();

                    for(int sourceColumnIndex = 0; sourceColumnIndex < sourceRow.length; sourceColumnIndex++) {
                        targetRow[sourceColumnIndex + 1] = sourceRow[sourceColumnIndex];
                    }

                    csvFileSink.processRow(targetRow);
                }

                @Override
                public void end() {

                }
            });
        }

        csvFileSink.end();
    }
}
