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

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This value reader will read dates (both {@link java.util.Date} and {@link java.sql.Date}, as
 * they are the most popular formats for dates) using the format <code>yyyy/mm/dd [hh:mm[:ss]]</code>
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/5, 15:56)
 */
public class DateValueReader implements ValueReader {

    @Override
    public boolean handles(Class<?> type) {
        return Date.class.equals(type) || java.sql.Date.class.equals(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E read(String text, Class<E> type) {
        if (Date.class.equals(type)) {
            final Matcher matcher = Pattern.compile("(\\d+)/(\\d+)/(\\d+)(?:\\s+(\\d+):(\\d+)(?::(\\d+))?)?").matcher(text);
            if (matcher.find()) {
                int year = Integer.parseInt(matcher.group(1));
                int month = Integer.parseInt(matcher.group(2));
                int day = Integer.parseInt(matcher.group(3));
                int hour = matcher.groupCount() > 3 ? Integer.parseInt(matcher.group(4)) : 0;
                int minute = matcher.groupCount() > 3 ? Integer.parseInt(matcher.group(5)) : 0;
                int second = matcher.groupCount() > 5 ? Integer.parseInt(matcher.group(6)) : 0;
                final GregorianCalendar calendar = new GregorianCalendar(year, month, day, hour, minute, second);
                return (E) calendar.getTime();
            }
        } else if (java.sql.Date.class.equals(type)) {
            return (E) new java.sql.Date(read(text, Date.class).getTime());
        }
        throw new IllegalArgumentException();
    }
}
