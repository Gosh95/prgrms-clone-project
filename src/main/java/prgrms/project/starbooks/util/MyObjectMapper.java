package prgrms.project.starbooks.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class MyObjectMapper {

    private static final ObjectMapper mapper = new ObjectMapper()
            .disable(WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule());

    public static Map<String, Object> convertToMap(Object object) {
        return mapper.convertValue(object, new TypeReference<Map<String, Object>>() {});
    }
}
