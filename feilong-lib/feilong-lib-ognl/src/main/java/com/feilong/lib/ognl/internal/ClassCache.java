package com.feilong.lib.ognl.internal;

import com.feilong.lib.ognl.ClassCacheInspector;

/**
 * This is a highly specialized map for storing values keyed by Class objects.
 */
public interface ClassCache{

    void setClassInspector(ClassCacheInspector inspector);

    void clear();

    int getSize();

    Object get(Class key);

    Object put(Class key,Object value);
}
