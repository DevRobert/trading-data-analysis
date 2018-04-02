package trading.analysis.statistics;

import trading.analysis.definition.IndicatorDefinition;

public class IndicatorsAggregationGroup {
    private IndicatorAggregation[] indicatorAggregations;

    public IndicatorsAggregationGroup(IndicatorDefinition[] indicatorDefinitions) {
        this.indicatorAggregations = new IndicatorAggregation[indicatorDefinitions.length];

        for(int indicatorIndex = 0; indicatorIndex < indicatorDefinitions.length; indicatorIndex++) {
            this.indicatorAggregations[indicatorIndex] = new IndicatorAggregation();
        }
    }

    public IndicatorAggregation[] getIndicatorAggregations() {
        return this.indicatorAggregations;
    }

    public void addRow(float[] indicatorValues) {
        for(int indicatorIndex = 0; indicatorIndex < this.indicatorAggregations.length; indicatorIndex++) {
            this.indicatorAggregations[indicatorIndex].addValue(indicatorValues[indicatorIndex]);
        }
    }
}
