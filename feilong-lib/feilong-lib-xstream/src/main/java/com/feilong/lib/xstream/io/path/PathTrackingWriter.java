/*
 * Copyright (C) 2004, 2005, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2011 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 07. March 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.io.path;

import com.feilong.lib.xstream.io.AbstractWriter;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;
import com.feilong.lib.xstream.io.WriterWrapper;

/**
 * Wrapper for HierarchicalStreamWriter that tracks the path (a subset of XPath) of the current node that is being written.
 *
 * @see PathTracker
 * @see Path
 *
 * @author Joe Walnes
 */
public class PathTrackingWriter extends WriterWrapper{

    private final PathTracker pathTracker;

    private final boolean     isNameEncoding;

    public PathTrackingWriter(HierarchicalStreamWriter writer, PathTracker pathTracker){
        super(writer);
        this.isNameEncoding = writer.underlyingWriter() instanceof AbstractWriter;
        this.pathTracker = pathTracker;
    }

    @Override
    public void startNode(String name){
        pathTracker.pushElement(isNameEncoding ? ((AbstractWriter) wrapped.underlyingWriter()).encodeNode(name) : name);
        super.startNode(name);
    }

    @Override
    public void startNode(String name,Class clazz){
        pathTracker.pushElement(isNameEncoding ? ((AbstractWriter) wrapped.underlyingWriter()).encodeNode(name) : name);
        super.startNode(name, clazz);
    }

    @Override
    public void endNode(){
        super.endNode();
        pathTracker.popElement();
    }
}
