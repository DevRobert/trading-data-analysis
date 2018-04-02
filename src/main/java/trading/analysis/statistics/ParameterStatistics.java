package trading.analysis.statistics;

import trading.analysis.definition.IndicatorDefinition;

import java.util.HashMap;
import java.util.Set;

public class ParameterStatistics {
    private IndicatorDefinition[] indicatorDefinitions;
    private HashMap<Object, IndicatorsAggregationGroup> indicatorsAggregationGroups;

    public ParameterStatistics(IndicatorDefinition[] indicatorDefinitions) {
        this.indicatorDefinitions = indicatorDefinitions;
        this.indicatorsAggregationGroups = new HashMap<>();
    }

    public void addRow(Object parameterValue, float[] indicatorValues) {
        IndicatorsAggregationGroup indicatorsAggregationGroup = this.indicatorsAggregationGroups.get(parameterValue);

        if(indicatorsAggregationGroup == null) {
            indicatorsAggregationGroup = new IndicatorsAggregationGroup(this.indicatorDefinitions);
            this.indicatorsAggregationGroups.put(parameterValue, indicatorsAggregationGroup);
        }

        indicatorsAggregationGroup.addRow(indicatorValues);
    }

    public Set<Object> getParameterValues() {
        return this.indicatorsAggregationGroups.keySet();
    }

    public IndicatorsAggregationGroup getIndicatorsAggregationGroup(Object parameterValue) {
        return this.indicatorsAggregationGroups.get(parameterValue);
    }
}
