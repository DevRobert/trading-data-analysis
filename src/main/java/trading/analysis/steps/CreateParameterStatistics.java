package trading.analysis.steps;

import trading.analysis.Convert;
import trading.analysis.definition.DataType;
import trading.analysis.definition.IndicatorDefinition;
import trading.analysis.definition.ParameterDefinition;
import trading.analysis.statistics.IndicatorAggregation;
import trading.analysis.statistics.IndicatorsAggregationGroup;
import trading.analysis.statistics.ParameterStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateParameterStatistics implements Step, DataFlowTarget {
    private IndicatorDefinition[] indicatorDefinitions;
    private ParameterDefinition[] parameterDefinitions;
    private String csvOutputFileName;
    private ParameterStatistics[] parameterStatistics;
    private int processedRows;

    public CreateParameterStatistics(IndicatorDefinition[] indicatorDefinitions, ParameterDefinition[] parameterDefinitions, String csvOutputFileName) {
        this.indicatorDefinitions = indicatorDefinitions;
        this.parameterDefinitions = parameterDefinitions;
        this.csvOutputFileName = csvOutputFileName;

        this.parameterStatistics = new ParameterStatistics[parameterDefinitions.length];

        for(int parameterIndex = 0; parameterIndex < parameterDefinitions.length; parameterIndex++) {
            parameterStatistics[parameterIndex] = new ParameterStatistics(indicatorDefinitions);
        }
    }

    @Override
    public void processRow(Object[] data) {
        float[] indicatorValues = new float[this.indicatorDefinitions.length];

        for(int indicatorIndex = 0; indicatorIndex < this.indicatorDefinitions.length; indicatorIndex++) {
            indicatorValues[indicatorIndex] = Convert.toFloat(data[indicatorIndex]);
        }

        for(int parameterIndex = 0; parameterIndex < this.parameterDefinitions.length; parameterIndex++) {
            Object parameterValue = data[this.indicatorDefinitions.length + parameterIndex];
            this.parameterStatistics[parameterIndex].addRow(parameterValue, indicatorValues);
        }

        this.processedRows++;
        Reporting.reportProgress(this, this.processedRows);
    }

    @Override
    public void end() {
        List<String> fieldNameList = new ArrayList<>();
        List<DataType> dataTypeList = new ArrayList<>();

        fieldNameList.add("parameter_name");
        dataTypeList.add(DataType.String);

        fieldNameList.add("parameter_value");
        dataTypeList.add(DataType.String);

        for (IndicatorDefinition indicatorDefinition : this.indicatorDefinitions) {
            fieldNameList.add("min_" + indicatorDefinition.getName());
            dataTypeList.add(DataType.Float);

            fieldNameList.add("avg_" + indicatorDefinition.getName());
            dataTypeList.add(DataType.Float);

            fieldNameList.add("max_" + indicatorDefinition.getName());
            dataTypeList.add(DataType.Float);
        }

        String[] fieldNames = fieldNameList.toArray(new String[0]);
        DataType[] dataTypes = dataTypeList.toArray(new DataType[0]);

        CsvFileSink csvFileSink = new CsvFileSink(this.csvOutputFileName, fieldNames, dataTypes);

        for (int parameterIndex = 0; parameterIndex < this.parameterDefinitions.length; parameterIndex++) {
            ParameterDefinition parameterDefinition = this.parameterDefinitions[parameterIndex];
            ParameterStatistics singleParameterStatistics = this.parameterStatistics[parameterIndex];

            Object[] parameterValues = singleParameterStatistics.getParameterValues().toArray();
            Arrays.sort(parameterValues);

            for (Object parameterValue : parameterValues) {
                Object[] data = new Object[fieldNameList.size()];

                data[0] = parameterDefinition.getName();
                data[1] = parameterDefinition.getDataType().convertToString(parameterValue);

                IndicatorsAggregationGroup indicatorsAggregationGroup = singleParameterStatistics.getIndicatorsAggregationGroup(parameterValue);
                IndicatorAggregation[] indicatorAggregations = indicatorsAggregationGroup.getIndicatorAggregations();

                for(int indicatorIndex = 0; indicatorIndex < this.indicatorDefinitions.length; indicatorIndex++) {
                    IndicatorAggregation indicatorAggregation = indicatorAggregations[indicatorIndex];

                    final int offset = 2; // name/ value
                    final int aggregations = 3;

                    data[offset + aggregations * indicatorIndex] = indicatorAggregation.getMin();
                    data[offset + aggregations * indicatorIndex + 1] = indicatorAggregation.getAvg();
                    data[offset + aggregations * indicatorIndex + 2] = indicatorAggregation.getMax();
                }

                csvFileSink.processRow(data);
            }
        }

        csvFileSink.end();

        Reporting.reportCompleted(this, this.processedRows);
    }

    @Override
    public String getName() {
        return "Create parameter statistics";
    }
}
