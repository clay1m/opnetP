package com.morgan.opnet.util;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.morgan.opnet.model.Activity;

public class CustomActivitySerializer extends JsonSerializer<Activity> {
	@Override
    public void serialize(Activity activity, JsonGenerator generator, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
        generator.writeObject(activity.getActivityName());
    }
}
