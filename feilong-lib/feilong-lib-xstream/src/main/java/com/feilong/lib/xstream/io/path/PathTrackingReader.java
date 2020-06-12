/*
 * Copyright (C) 2004, 2005 Joe Walnes.
 * Copyright (C) 2006, 2007 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 03. April 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.io.path;

import com.feilong.lib.xstream.converters.ErrorWriter;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.ReaderWrapper;

/**
 * Wrapper for HierarchicalStreamReader that tracks the path (a subset of XPath) of the current node that is being read.
 *
 * @see PathTracker
 * @see Path
 *
 * @author Joe Walnes
 */
public class PathTrackingReader extends ReaderWrapper{

    private final PathTracker pathTracker;

    public PathTrackingReader(HierarchicalStreamReader reader, PathTracker pathTracker){
        super(reader);
        this.pathTracker = pathTracker;
        pathTracker.pushElement(getNodeName());
    }

    @Override
    public void moveDown(){
        super.moveDown();
        pathTracker.pushElement(getNodeName());
    }

    @Override
    public void moveUp(){
        super.moveUp();
        pathTracker.popElement();
    }

    @Override
    public void appendErrors(ErrorWriter errorWriter){
        errorWriter.add("path", pathTracker.getPath().toString());
        super.appendErrors(errorWriter);
    }

}
