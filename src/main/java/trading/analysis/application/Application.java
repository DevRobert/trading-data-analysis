package trading.analysis.application;

import trading.analysis.definition.AnalysisDefinitions;
import trading.analysis.definition.IndicatorDefinition;
import trading.analysis.definition.StrategyDefinition;
import trading.analysis.tasks.AggregateDataTask;
import trading.analysis.tasks.AnalyzeAggregatedDataTask;
import trading.analysis.tasks.ConvertCsvToBinaryTask;
import trading.analysis.tasks.RecoverCsvFromBinaryTask;

import java.io.*;

public class Application {
    public static void main(String[] args) throws IOException {
        AnalysisDefinitions analysisDefinitions = new AnalysisDefinitions(
                IndicatorDefinition.getDefaultIndicatorDefinitions(),
                StrategyDefinition.getLocalMaximumStrategyDefinition());

        // todo aggregation csv

        String fullCsvFileName = "C:\\Users\\robert\\GitHub\\data\\full_degiro.csv";
        String fullBinaryFileName = "C:\\Users\\robert\\GitHub\\data\\full_degiro.bin";
        String recoveredCsvFileName = "C:\\Users\\robert\\GitHub\\data\\full_degiro_recovered.csv";
        String aggregatedCsvFileName = "C:\\Users\\robert\\GitHub\\data\\aggregated_degiro.csv";
        String parameterStatisticsCsvFileName = "C:\\Users\\robert\\GitHub\\data\\degiro_parameter_statistics.csv";
        String aggregatedParameterStatisticsCsvFileName = "C:\\Users\\robert\\GitHub\\data\\degiro_aggregated_parameter_statistics.csv";

        // new ConvertCsvToBinaryTask().run(analysisDefinitions, fullCsvFileName, fullBinaryFileName);

        // new RecoverCsvFromBinaryTask().run(analysisDefinitions, fullBinaryFileName, recoveredCsvFileName);

        // new AggregateDataTask().run(analysisDefinitions, fullBinaryFileName, aggregatedCsvFileName, parameterStatisticsCsvFileName);

        new AnalyzeAggregatedDataTask().run(analysisDefinitions, aggregatedCsvFileName, aggregatedParameterStatisticsCsvFileName);
    }
}
