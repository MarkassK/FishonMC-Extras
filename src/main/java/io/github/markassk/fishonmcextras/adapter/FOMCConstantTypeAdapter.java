package io.github.markassk.fishonmcextras.adapter;

import com.google.gson.*;
import io.github.markassk.fishonmcextras.FOMC.Constant;

import java.lang.reflect.Type;

public class FOMCConstantTypeAdapter implements JsonSerializer<Constant>, JsonDeserializer<Constant> {
    @Override
    public Constant deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Constant.valueOfId(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(Constant textTag, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(textTag.ID);
    }
}
