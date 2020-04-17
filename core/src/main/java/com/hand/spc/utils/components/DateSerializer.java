package com.hand.spc.utils.components;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateSerializer extends JsonSerializer<Date> {
	  private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	    public DateSerializer() {
	    }

	    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
	        jsonGenerator.writeString(DATE_FORMAT.format(date));
	    }
}
