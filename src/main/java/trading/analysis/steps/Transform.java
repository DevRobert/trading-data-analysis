package trading.analysis.steps;

import java.util.function.Function;

public class Transform implements Step, DataFlowTarget {
    private Function<Object[], Object[]>  transformHandler;
    private DataFlowTarget dataFlowTarget;
    private int processedRows = 0;

    public Transform(Function<Object[], Object[]> transformHandler, DataFlowTarget dataFlowTarget) {
        this.transformHandler = transformHandler;
        this.dataFlowTarget = dataFlowTarget;
    }

    @Override
    public void processRow(Object[] data) {
        this.dataFlowTarget.processRow(this.transformHandler.apply(data));
        this.processedRows++;
        Reporting.reportProgress(this, processedRows);
    }

    @Override
    public void end() {
        this.dataFlowTarget.end();
        Reporting.reportCompleted(this, processedRows);
    }

    @Override
    public String getName() {
        return "Transform";
    }
}
