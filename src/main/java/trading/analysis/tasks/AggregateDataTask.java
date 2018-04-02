package trading.analysis.tasks;

import trading.analysis.definition.AnalysisDefinitions;
import trading.analysis.definition.DataType;
import trading.analysis.definition.IndicatorDefinition;
import trading.analysis.definition.ParameterDefinition;
import trading.analysis.steps.AggregateIndicatorsByParameters;
import trading.analysis.steps.BinaryFileSource;
import trading.analysis.steps.CreateParameterStatistics;
import trading.analysis.steps.DistributeStep;

import java.util.ArrayList;
import java.util.List;

public class AggregateDataTask {
    public void run(AnalysisDefinitions analysisDefinitions, String binaryInputFileName, String aggregatedCsvFileName, String parameterStatisticsCsvFileName) {
        List<DataType> dataTypeList = new ArrayList<>();

        for (IndicatorDefinition indicatorDefinition : analysisDefinitions.getRelevantIndicatorDefinitions()) {
            dataTypeList.add(indicatorDefinition.getDataType());
        }

        for (ParameterDefinition parameterDefinition : analysisDefinitions.getStrategyDefinition().getParameterDefinitions()) {
            dataTypeList.add(parameterDefinition.getDataType());
        }

        DataType[] dataTypes = dataTypeList.toArray(new DataType[0]);

        new BinaryFileSource(binaryInputFileName, dataTypes).run(new DistributeStep(
                new CreateParameterStatistics(analysisDefinitions.getRelevantIndicatorDefinitions(), analysisDefinitions.getStrategyDefinition().getParameterDefinitions(), parameterStatisticsCsvFileName),
                new AggregateIndicatorsByParameters(analysisDefinitions.getRelevantIndicatorDefinitions(), analysisDefinitions.getStrategyDefinition().getParameterDefinitions(), aggregatedCsvFileName)
        ));
    }
}
