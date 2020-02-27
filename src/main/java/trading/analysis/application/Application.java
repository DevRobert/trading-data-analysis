package trading.analysis.application;

import trading.analysis.definition.AnalysisDefinitions;
import trading.analysis.definition.DataType;
import trading.analysis.definition.IndicatorDefinition;
import trading.analysis.definition.StrategyDefinition;
import trading.analysis.tasks.*;

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

        // new AnalyzeAggregatedDataTask().run(analysisDefinitions, aggregatedCsvFileName, aggregatedParameterStatisticsCsvFileName);

        new MergeCsvsTask().run(
                "C:\\Users\\robert\\GitHub\\trading-data-import\\downloads 2020-02-25",
                "C:\\Users\\robert\\GitHub\\trading-data-import\\downloads-merged.csv",
                new String[] { "timestamp", "open", "high", "low", "close", "adjusted_close", "volume", "dividend_amount", "split_coefficient" },
                new DataType[] { DataType.String, DataType.Float, DataType.Float, DataType.Float, DataType.Float, DataType.Float, DataType.Integer, DataType.Float, DataType.Float },
                ","
        );
    }
}
