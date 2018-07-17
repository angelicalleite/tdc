package br.gov.sibbr.api.core.helper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * Provider mapper json for entity informade or list of entity
 */
public class JsonMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Getter objectMapper and configurate properties for mapper only attributes existings
     */
    private static ObjectMapper getMapper() {
        // mapper only attributes existing or annotations class with @JsonIgnoreProperties(ignoreUnknown = true)
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        return objectMapper;
    }

    public static <T> List<T> toList(String json, Class<T> type) {
        try {
            return getMapper().readValue(json, getMapper().getTypeFactory().constructCollectionType(List.class, type));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> toList(String json, String point, Class<T> type) {
        try {
            String node = getMapper().readTree(json).at(point).toString();
            return getMapper().readValue(node, getMapper().getTypeFactory().constructCollectionType(List.class, type));
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T toObject(String json, String point, Class<T> classe) {
        try {
            return getMapper().treeToValue(getMapper().readTree(json).at(point), classe);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T toObject(String json, Class<T> classe) {
        try {
            return getMapper().treeToValue(getMapper().readTree(json), classe);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
