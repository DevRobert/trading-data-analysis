package trading.analysis.steps;

public class DistributeStep implements Step, DataFlowTarget {
    private DataFlowTarget[] dataFlowTargets;

    public DistributeStep(DataFlowTarget dataFlowTarget) {
        this(new DataFlowTarget[] { dataFlowTarget });
    }

    public DistributeStep(DataFlowTarget firstDataFlowTarget, DataFlowTarget secondDataFlowTarget) {
        this(new DataFlowTarget[] { firstDataFlowTarget, secondDataFlowTarget });
    }

    public DistributeStep(DataFlowTarget[] dataFlowTargets) {
        this.dataFlowTargets = dataFlowTargets;
    }

    @Override
    public void processRow(Object[] data) {
        for(DataFlowTarget dataFlowTarget: this.dataFlowTargets) {
            dataFlowTarget.processRow(data);
        }
    }

    @Override
    public void end() {
        for(DataFlowTarget dataFlowTarget: this.dataFlowTargets) {
            dataFlowTarget.end();
        }
    }

    @Override
    public String getName() {
        return "Distribute";
    }
}
