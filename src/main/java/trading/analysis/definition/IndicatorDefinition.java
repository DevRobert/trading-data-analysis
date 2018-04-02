package trading.analysis.definition;

public class IndicatorDefinition {
    private String name;
    private DataType dataType;
    private boolean ignore;

    private IndicatorDefinition(String name, DataType dataType, boolean ignore) {
        this.name = name;
        this.dataType = dataType;
        this.ignore = ignore;
    }

    public String getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public boolean getIgnore() {
        return ignore;
    }

    public static IndicatorDefinition relevant(String name, DataType dataType) {
        return new IndicatorDefinition(name, dataType, false);
    }

    public static IndicatorDefinition ignore(String name, DataType dataType) {
        return new IndicatorDefinition(name, dataType, true);
    }

    public static IndicatorDefinition[] getDefaultIndicatorDefinitions() {
        return new IndicatorDefinition[] {
                IndicatorDefinition.ignore("simulation", DataType.Integer),
                IndicatorDefinition.ignore("initial_balance", DataType.Float),
                IndicatorDefinition.ignore("final_balance", DataType.Float),
                IndicatorDefinition.ignore("average_market_rate_of_return", DataType.Float),
                IndicatorDefinition.relevant("realized_rate_of_return", DataType.Float),
                IndicatorDefinition.relevant("added_rate_of_return", DataType.Float),
                IndicatorDefinition.relevant("transactions", DataType.Integer)
        };
    }
}
