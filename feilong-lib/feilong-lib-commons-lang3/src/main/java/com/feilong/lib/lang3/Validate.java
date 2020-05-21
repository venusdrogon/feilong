/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.lib.lang3;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This class assists in validating arguments. The validation methods are
 * based along the following principles:
 * <ul>
 * <li>An invalid {@code null} argument causes a {@link NullPointerException}.</li>
 * <li>A non-{@code null} argument causes an {@link IllegalArgumentException}.</li>
 * <li>An invalid index into an array/collection/map/string causes an {@link IndexOutOfBoundsException}.</li>
 * </ul>
 *
 * <p>
 * All exceptions messages are
 * <a href="http://docs.oracle.com/javase/1.5.0/docs/api/java/util/Formatter.html#syntax">format strings</a>
 * as defined by the Java platform. For example:
 * </p>
 *
 * <pre>
 * Validate.isTrue(i &gt; 0, "The value must be greater than zero: %d", i);
 * Validate.notNull(surname, "The surname must not be %s", null);
 * </pre>
 *
 * <p>
 * #ThreadSafe#
 * </p>
 * 
 * @see java.lang.String#format(String, Object...)
 * @since 2.0
 * @deprecated pls use com.feilong.core.Validate
 */
@Deprecated
public class Validate{

    private static final String DEFAULT_IS_NULL_EX_MESSAGE                = "The validated object is null";

    private static final String DEFAULT_NO_NULL_ELEMENTS_ARRAY_EX_MESSAGE = "The validated array contains null element at index: %d";

    private static final String DEFAULT_NOT_BLANK_EX_MESSAGE              = "The validated character sequence is blank";

    // isTrue
    //---------------------------------------------------------------------------------

    /**
     * <p>
     * Validate that the argument condition is {@code true}; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.
     * </p>
     *
     * <pre>
     * Validate.isTrue(i &gt;= min &amp;&amp; i &lt;= max, "The value must be between &#37;d and &#37;d", min, max);
     * Validate.isTrue(myObject.isOk(), "The object is not okay");
     * </pre>
     *
     * @param expression
     *            the boolean expression to check
     * @param message
     *            the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values
     *            the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException
     *             if expression is {@code false}
     * @see #isTrue(boolean)
     * @see #isTrue(boolean, String, long)
     * @see #isTrue(boolean, String, double)
     */
    public static void isTrue(final boolean expression,final String message,final Object...values){
        if (!expression){
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    // notNull
    //---------------------------------------------------------------------------------

    /**
     * <p>
     * Validate that the specified argument is not {@code null};
     * otherwise throwing an exception.
     *
     * <pre>
     * Validate.notNull(myObject, "The object must not be null");
     * </pre>
     *
     * <p>
     * The message of the exception is &quot;The validated object is
     * null&quot;.
     * </p>
     *
     * @param <T>
     *            the object type
     * @param object
     *            the object to check
     * @return the validated object (never {@code null} for method chaining)
     * @throws NullPointerException
     *             if the object is {@code null}
     * @see #notNull(Object, String, Object...)
     */
    public static <T> T notNull(final T object){
        return notNull(object, DEFAULT_IS_NULL_EX_MESSAGE);
    }

    /**
     * <p>
     * Validate that the specified argument is not {@code null};
     * otherwise throwing an exception with the specified message.
     *
     * <pre>
     * Validate.notNull(myObject, "The object must not be null");
     * </pre>
     *
     * @param <T>
     *            the object type
     * @param object
     *            the object to check
     * @param message
     *            the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values
     *            the optional values for the formatted exception message
     * @return the validated object (never {@code null} for method chaining)
     * @throws NullPointerException
     *             if the object is {@code null}
     * @see #notNull(Object)
     */
    public static <T> T notNull(final T object,final String message,final Object...values){
        return Objects.requireNonNull(object, () -> String.format(message, values));
    }

    // notEmpty collection
    //---------------------------------------------------------------------------------

    /**
     * <p>
     * Validate that the specified argument collection is neither {@code null}
     * nor a size of zero (no elements); otherwise throwing an exception
     * with the specified message.
     *
     * <pre>
     * Validate.notEmpty(myCollection, "The collection must not be empty");
     * </pre>
     *
     * @param <T>
     *            the collection type
     * @param collection
     *            the collection to check, validated not null by this method
     * @param message
     *            the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values
     *            the optional values for the formatted exception message, null array not recommended
     * @return the validated collection (never {@code null} method for chaining)
     * @throws NullPointerException
     *             if the collection is {@code null}
     * @throws IllegalArgumentException
     *             if the collection is empty
     * @see #notEmpty(Object[])
     */
    public static <T extends Collection<?>> T notEmpty(final T collection,final String message,final Object...values){
        Objects.requireNonNull(collection, () -> String.format(message, values));
        if (collection.isEmpty()){
            throw new IllegalArgumentException(String.format(message, values));
        }
        return collection;
    }

    // notEmpty map
    //---------------------------------------------------------------------------------

    /**
     * <p>
     * Validate that the specified argument map is neither {@code null}
     * nor a size of zero (no elements); otherwise throwing an exception
     * with the specified message.
     *
     * <pre>
     * Validate.notEmpty(myMap, "The map must not be empty");
     * </pre>
     *
     * @param <T>
     *            the map type
     * @param map
     *            the map to check, validated not null by this method
     * @param message
     *            the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values
     *            the optional values for the formatted exception message, null array not recommended
     * @return the validated map (never {@code null} method for chaining)
     * @throws NullPointerException
     *             if the map is {@code null}
     * @throws IllegalArgumentException
     *             if the map is empty
     * @see #notEmpty(Object[])
     */
    public static <T extends Map<?, ?>> T notEmpty(final T map,final String message,final Object...values){
        Objects.requireNonNull(map, () -> String.format(message, values));
        if (map.isEmpty()){
            throw new IllegalArgumentException(String.format(message, values));
        }
        return map;
    }

    // notEmpty string
    //---------------------------------------------------------------------------------

    /**
     * <p>
     * Validate that the specified argument character sequence is
     * neither {@code null} nor a length of zero (no characters);
     * otherwise throwing an exception with the specified message.
     *
     * <pre>
     * Validate.notEmpty(myString, "The string must not be empty");
     * </pre>
     *
     * @param <T>
     *            the character sequence type
     * @param chars
     *            the character sequence to check, validated not null by this method
     * @param message
     *            the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values
     *            the optional values for the formatted exception message, null array not recommended
     * @return the validated character sequence (never {@code null} method for chaining)
     * @throws NullPointerException
     *             if the character sequence is {@code null}
     * @throws IllegalArgumentException
     *             if the character sequence is empty
     * @see #notEmpty(CharSequence)
     */
    public static <T extends CharSequence> T notEmpty(final T chars,final String message,final Object...values){
        Objects.requireNonNull(chars, () -> String.format(message, values));
        if (chars.length() == 0){
            throw new IllegalArgumentException(String.format(message, values));
        }
        return chars;
    }

    // notBlank string
    //---------------------------------------------------------------------------------

    /**
     * <p>
     * Validate that the specified argument character sequence is
     * neither {@code null}, a length of zero (no characters), empty
     * nor whitespace; otherwise throwing an exception with the specified
     * message.
     *
     * <pre>
     * Validate.notBlank(myString, "The string must not be blank");
     * </pre>
     *
     * @param <T>
     *            the character sequence type
     * @param chars
     *            the character sequence to check, validated not null by this method
     * @param message
     *            the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values
     *            the optional values for the formatted exception message, null array not recommended
     * @return the validated character sequence (never {@code null} method for chaining)
     * @throws NullPointerException
     *             if the character sequence is {@code null}
     * @throws IllegalArgumentException
     *             if the character sequence is blank
     * @see #notBlank(CharSequence)
     *
     * @since 3.0
     */
    public static <T extends CharSequence> T notBlank(final T chars,final String message,final Object...values){
        Objects.requireNonNull(chars, () -> String.format(message, values));
        if (StringUtils.isBlank(chars)){
            throw new IllegalArgumentException(String.format(message, values));
        }
        return chars;
    }

    /**
     * <p>
     * Validate that the specified argument character sequence is
     * neither {@code null}, a length of zero (no characters), empty
     * nor whitespace; otherwise throwing an exception.
     *
     * <pre>
     * Validate.notBlank(myString);
     * </pre>
     *
     * <p>
     * The message in the exception is &quot;The validated character
     * sequence is blank&quot;.
     * </p>
     *
     * @param <T>
     *            the character sequence type
     * @param chars
     *            the character sequence to check, validated not null by this method
     * @return the validated character sequence (never {@code null} method for chaining)
     * @throws NullPointerException
     *             if the character sequence is {@code null}
     * @throws IllegalArgumentException
     *             if the character sequence is blank
     * @see #notBlank(CharSequence, String, Object...)
     *
     * @since 3.0
     */
    public static <T extends CharSequence> T notBlank(final T chars){
        return notBlank(chars, DEFAULT_NOT_BLANK_EX_MESSAGE);
    }

    // noNullElements array
    //---------------------------------------------------------------------------------

    /**
     * <p>
     * Validate that the specified argument array is neither
     * {@code null} nor contains any elements that are {@code null};
     * otherwise throwing an exception with the specified message.
     *
     * <pre>
     * Validate.noNullElements(myArray, "The array contain null at position %d");
     * </pre>
     *
     * <p>
     * If the array is {@code null}, then the message in the exception
     * is &quot;The validated object is null&quot;.
     * </p>
     *
     * <p>
     * If the array has a {@code null} element, then the iteration
     * index of the invalid element is appended to the {@code values}
     * argument.
     * </p>
     *
     * @param <T>
     *            the array type
     * @param array
     *            the array to check, validated not null by this method
     * @param message
     *            the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values
     *            the optional values for the formatted exception message, null array not recommended
     * @return the validated array (never {@code null} method for chaining)
     * @throws NullPointerException
     *             if the array is {@code null}
     * @throws IllegalArgumentException
     *             if an element is {@code null}
     * @see #noNullElements(Object[])
     */
    public static <T> T[] noNullElements(final T[] array,final String message,final Object...values){
        notNull(array);
        for (int i = 0; i < array.length; i++){
            if (array[i] == null){
                final Object[] values2 = ArrayUtils.add(values, Integer.valueOf(i));
                throw new IllegalArgumentException(String.format(message, values2));
            }
        }
        return array;
    }

    /**
     * <p>
     * Validate that the specified argument array is neither
     * {@code null} nor contains any elements that are {@code null};
     * otherwise throwing an exception.
     * </p>
     *
     * <pre>
     * Validate.noNullElements(myArray);
     * </pre>
     *
     * <p>
     * If the array is {@code null}, then the message in the exception
     * is &quot;The validated object is null&quot;.
     * </p>
     *
     * <p>
     * If the array has a {@code null} element, then the message in the
     * exception is &quot;The validated array contains null element at index:
     * &quot; followed by the index.
     * </p>
     *
     * @param <T>
     *            the array type
     * @param array
     *            the array to check, validated not null by this method
     * @return the validated array (never {@code null} method for chaining)
     * @throws NullPointerException
     *             if the array is {@code null}
     * @throws IllegalArgumentException
     *             if an element is {@code null}
     * @see #noNullElements(Object[], String, Object...)
     */
    public static <T> T[] noNullElements(final T[] array){
        return noNullElements(array, DEFAULT_NO_NULL_ELEMENTS_ARRAY_EX_MESSAGE);
    }

    // inclusiveBetween
    //---------------------------------------------------------------------------------

    /**
     * Validate that the specified primitive value falls between the two
     * inclusive values specified; otherwise, throws an exception with the
     * specified message.
     *
     * <pre>
     * Validate.inclusiveBetween(0, 2, 1, "Not in range");
     * </pre>
     *
     * @param start
     *            the inclusive start value
     * @param end
     *            the inclusive end value
     * @param value
     *            the value to validate
     * @param message
     *            the exception message if invalid, not null
     *
     * @throws IllegalArgumentException
     *             if the value falls outside the boundaries
     *
     * @since 3.3
     */
    public static void inclusiveBetween(final long start,final long end,final long value,final String message){
        // TODO when breaking BC, consider returning value
        if (value < start || value > end){
            throw new IllegalArgumentException(message);
        }
    }

}
