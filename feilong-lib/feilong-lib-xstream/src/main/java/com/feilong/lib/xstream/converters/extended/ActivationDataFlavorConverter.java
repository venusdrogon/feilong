/*
 * Copyright (C) 2015 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 *
 * Created on 21.06.2015 by Joerg Schaible
 */
package com.feilong.lib.xstream.converters.extended;

import javax.activation.ActivationDataFlavor;

import com.feilong.lib.xstream.converters.ConversionException;
import com.feilong.lib.xstream.converters.Converter;
import com.feilong.lib.xstream.converters.MarshallingContext;
import com.feilong.lib.xstream.converters.UnmarshallingContext;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;

/**
 * Converts an {@link ActivationDataFlavor}.
 *
 * @author J&ouml;rg Schaible
 * @since 1.4.9
 */
public class ActivationDataFlavorConverter implements Converter{

    @Override
    public boolean canConvert(final Class type){
        return type == ActivationDataFlavor.class;
    }

    @Override
    public void marshal(final Object source,final HierarchicalStreamWriter writer,final MarshallingContext context){
        final ActivationDataFlavor dataFlavor = (ActivationDataFlavor) source;
        final String mimeType = dataFlavor.getMimeType();
        if (mimeType != null){
            writer.startNode("mimeType");
            writer.setValue(mimeType);
            writer.endNode();
        }
        final String name = dataFlavor.getHumanPresentableName();
        if (name != null){
            writer.startNode("humanRepresentableName");
            writer.setValue(name);
            writer.endNode();
        }
        final Class representationClass = dataFlavor.getRepresentationClass();
        if (representationClass != null){
            writer.startNode("representationClass");
            context.convertAnother(representationClass);
            writer.endNode();
        }
    }

    @Override
    public Object unmarshal(final HierarchicalStreamReader reader,final UnmarshallingContext context){
        String mimeType = null;
        String name = null;
        Class type = null;
        while (reader.hasMoreChildren()){
            reader.moveDown();

            final String elementName = reader.getNodeName();
            if (elementName.equals("mimeType")){
                mimeType = reader.getValue();
            }else if (elementName.equals("humanRepresentableName")){
                name = reader.getValue();
            }else if (elementName.equals("representationClass")){
                type = (Class) context.convertAnother(null, Class.class);
            }else{
                final ConversionException exception = new ConversionException("Unknown child element");
                exception.add("element", reader.getNodeName());
                throw exception;
            }
            reader.moveUp();
        }
        ActivationDataFlavor dataFlavor = null;
        try{
            if (type == null){
                dataFlavor = new ActivationDataFlavor(mimeType, name);
            }else if (mimeType == null){
                dataFlavor = new ActivationDataFlavor(type, name);
            }else{
                dataFlavor = new ActivationDataFlavor(type, mimeType, name);
            }
        }catch (final IllegalArgumentException ex){
            throw new ConversionException(ex);
        }catch (final NullPointerException ex){
            throw new ConversionException(ex);
        }
        return dataFlavor;
    }
}
