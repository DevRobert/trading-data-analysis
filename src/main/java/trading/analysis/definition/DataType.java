package trading.analysis.definition;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class DataType {
    public static final DataType Integer = new IntegerDataType();
    public static final DataType Float = new FloatDataType();
    public static final DataType String = new StringDataType();

    public abstract Object parseFromString(String s);
    public abstract String convertToString(Object value);

    public abstract void writeToStream(DataOutputStream dataOutputStream, Object value) throws IOException;
    public abstract Object readFromStream(DataInputStream dataInputStream) throws IOException;
}
