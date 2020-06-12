/*
 * Copyright (C) 2004, 2005 Joe Walnes.
 * Copyright (C) 2006, 2007, 2010, 2011, 2013, 2014, 2016, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 08. May 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.converters.collections;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.feilong.lib.xstream.converters.MarshallingContext;
import com.feilong.lib.xstream.converters.UnmarshallingContext;
import com.feilong.lib.xstream.converters.reflection.ObjectAccessException;
import com.feilong.lib.xstream.core.JVM;
import com.feilong.lib.xstream.core.util.Fields;
import com.feilong.lib.xstream.core.util.PresortedSet;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;
import com.feilong.lib.xstream.mapper.Mapper;

/**
 * Converts a java.util.TreeSet to XML, and serializes
 * the associated java.util.Comparator. The converter
 * assumes that the elements in the XML are already sorted
 * according the comparator.
 *
 * @author Joe Walnes
 * @author J&ouml;rg Schaible
 */
public class TreeSetConverter extends CollectionConverter{

    private transient TreeMapConverter treeMapConverter;

    private final static Field         sortedMapField;

    private final static Object        constantValue;
    static{
        Object value = null;
        sortedMapField = JVM.hasOptimizedTreeSetAddAll() ? Fields.locate(TreeSet.class, SortedMap.class, false) : null;
        if (sortedMapField != null){
            TreeSet set = new TreeSet();
            set.add("1");
            set.add("2");

            Map backingMap = null;
            try{
                backingMap = (Map) sortedMapField.get(set);
            }catch (final IllegalAccessException e){
                // give up;
            }
            if (backingMap != null){
                Object[] values = backingMap.values().toArray();
                if (values[0] == values[1]){
                    value = values[0];
                }
            }
        }else{
            Field valueField = Fields.locate(TreeSet.class, Object.class, true);
            if (valueField != null){
                try{
                    value = valueField.get(null);
                }catch (final IllegalAccessException e){
                    // give up;
                }
            }
        }
        constantValue = value;
    }

    public TreeSetConverter(Mapper mapper){
        super(mapper, TreeSet.class);
        readResolve();
    }

    @Override
    public void marshal(Object source,HierarchicalStreamWriter writer,MarshallingContext context){
        SortedSet sortedSet = (SortedSet) source;
        treeMapConverter.marshalComparator(sortedSet.comparator(), writer, context);
        super.marshal(source, writer, context);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader,UnmarshallingContext context){
        TreeSet result = null;
        final TreeMap treeMap;
        Comparator unmarshalledComparator = treeMapConverter.unmarshalComparator(reader, context, null);
        boolean inFirstElement = unmarshalledComparator instanceof Mapper.Null;
        Comparator comparator = inFirstElement ? null : unmarshalledComparator;
        if (sortedMapField != null){
            TreeSet possibleResult = comparator == null ? new TreeSet() : new TreeSet(comparator);
            Object backingMap = null;
            try{
                backingMap = sortedMapField.get(possibleResult);
            }catch (IllegalAccessException e){
                throw new ObjectAccessException("Cannot get backing map of TreeSet", e);
            }
            if (backingMap instanceof TreeMap){
                treeMap = (TreeMap) backingMap;
                result = possibleResult;
            }else{
                treeMap = null;
            }
        }else{
            treeMap = null;
        }
        if (treeMap == null){
            final PresortedSet set = new PresortedSet(comparator);
            result = comparator == null ? new TreeSet() : new TreeSet(comparator);
            if (inFirstElement){
                // we are already within the first element
                addCurrentElementToCollection(reader, context, result, set);
                reader.moveUp();
            }
            populateCollection(reader, context, result, set);
            if (set.size() > 0){
                result.addAll(set); // comparator will not be called if internally optimized
            }
        }else{
            treeMapConverter.populateTreeMap(reader, context, treeMap, unmarshalledComparator);
        }
        return result;
    }

    private Object readResolve(){
        treeMapConverter = new TreeMapConverter(mapper()){

            @Override
            protected void populateMap(HierarchicalStreamReader reader,UnmarshallingContext context,Map map,final Map target){
                populateCollection(reader, context, new AbstractList(){

                    @Override
                    public boolean add(Object object){
                        return target.put(object, constantValue != null ? constantValue : object) != null;
                    }

                    @Override
                    public Object get(int location){
                        return null;
                    }

                    @Override
                    public int size(){
                        return target.size();
                    }
                });
            }

            @Override
            protected void putCurrentEntryIntoMap(HierarchicalStreamReader reader,UnmarshallingContext context,Map map,Map target){
                final Object key = readItem(reader, context, map); // call readBareItem when deprecated method is removed
                target.put(key, key);
            }
        };
        return this;
    }
}
