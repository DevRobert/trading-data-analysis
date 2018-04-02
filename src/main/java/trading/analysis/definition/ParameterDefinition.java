package trading.analysis.definition;

import trading.analysis.statistics.ParameterStatistics;

public class ParameterDefinition {
    private String name;
    private DataType dataType;

    public ParameterDefinition(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
    }
}
