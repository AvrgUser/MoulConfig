package io.github.notenoughupdates.moulconfig.managed;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonToken;
import com.mojang.blaze3d.platform.InputConstants;
import io.github.notenoughupdates.moulconfig.common.KeyBindHelper;

public class InputConstantsKeyTypeAdapter extends TypeAdapter<InputConstants.Key> {
    @Override
    public void write(com.google.gson.stream.JsonWriter out, InputConstants.Key value) throws java.io.IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(KeyBindHelper.getKeyIdentifier(value));
    }

    @Override
    public InputConstants.Key read(com.google.gson.stream.JsonReader in) throws java.io.IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String name = in.nextString();
        return KeyBindHelper.getKeyByIdentifier(name);
    }
}
