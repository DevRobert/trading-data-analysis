package trading.analysis.tasks;

import trading.analysis.definition.AnalysisDefinitions;
import trading.analysis.definition.DataType;
import trading.analysis.definition.IndicatorDefinition;
import trading.analysis.definition.ParameterDefinition;
import trading.analysis.steps.BinaryFileSink;
import trading.analysis.steps.CsvFileSource;
import trading.analysis.steps.RemoveFields;

import java.util.ArrayList;
import java.util.List;

public class ConvertCsvToBinaryTask {
    public void run(AnalysisDefinitions analysisDefinitions, String csvInputFileName, String binaryOutputFileName) {
        System.out.println("Convert CSV to binary task started...");

        List<DataType> sourceDataTypeList = new ArrayList<>();
        List<Boolean> removeFieldList = new ArrayList<>();
        List<DataType> targetDataTypeList = new ArrayList<>();

        for(IndicatorDefinition indicatorDefinition: analysisDefinitions.getAllIndicatorDefinitions()) {
            sourceDataTypeList.add(indicatorDefinition.getDataType());
            removeFieldList.add(indicatorDefinition.getIgnore());

            if(!indicatorDefinition.getIgnore()) {
                targetDataTypeList.add(indicatorDefinition.getDataType());
            }
        }

        for(ParameterDefinition parameterDefinition: analysisDefinitions.getStrategyDefinition().getParameterDefinitions()) {
            sourceDataTypeList.add(parameterDefinition.getDataType());
            removeFieldList.add(false);
            targetDataTypeList.add(parameterDefinition.getDataType());
        }

        DataType[] sourceDataTypes = sourceDataTypeList.toArray(new DataType[0]);
        Boolean[] removeFields = removeFieldList.toArray(new Boolean[0]);
        DataType[] targetDataTypes = targetDataTypeList.toArray(new DataType[0]);

        new CsvFileSource(csvInputFileName, sourceDataTypes).run(
                new RemoveFields(removeFields,
                        new BinaryFileSink(binaryOutputFileName, targetDataTypes)));

        System.out.println("Convert CSV to binary task completed.");
    }
}
