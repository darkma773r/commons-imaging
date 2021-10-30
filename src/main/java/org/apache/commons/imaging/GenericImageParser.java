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
package org.apache.commons.imaging;

/**
 * Generic extension of {@link ImageParser} intended to facilitate easier
 * implementation by subclasses. This class handles conversion of input
 * {@link ImagingParameters} instances into type-specific instances as needed.
 */
public abstract class GenericImageParser<P extends ImagingParameters> extends ImageParser {

    /** Type of parameters object used by this parser. */
    private final Class<P> parametersType;

    protected GenericImageParser(final Class<P> parametersType) {
        this.parametersType = parametersType;
    }

    /** {@inheritDoc} */
    @Override
    public abstract P getDefaultParameters();

    /**
     * Get a format-specific parameters instance based on the argument.
     * @param params
     * @return
     */
    @Override
    protected P normalizeParameters(final ImagingParameters params) {
        if (params == null) {
            return getDefaultParameters();
        }
        Class<?> actualType = params.getClass();
        if (actualType.equals(parametersType)) {
            // we were given the expected type so just cast the existing value
            return parametersType.cast(params);
        } else if (actualType.isAssignableFrom(parametersType)) {
            // the given parameters object is a super class of
            // our type; create a new parameters object of the expected
            // type and populate as many fields as possible from the
            // argument
            return copyParameters(params);
        }

        throw new IllegalArgumentException("Invalid imaging parameters: expected type " +
                parametersType.getName() + " or a super class but was " + actualType.getName());
    }

    /**
     * Create a format-specific parameters object populated with values from
     * the argument. Subclasses must not return null.
     * @param params
     * @return
     */
    protected abstract P copyParameters(final ImagingParameters params);
}
