/*
 * Copyright (C) 2004, 2005, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2009, 2011 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 07. March 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.io.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.feilong.lib.xstream.io.naming.NameCoder;

public class DomReader extends AbstractDocumentReader{

    private Element            currentElement;

    private final StringBuffer textBuffer;

    private List               childElements;

    public DomReader(Element rootElement){
        this(rootElement, new XmlFriendlyNameCoder());
    }

    public DomReader(Document document){
        this(document.getDocumentElement());
    }

    /**
     * @since 1.4
     */
    public DomReader(Element rootElement, NameCoder nameCoder){
        super(rootElement, nameCoder);
        textBuffer = new StringBuffer();
    }

    /**
     * @since 1.4
     */
    public DomReader(Document document, NameCoder nameCoder){
        this(document.getDocumentElement(), nameCoder);
    }

    @Override
    public String getNodeName(){
        return decodeNode(currentElement.getTagName());
    }

    @Override
    public String getValue(){
        NodeList childNodes = currentElement.getChildNodes();
        textBuffer.setLength(0);
        int length = childNodes.getLength();
        for (int i = 0; i < length; i++){
            Node childNode = childNodes.item(i);
            if (childNode instanceof Text){
                Text text = (Text) childNode;
                textBuffer.append(text.getData());
            }
        }
        return textBuffer.toString();
    }

    @Override
    public String getAttribute(String name){
        Attr attribute = currentElement.getAttributeNode(encodeAttribute(name));
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public String getAttribute(int index){
        return ((Attr) currentElement.getAttributes().item(index)).getValue();
    }

    @Override
    public int getAttributeCount(){
        return currentElement.getAttributes().getLength();
    }

    @Override
    public String getAttributeName(int index){
        return decodeAttribute(((Attr) currentElement.getAttributes().item(index)).getName());
    }

    @Override
    protected Object getParent(){
        return currentElement.getParentNode();
    }

    @Override
    protected Object getChild(int index){
        return childElements.get(index);
    }

    @Override
    protected int getChildCount(){
        return childElements.size();
    }

    @Override
    protected void reassignCurrentElement(Object current){
        currentElement = (Element) current;
        NodeList childNodes = currentElement.getChildNodes();
        childElements = new ArrayList();
        for (int i = 0; i < childNodes.getLength(); i++){
            Node node = childNodes.item(i);
            if (node instanceof Element){
                childElements.add(node);
            }
        }
    }

    @Override
    public String peekNextChild(){
        NodeList childNodes = currentElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++){
            Node node = childNodes.item(i);
            if (node instanceof Element){
                return decodeNode(((Element) node).getTagName());
            }
        }
        return null;
    }
}
