/*
 * Copyright (C) 2004, 2005 Joe Walnes.
 * Copyright (C) 2006, 2007, 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 03. April 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.core;

import com.feilong.lib.xstream.converters.ConverterLookup;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;
import com.feilong.lib.xstream.mapper.Mapper;

public class ReferenceByXPathMarshallingStrategy extends AbstractTreeMarshallingStrategy{

    public static int RELATIVE    = 0;

    public static int ABSOLUTE    = 1;

    public static int SINGLE_NODE = 2;

    private final int mode;

    public ReferenceByXPathMarshallingStrategy(int mode){
        this.mode = mode;
    }

    @Override
    protected TreeUnmarshaller createUnmarshallingContext(
                    Object root,
                    HierarchicalStreamReader reader,
                    ConverterLookup converterLookup,
                    Mapper mapper){
        return new ReferenceByXPathUnmarshaller(root, reader, converterLookup, mapper);
    }

    @Override
    protected TreeMarshaller createMarshallingContext(HierarchicalStreamWriter writer,ConverterLookup converterLookup,Mapper mapper){
        return new ReferenceByXPathMarshaller(writer, converterLookup, mapper, mode);
    }
}
