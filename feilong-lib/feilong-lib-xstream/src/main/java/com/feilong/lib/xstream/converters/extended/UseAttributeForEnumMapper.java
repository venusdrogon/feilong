/*
 * Copyright (C) 2013 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 25. September 2013 by Joerg Schaible
 */
package com.feilong.lib.xstream.converters.extended;

import com.feilong.lib.xstream.converters.SingleValueConverter;
import com.feilong.lib.xstream.core.util.DependencyInjectionFactory;
import com.feilong.lib.xstream.mapper.AttributeMapper;
import com.feilong.lib.xstream.mapper.DefaultMapper;
import com.feilong.lib.xstream.mapper.Mapper;

class UseAttributeForEnumMapper extends AttributeMapper{

    public UseAttributeForEnumMapper(Mapper wrapped){
        super(wrapped, null, null);
    }

    /**
     * @deprecated only used for Java 1.4 support
     */
    public static boolean isEnum(Class type){
        while (type != null && type != Object.class){
            if (type.getName().equals("java.lang.Enum")){
                return true;
            }
            type = type.getSuperclass();
        }
        return false;
    }

    @Override
    public boolean shouldLookForSingleValueConverter(String fieldName,Class type,Class definedIn){
        return isEnum(type);
    }

    @Override
    public SingleValueConverter getConverterFromItemType(String fieldName,Class type,Class definedIn){
        return null;
    }

    @Override
    public SingleValueConverter getConverterFromAttribute(Class definedIn,String attribute,Class type){
        return null;
    }

    static Mapper createEnumMapper(final Mapper mapper){
        try{
            Class enumMapperClass = Class.forName("com.feilong.lib.xstream.mapper.EnumMapper", true, Mapper.class.getClassLoader());
            return (Mapper) DependencyInjectionFactory.newInstance(
                            enumMapperClass,
                            new Object[] { new UseAttributeForEnumMapper(mapper.lookupMapperOfType(DefaultMapper.class)) });
        }catch (Exception e){
            return null;
        }
    }
}