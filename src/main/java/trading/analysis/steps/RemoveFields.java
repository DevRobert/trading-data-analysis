package trading.analysis.steps;

public class RemoveFields implements Step, DataFlowTarget {
    private int targetSize;
    private int[] targetIndexBySourceIndex;
    private DataFlowTarget dataFlowTarget;
    private int processedRows = 0;

    public RemoveFields(Boolean[] remove, DataFlowTarget dataFlowTarget) {
        this.dataFlowTarget = dataFlowTarget;
        this.targetSize = 0;
        this.targetIndexBySourceIndex = new int[remove.length];

        for(int fieldIndex = 0; fieldIndex < remove.length; fieldIndex++) {
            if(!remove[fieldIndex]) {
                this.targetIndexBySourceIndex[fieldIndex] = this.targetSize;
                this.targetSize++;
            }
            else {
                this.targetIndexBySourceIndex[fieldIndex] = -1;
            }
        }
    }

    @Override
    public void processRow(Object[] data) {
        Object[] result = new Object[this.targetSize];

        for(int sourceIndex = 0; sourceIndex < targetIndexBySourceIndex.length; sourceIndex++) {
            int targetIndex = targetIndexBySourceIndex[sourceIndex];

            if(targetIndex >= 0) {
                result[targetIndex] = data[sourceIndex];
            }
        }

        this.dataFlowTarget.processRow(result);

        this.processedRows++;
        Reporting.reportProgress(this, this.processedRows);
    }

    @Override
    public void end() {
        this.dataFlowTarget.end();
        Reporting.reportCompleted(this, this.processedRows);
    }

    @Override
    public String getName() {
        return "Remove fields";
    }
}
