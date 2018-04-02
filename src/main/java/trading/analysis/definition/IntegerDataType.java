package trading.analysis.definition;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IntegerDataType extends DataType {
    @Override
    public Object parseFromString(String s) {
        return java.lang.Integer.parseInt(s);
    }

    @Override
    public String convertToString(Object value) {
        return value.toString();
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream, Object value) throws IOException {
        dataOutputStream.writeInt((int) value);
    }

    @Override
    public Object readFromStream(DataInputStream dataInputStream) throws IOException {
        return dataInputStream.readInt();
    }
}
