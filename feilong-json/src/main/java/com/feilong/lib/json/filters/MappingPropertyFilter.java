/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.feilong.lib.json.filters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.feilong.lib.json.util.PropertyFilter;

/**
 * The Class MappingPropertyFilter.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public abstract class MappingPropertyFilter implements PropertyFilter{

    /** The filters. */
    private final Map filters = new HashMap();

    //---------------------------------------------------------------

    /**
     * Instantiates a new mapping property filter.
     */
    public MappingPropertyFilter(){
        this(null);
    }

    /**
     * Instantiates a new mapping property filter.
     *
     * @param filters
     *            the filters
     */
    public MappingPropertyFilter(Map filters){
        if (filters != null){
            for (Iterator i = filters.entrySet().iterator(); i.hasNext();){
                Map.Entry entry = (Map.Entry) i.next();
                Object key = entry.getKey();
                Object filter = entry.getValue();
                if (filter instanceof PropertyFilter){
                    this.filters.put(key, filter);
                }
            }
        }
    }

    /**
     * 添加 property filter.
     *
     * @param target
     *            the target
     * @param filter
     *            the filter
     */
    public void addPropertyFilter(Object target,PropertyFilter filter){
        if (filter != null){
            filters.put(target, filter);
        }
    }

    /**
     * Apply.
     *
     * @param source
     *            the source
     * @param name
     *            the name
     * @param value
     *            the value
     * @return true, if successful
     */
    @Override
    public boolean apply(Object source,String name,Object value){
        for (Iterator i = filters.entrySet().iterator(); i.hasNext();){
            Map.Entry entry = (Map.Entry) i.next();
            Object key = entry.getKey();
            if (keyMatches(key, source, name, value)){
                PropertyFilter filter = (PropertyFilter) entry.getValue();
                return filter.apply(source, name, value);
            }
        }
        return false;
    }

    /**
     * 删除 property filter.
     *
     * @param target
     *            the target
     */
    public void removePropertyFilter(Object target){
        if (target != null){
            filters.remove(target);
        }
    }

    /**
     * Key matches.
     *
     * @param key
     *            the key
     * @param source
     *            the source
     * @param name
     *            the name
     * @param value
     *            the value
     * @return true, if successful
     */
    protected abstract boolean keyMatches(Object key,Object source,String name,Object value);
}