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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;

/**
 * Generic extension of {@link ImageParser} intended to facilitate easier
 * implementation by subclasses. This class handles conversion of input
 * {@link ImagingParameters} instances into type-specific instances as needed.
 */
public abstract class GenericImageParser<P extends ImagingParameters> extends ImageParser {

    private final Class<P> parametersType;

    protected GenericImageParser(final Class<P> parametersType) {
        this.parametersType = parametersType;
    }

    /** {@inheritDoc} */
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final ImagingParameters params)
            throws ImageReadException, IOException {
        return getMetadataInternal(byteSource, getParameters(params));
    }

    /** {@inheritDoc} */
    @Override
    public ImageInfo getImageInfo(final ByteSource byteSource, final ImagingParameters params)
            throws ImageReadException, IOException {
        return getImageInfoInternal(byteSource, getParameters(params));
    }

    /** {@inheritDoc} */
    @Override
    public BufferedImage getBufferedImage(final ByteSource byteSource, final ImagingParameters params)
            throws ImageReadException, IOException {
        return getBufferedImageInternal(byteSource, getParameters(params));
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final ImagingParameters params)
            throws ImageReadException, IOException {
        return getImageSize(byteSource, getParameters(params));
    }

    /** {@inheritDoc} */
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final ImagingParameters params)
            throws ImageReadException, IOException {
        return getICCProfileBytesInternal(byteSource, getParameters(params));
    }

    /** {@inheritDoc} */
    @Override
    public void writeImage(final BufferedImage src, final OutputStream os, final ImagingParameters params)
            throws ImageWriteException, IOException {
        try (OutputStream ensureClosed = os) {
            writeImageInternal(src, ensureClosed, getParameters(params));
        }
    }

    /**
     * Get a format-specific parameters instance based on the argument.
     * @param params
     * @return
     */
    protected P getParameters(final ImagingParameters params) {
        if (params == null) {
            return createDefaultParameters();
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
            return createParameters(params);
        }

        throw new IllegalArgumentException("Invalid imaging parameters: expected type " +
                parametersType.getName() + " or a super class but was " + actualType.getName());
    }

    /**
     * Create a format-specific parameters object containing default values. Subclasses
     * must not return null.
     * @return
     */
    protected abstract P createDefaultParameters();

    /**
     * Create a format-specific parameters object populated with values from
     * the argument. Subclasses must not return null.
     * @param params
     * @return
     */
    protected abstract P createParameters(final ImagingParameters params);

    /**
     * Internal method to read image metadata.
     * @param byteSource
     * @param params format-specific parameters object; guaranteed to not be null
     * @return
     * @throws ImageReadException
     * @throws IOException
     */
    protected abstract ImageMetadata getMetadataInternal(ByteSource byteSource, P params)
            throws ImageReadException, IOException;

    /**
     * Internal method to read image information.
     * @param byteSource
     * @param params format-specific parameters object; guaranteed to not be null
     * @return
     * @throws ImageReadException
     * @throws IOException
     */
    protected abstract ImageInfo getImageInfoInternal(ByteSource byteSource, P params)
            throws ImageReadException, IOException;

    /**
     * Internal method to read a buffered image.
     * @param byteSource
     * @param params format-specific parameters object; guaranteed to not be null
     * @return
     * @throws ImageReadException
     * @throws IOException
     */
    protected abstract BufferedImage getBufferedImageInternal(ByteSource byteSource, P params)
            throws ImageReadException, IOException;

    /**
     * Internal method to get the image size.
     * @param byteSource
     * @param params format-specific parameters object; guaranteed to not be null
     * @return
     * @throws ImageReadException
     * @throws IOException
     */
    protected abstract Dimension getImageSizeInternal(ByteSource byteSource, P params)
            throws ImageReadException, IOException;

    /**
     * Internal method to get the ICC profile bytes.
     * @param byteSource
     * @param params format-specific parameters object; guaranteed to not be null
     * @return
     * @throws ImageReadException
     * @throws IOException
     */
    protected abstract byte[] getICCProfileBytesInternal(ByteSource byteSource, P params)
            throws ImageReadException, IOException;

    /**
     * Internal method to write an image. Subclasses do not need to close the output stream.
     * @param src
     * @param os
     * @param params format-specific parameters object; guaranteed to not be null
     * @throws ImageWriteException
     * @throws IOException
     */
    protected abstract void writeImageInternal(final BufferedImage src, final OutputStream os, final P params)
            throws ImageWriteException, IOException;
}
