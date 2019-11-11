package com.mmnaseri.utils.spring.data.domain.impl.id;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.error.PropertyTypeMismatchException;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

/**
 * This class is for finding a field with the name {@literal "id"}.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/23/15)
 */
@SuppressWarnings("WeakerAccess")
public class NamedFieldIdPropertyResolver implements IdPropertyResolver {

    @Override
    public String resolve(Class<?> entityType, Class<?> idType) {
        final Field field = ReflectionUtils.findField(entityType, "id");
        if (field != null) {
            if (PropertyUtils.getTypeOf(idType).isAssignableFrom(PropertyUtils.getTypeOf(field.getType()))) {
                return field.getName();
            } else {
                throw new PropertyTypeMismatchException(entityType, field.getName(), idType, field.getType());
            }
        }
        return null;
    }

}
