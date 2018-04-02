package trading.analysis.statistics;

public class IndicatorAggregation {
    private float min;
    private float max;
    private double sum;
    private int num;

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }

    public float getAvg() {
        return (float) (this.sum / (double) this.num);
    }

    public int getNum() {
        return this.num;
    }

    public void addValue(float value) {
        if(value < this.min) {
            this.min = value;
        }

        if(value > this.max) {
            this.max = value;
        }

        this.num++;
        this.sum += value;
    }
}
