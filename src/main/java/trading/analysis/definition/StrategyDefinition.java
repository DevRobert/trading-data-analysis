package trading.analysis.definition;

public class StrategyDefinition {
    private ParameterDefinition[] parameterDefinitions;

    public StrategyDefinition(ParameterDefinition[] parameterDefinitions) {
        this.parameterDefinitions = parameterDefinitions;
    }

    public ParameterDefinition[] getParameterDefinitions() {
        return parameterDefinitions;
    }

    public static StrategyDefinition getLocalMaximumStrategyDefinition() {
        return new StrategyDefinition(new ParameterDefinition[] {
                new ParameterDefinition("maximum_look_behind", DataType.Integer),
                new ParameterDefinition("maximum_min_decline", DataType.Float),
                new ParameterDefinition("trailing_stop_loss_min_decline", DataType.Float),
                new ParameterDefinition("trailing_stop_loss_activation_min_raise", DataType.Float),
                new ParameterDefinition("stop_loss_min_decline", DataType.Float)
        });
    }
}
