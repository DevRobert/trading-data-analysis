package trading.analysis.steps;

import trading.analysis.Convert;
import trading.analysis.definition.DataType;
import trading.analysis.definition.IndicatorDefinition;
import trading.analysis.definition.ParameterDefinition;
import trading.analysis.statistics.IndicatorAggregation;
import trading.analysis.statistics.IndicatorsAggregationGroup;

import java.util.ArrayList;
import java.util.List;

public class AggregateIndicatorsByParameters implements Step, DataFlowTarget {
    private IndicatorDefinition[] indicatorDefinitions;
    private ParameterDefinition[] parameterDefinitions;
    private CsvFileSink csvFileSink;

    private IndicatorsAggregationGroup group;
    private Object[] groupParameterValues;
    private int outputFieldNumber;
    private int processedRows;

    public AggregateIndicatorsByParameters(IndicatorDefinition[] indicatorDefinitions, ParameterDefinition[] parameterDefinitions, String csvOutputFileName) {
        this.indicatorDefinitions = indicatorDefinitions;
        this.parameterDefinitions = parameterDefinitions;

        List<String> fieldNameList = new ArrayList<>();
        List<DataType> dataTypeList = new ArrayList<>();

        for(ParameterDefinition parameterDefinition: parameterDefinitions) {
            fieldNameList.add(parameterDefinition.getName());
            dataTypeList.add(parameterDefinition.getDataType());
        }

        fieldNameList.add("num");
        dataTypeList.add(DataType.Integer);

        for(IndicatorDefinition indicatorDefinition: indicatorDefinitions) {
            fieldNameList.add("min_" + indicatorDefinition.getName());
            dataTypeList.add(DataType.Float);

            fieldNameList.add("avg_" + indicatorDefinition.getName());
            dataTypeList.add(DataType.Float);

            fieldNameList.add("max_" + indicatorDefinition.getName());
            dataTypeList.add(DataType.Float);
        }

        String[] fieldNames = fieldNameList.toArray(new String[0]);
        DataType[] dataTypes = dataTypeList.toArray(new DataType[0]);

        this.csvFileSink = new CsvFileSink(csvOutputFileName, fieldNames, dataTypes);
        this.outputFieldNumber = fieldNames.length;
    }

    private static boolean areParameterValuesEqual(Object[] parameterValues, Object[] otherParameterValues) {
        // todo reverse order for performance optimization?

        for(int parameterIndex = 0; parameterIndex < parameterValues.length; parameterIndex++) {
            if(!parameterValues[parameterIndex].equals(otherParameterValues[parameterIndex])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void processRow(Object[] data) {
        float[] indicatorValues = new float[this.indicatorDefinitions.length];

        for(int indicatorIndex = 0; indicatorIndex < this.indicatorDefinitions.length; indicatorIndex++) {
            IndicatorDefinition indicatorDefinition = this.indicatorDefinitions[indicatorIndex];
            float indicatorValue = Convert.toFloat(data[indicatorIndex]);
            indicatorValues[indicatorIndex] = indicatorValue;
        }

        Object[] parameterValues = new Object[this.parameterDefinitions.length];

        for(int parameterIndex = 0; parameterIndex < this.parameterDefinitions.length; parameterIndex++) {
            ParameterDefinition parameterDefinition = this.parameterDefinitions[parameterIndex];
            Object parameterValue = data[this.indicatorDefinitions.length + parameterIndex];
            parameterValues[parameterIndex] = parameterValue;
        }

        if(this.group == null) {
            // First group
            this.group = new IndicatorsAggregationGroup(this.indicatorDefinitions);
            this.groupParameterValues = parameterValues;
        }
        else if(!areParameterValuesEqual(this.groupParameterValues, parameterValues)) {
            this.reportGroup();

            this.group = new IndicatorsAggregationGroup(indicatorDefinitions);
            this.groupParameterValues = parameterValues;
        }

        group.addRow(indicatorValues);

        this.processedRows++;
        Reporting.reportProgress(this, this.processedRows);
    }

    private void reportGroup() {
        if(this.group == null) {
            return;
        }

        Object[] data = new Object[this.outputFieldNumber];

        for(int parameterIndex = 0; parameterIndex < this.groupParameterValues.length; parameterIndex++) {
            data[parameterIndex] = this.groupParameterValues[parameterIndex];
        }

        data[this.parameterDefinitions.length] = this.group.getIndicatorAggregations()[0].getNum();

        int offset = this.parameterDefinitions.length + 1;

        for(int indicatorIndex = 0; indicatorIndex < this.indicatorDefinitions.length; indicatorIndex++) {
            IndicatorAggregation indicatorAggregation = this.group.getIndicatorAggregations()[indicatorIndex];
            data[offset + 3 * indicatorIndex] = indicatorAggregation.getMin();
            data[offset + 3 * indicatorIndex + 1] = indicatorAggregation.getAvg();
            data[offset + 3 * indicatorIndex + 2] = indicatorAggregation.getMax();
        }

        this.csvFileSink.processRow(data);
    }

    @Override
    public void end() {
        this.reportGroup();
        this.csvFileSink.end();
        Reporting.reportCompleted(this, this.processedRows);
    }

    @Override
    public String getName() {
        return "Aggregate indicators by parameters";
    }
}
