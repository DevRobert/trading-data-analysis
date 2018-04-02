package trading.analysis.definition;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StringDataType extends DataType {
    @Override
    public Object parseFromString(java.lang.String s) {
        return s;
    }

    @Override
    public java.lang.String convertToString(Object value) {
        return (String) value;
    }

    @Override
    public void writeToStream(DataOutputStream dataOutputStream, Object value) throws IOException {
        throw new RuntimeException("Not supported.");
    }

    @Override
    public Object readFromStream(DataInputStream dataInputStream) throws IOException {
        throw new RuntimeException("Not supported.");
    }

    // TODO When we like to support stream handling, we should first store the length of the string (integer) and afterwards the string itself
}
