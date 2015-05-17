package kr.co.koreanmagic.hibernate3.mapper.domain.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class ToEnumType extends JsonSerializer<Enum<?>> {

	@Override
	public void serialize(Enum<?> arg0, JsonGenerator generator, SerializerProvider arg2) throws IOException, JsonProcessingException {
		generator.writeStartObject();
		generator.writeNumberField("ordinal", arg0.ordinal());
		generator.writeStringField("value", arg0.toString());
		generator.writeStringField("name", arg0.name());
		generator.writeEndObject();
	}

}
