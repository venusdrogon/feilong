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
package com.feilong.lib.json.util;

import com.feilong.lib.json.processors.PropertyNameProcessor;

/**
 * The Class KeyUpdate.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public class KeyUpdater{

    /** Don't let anyone instantiate this class. */
    private KeyUpdater(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Update.
     *
     * @param beanClass
     *            the bean class
     * @param key
     *            the key
     * @param propertyNameProcessor
     *            the property name processor
     * @return the string
     */
    public static String update(Class<?> beanClass,String key,PropertyNameProcessor propertyNameProcessor){
        return null == propertyNameProcessor ? //
                        key : //
                        propertyNameProcessor.processPropertyName(beanClass, key);
    }
}
