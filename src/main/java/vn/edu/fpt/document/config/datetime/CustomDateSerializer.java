package vn.edu.fpt.document.config.datetime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;

import static vn.edu.fpt.document.utils.CustomDateTimeFormatter.DATE_FORMATTER;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 17:53
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public class CustomDateSerializer extends StdSerializer<LocalDate> {

    public CustomDateSerializer(){
        this(null);
    }

    public CustomDateSerializer(Class<LocalDate> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDate localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeString(localDateTime.format(DATE_FORMATTER));
    }
}
