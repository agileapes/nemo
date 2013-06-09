/*
 * Copyright (c) 2013. AgileApes (http://www.agileapes.scom/), and
 * associated organization.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 */

package com.agileapes.nemo.value.impl;

import com.agileapes.nemo.value.ValueReader;
import com.agileapes.nemo.value.ValueReaderContextAware;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * This extension allows the value readers to be discovered from within the Spring application
 * context
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/4, 19:12)
 */
public class SpringValueReaderContext extends DefaultValueReaderContext implements BeanFactoryPostProcessor {

    private static final Logger logger = Logger.getLogger(SpringValueReaderContext.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        handleValueReaders(beanFactory);
        handleContextAwareBeans(beanFactory);
    }

    private void handleValueReaders(ConfigurableListableBeanFactory beanFactory) {
        final String[] names = beanFactory.getBeanNamesForType(ValueReader.class);
        for (String name : names) {
            if (beanFactory.isSingleton(name)) {
                logger.info("Discovered value reader " + name);
                final ValueReader reader = beanFactory.getBean(name, ValueReader.class);
                if (!reader.equals(this)) {
                    add(reader);
                }
            }
        }
    }

    private void handleContextAwareBeans(ConfigurableListableBeanFactory beanFactory) {
        final String[] names = beanFactory.getBeanNamesForType(ValueReaderContextAware.class);
        for (String name : names) {
            if (beanFactory.isSingleton(name)) {
                beanFactory.getBean(name, ValueReaderContextAware.class).setValueReaderContext(this);
            }
        }
    }

}