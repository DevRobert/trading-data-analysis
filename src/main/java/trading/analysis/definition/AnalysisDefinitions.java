package trading.analysis.definition;

import java.util.ArrayList;
import java.util.List;

public class AnalysisDefinitions {
    private IndicatorDefinition[] indicatorDefinitions;
    private IndicatorDefinition[] relevantIndicatorDefinitions;
    private StrategyDefinition strategyDefinition;

    public AnalysisDefinitions(IndicatorDefinition[] indicatorDefinitions, StrategyDefinition strategyDefinition) {
        this.indicatorDefinitions = indicatorDefinitions;
        this.strategyDefinition = strategyDefinition;

        this.calculateRelevantIndicatorDefinitions();
    }

    private void calculateRelevantIndicatorDefinitions() {
        List<IndicatorDefinition> relevantIndicatorDefinitionList = new ArrayList<>();

        for(IndicatorDefinition indicatorDefinition: indicatorDefinitions) {
            if(!indicatorDefinition.getIgnore()) {
                relevantIndicatorDefinitionList.add(indicatorDefinition);
            }
        }

        this.relevantIndicatorDefinitions = relevantIndicatorDefinitionList.toArray(new IndicatorDefinition[0]);
    }

    public IndicatorDefinition[] getAllIndicatorDefinitions() {
        return this.indicatorDefinitions;
    }

    public IndicatorDefinition[] getRelevantIndicatorDefinitions() {
        return this.relevantIndicatorDefinitions;
    }

    public StrategyDefinition getStrategyDefinition() {
        return strategyDefinition;
    }
}
