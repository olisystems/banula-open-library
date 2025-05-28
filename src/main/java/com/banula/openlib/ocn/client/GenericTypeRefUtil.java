package com.banula.openlib.ocn.client;

import com.banula.openlib.ocpi.model.OcpiResponse;
import org.springframework.core.ParameterizedTypeReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericTypeRefUtil {

    public static <T> ParameterizedTypeReference<OcpiResponse<T>> getWrapperTypeRef(final Class<T> innerType) {
        ParameterizedType type = new ParameterizedType() {
            public Type[] getActualTypeArguments() {
                return new Type[] { innerType };
            }

            public Type getRawType() {
                return OcpiResponse.class;
            }

            public Type getOwnerType() {
                return null;
            }
        };

        return new ParameterizedTypeReference<OcpiResponse<T>>() {
            public Type getType() {
                return type;
            }
        };
    }
}