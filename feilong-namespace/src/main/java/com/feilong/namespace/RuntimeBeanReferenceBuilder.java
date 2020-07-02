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
package com.feilong.namespace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;

/**
 * 构造 {@link RuntimeBeanReference}.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 2.0.0
 */
public class RuntimeBeanReferenceBuilder{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeBeanReferenceBuilder.class);

    /** Don't let anyone instantiate this class. */
    private RuntimeBeanReferenceBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param parserContext
     *            the parser context
     * @param beanDefinitionBuilder
     *            the bean definition builder
     * @return the runtime bean reference
     */
    public static RuntimeBeanReference build(ParserContext parserContext,BeanDefinitionBuilder beanDefinitionBuilder){
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        String beanName = parserContext.getReaderContext().generateBeanName(beanDefinition);

        //---------------------------------------------------------------
        LOGGER.debug("generateBeanName:[{}]", beanName);
        //---------------------------------------------------------------
        parserContext.registerBeanComponent(new BeanComponentDefinition(beanDefinition, beanName));

        return new RuntimeBeanReference(beanName);
    }
}
