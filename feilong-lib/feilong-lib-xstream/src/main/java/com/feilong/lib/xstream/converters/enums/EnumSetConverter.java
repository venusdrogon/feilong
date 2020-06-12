/*
 * Copyright (C) 2005 Joe Walnes.
 * Copyright (C) 2006, 2007, 2008, 2009, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 06. April 2005 by Joe Walnes
 */

// ***** READ THIS *****
// This class will only compile with JDK 1.5.0 or above as it test Java enums.
// If you are using an earlier version of Java, just don't try to build this class. XStream should work fine without it.

package com.feilong.lib.xstream.converters.enums;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.Iterator;

import com.feilong.lib.xstream.converters.ConversionException;
import com.feilong.lib.xstream.converters.Converter;
import com.feilong.lib.xstream.converters.MarshallingContext;
import com.feilong.lib.xstream.converters.UnmarshallingContext;
import com.feilong.lib.xstream.core.util.Fields;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;
import com.feilong.lib.xstream.mapper.Mapper;

/**
 * Serializes a Java 5 EnumSet. If a SecurityManager is set, the converter will only work with permissions
 * for SecurityManager.checkPackageAccess, SecurityManager.checkMemberAccess(this, EnumSet.MEMBER)
 * and ReflectPermission("suppressAccessChecks").
 *
 * @author Joe Walnes
 * @author J&ouml;rg Schaible
 */
public class EnumSetConverter implements Converter{

    private final static Field typeField = Fields.locate(EnumSet.class, Class.class, false);

    private final Mapper       mapper;

    public EnumSetConverter(Mapper mapper){
        this.mapper = mapper;
    }

    @Override
    public boolean canConvert(Class type){
        return typeField != null && type != null && EnumSet.class.isAssignableFrom(type);
    }

    @Override
    public void marshal(Object source,HierarchicalStreamWriter writer,MarshallingContext context){
        EnumSet set = (EnumSet) source;
        Class enumTypeForSet = (Class) Fields.read(typeField, set);
        String attributeName = mapper.aliasForSystemAttribute("enum-type");
        if (attributeName != null){
            writer.addAttribute(attributeName, mapper.serializedClass(enumTypeForSet));
        }
        writer.setValue(joinEnumValues(set));
    }

    private String joinEnumValues(EnumSet set){
        boolean seenFirst = false;
        StringBuffer result = new StringBuffer();
        for (Iterator iterator = set.iterator(); iterator.hasNext();){
            Enum value = (Enum) iterator.next();
            if (seenFirst){
                result.append(',');
            }else{
                seenFirst = true;
            }
            result.append(value.name());
        }
        return result.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object unmarshal(HierarchicalStreamReader reader,UnmarshallingContext context){
        String attributeName = mapper.aliasForSystemAttribute("enum-type");
        if (attributeName == null){
            throw new ConversionException("No EnumType specified for EnumSet");
        }
        Class enumTypeForSet = mapper.realClass(reader.getAttribute(attributeName));
        EnumSet set = EnumSet.noneOf(enumTypeForSet);
        String[] enumValues = reader.getValue().split(",");
        for (int i = 0; i < enumValues.length; i++){
            String enumValue = enumValues[i];
            if (enumValue.length() > 0){
                set.add(Enum.valueOf(enumTypeForSet, enumValue));
            }
        }
        return set;
    }

}