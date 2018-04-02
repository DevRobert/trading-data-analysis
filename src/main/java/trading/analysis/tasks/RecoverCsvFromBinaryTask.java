package trading.analysis.tasks;

import trading.analysis.definition.AnalysisDefinitions;
import trading.analysis.definition.DataType;
import trading.analysis.definition.IndicatorDefinition;
import trading.analysis.definition.ParameterDefinition;
import trading.analysis.steps.BinaryFileSource;
import trading.analysis.steps.CsvFileSink;

import java.util.ArrayList;
import java.util.List;

public class RecoverCsvFromBinaryTask {
    public void run(AnalysisDefinitions analysisDefinitions, String binaryInputFileName, String csvOutputFileName) {
        System.out.println("Recover CSV from binary task started...");

        List<DataType> dataTypeList = new ArrayList<>();
        List<String> fieldNameList = new ArrayList();

        for(IndicatorDefinition indicatorDefinition: analysisDefinitions.getRelevantIndicatorDefinitions()) {
            dataTypeList.add(indicatorDefinition.getDataType());
            fieldNameList.add(indicatorDefinition.getName());
        }

        for(ParameterDefinition parameterDefinition: analysisDefinitions.getStrategyDefinition().getParameterDefinitions()) {
            dataTypeList.add(parameterDefinition.getDataType());
            fieldNameList.add(parameterDefinition.getName());
        }

        DataType[] dataTypes = dataTypeList.toArray(new DataType[0]);
        String[] fieldNames = fieldNameList.toArray(new String[0]);

        new BinaryFileSource(binaryInputFileName, dataTypes).run(
                new CsvFileSink(csvOutputFileName, fieldNames, dataTypes)
        );

        System.out.println("Recover CSV from binary task completed.");
    }
}
