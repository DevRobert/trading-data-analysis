package trading.analysis.tasks;

import trading.analysis.definition.AnalysisDefinitions;
import trading.analysis.definition.DataType;
import trading.analysis.definition.IndicatorDefinition;
import trading.analysis.definition.ParameterDefinition;
import trading.analysis.steps.CreateParameterStatistics;
import trading.analysis.steps.CsvFileSource;
import trading.analysis.steps.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AnalyzeAggregatedDataTask {
    public void run(AnalysisDefinitions analysisDefinitions, String aggregatedDataCsvInputFileName, String aggregatedParameterStatisticsOutputFileName) {
        ParameterDefinition[] parameterDefinitions = analysisDefinitions.getStrategyDefinition().getParameterDefinitions();
        IndicatorDefinition[] indicatorDefinitions = analysisDefinitions.getRelevantIndicatorDefinitions();

        // Source data types

        List<DataType> sourceDataTypeList = new ArrayList<>();

        for (ParameterDefinition parameterDefinition : parameterDefinitions) {
            sourceDataTypeList.add(parameterDefinition.getDataType());
        }

        sourceDataTypeList.add(DataType.Integer); // Num

        for (IndicatorDefinition indicatorDefinition : indicatorDefinitions) {
            sourceDataTypeList.add(DataType.Float); // Min
            sourceDataTypeList.add(DataType.Float); // Avg
            sourceDataTypeList.add(DataType.Float); // Max
        }

        // ;min_realized_rate_of_return;avg_realized_rate_of_return;max_realized_rate_of_return;min_added_rate_of_return;avg_added_rate_of_return;max_added_rate_of_return;min_transactions;avg_transactions;max_transactions

        Function<Object[], Object[]> transformInput = data -> {
            // Input: parameters, num, indicator aggregation value
            // Output: first indicators, then parameters

            Object[] parameterValues = new Object[parameterDefinitions.length];

            for(int parameterIndex = 0; parameterIndex < parameterDefinitions.length; parameterIndex++) {
                parameterValues[parameterIndex] = data[parameterIndex];
            }

            Object[] indicatorValues = new Object[indicatorDefinitions.length];

            for(int indicatorIndex = 0; indicatorIndex < indicatorDefinitions.length; indicatorIndex++) {
                int offset = parameterDefinitions.length + 1 + 3 * indicatorIndex;
                Object minValue = data[offset];
                Object avgValue = data[offset + 1];
                Object maxValue = data[offset + 2];
                indicatorValues[indicatorIndex] = avgValue;
            }

            Object[] result = new Object[parameterValues.length + indicatorValues.length];

            System.arraycopy(indicatorValues, 0, result, 0, indicatorValues.length);
            System.arraycopy(parameterValues, 0, result, indicatorValues.length, parameterValues.length);

            return result;
        };

        DataType[] sourceDataTypes = sourceDataTypeList.toArray(new DataType[0]);

        new CsvFileSource(aggregatedDataCsvInputFileName, sourceDataTypes, ";").run(
                new Transform(transformInput,
                        new CreateParameterStatistics(analysisDefinitions.getRelevantIndicatorDefinitions(), analysisDefinitions.getStrategyDefinition().getParameterDefinitions(), aggregatedParameterStatisticsOutputFileName)));
    }
}
