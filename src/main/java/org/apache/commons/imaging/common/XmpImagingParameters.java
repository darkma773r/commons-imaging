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

package org.apache.commons.imaging.common;

import org.apache.commons.imaging.ImagingParameters;

/**
 * Parameters for formats that support Xmp.
 * @since 1.0-alpha3
 */
public class XmpImagingParameters extends ImagingParameters {

    private String xmpXml;

    public XmpImagingParameters() {}

    public XmpImagingParameters(final ImagingParameters other) {
        super(other);

        if (other instanceof XmpImagingParameters) {
            XmpImagingParameters otherXmp = (XmpImagingParameters) other;

            this.xmpXml = otherXmp.xmpXml;
        }
    }

    public String getXmpXml() {
        return xmpXml;
    }

    public void setXmpXml(String xmpXml) {
        this.xmpXml = xmpXml;
    }

}
