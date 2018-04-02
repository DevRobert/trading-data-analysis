package trading.analysis;

public class Convert {
    public static float toFloat(Object value) {
        if(value instanceof Integer) {
            return (Integer) value;
        }

        if(value instanceof Float) {
            return (Float) value;
        }

        if(value instanceof Double) {
            return (float) (double) (Double) value;
        }

        throw new RuntimeException("Data type not supported: " + value.getClass().getName());
    }
}
