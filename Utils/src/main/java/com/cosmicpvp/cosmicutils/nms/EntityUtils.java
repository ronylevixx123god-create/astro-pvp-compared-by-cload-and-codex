package com.cosmicpvp.cosmicutils.nms;

import com.cosmicpvp.cosmicutils.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import org.bukkit.entity.Entity;

public class EntityUtils {
    public static void setGenericAttribute(Entity entity, EntityAttribute attribute, float value) throws NoSuchMethodException {
        AttributeInstance inst = EntityUtils.getGenericAttributes(entity, attribute);
        inst.setValue((double) value);
    }

    private static AttributeInstance getGenericAttributes(Entity entity, EntityAttribute attributes) throws NoSuchMethodException {
        Object handle = ReflectionUtils.getNMSHandle(entity);
        return (AttributeInstance) ReflectionUtils.invokeMethod(ReflectionUtils.getMethod(ReflectionUtils.getEntityLivingClass(), "getAttributeInstance", ReflectionUtils.getIattributeInstanceClass()), handle, attributes.getNmsEnumObject());
    }
}

