package vn.edu.fpt.document.config.datetime;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;

import static vn.edu.fpt.document.utils.CustomDateTimeFormatter.DATE_FORMATTER;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 17:54
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public class CustomDateDeserializer extends StdDeserializer<LocalDate> {

    public CustomDateDeserializer(){
        this(null);
    }

    protected CustomDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return LocalDate.parse(jsonParser.getText(), DATE_FORMATTER);
    }
}
