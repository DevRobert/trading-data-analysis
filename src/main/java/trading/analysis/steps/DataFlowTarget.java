package trading.analysis.steps;

public interface DataFlowTarget {
    void processRow(Object[] data);
    void end();
}
