/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.jcr.resource.internal;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.commons.classloader.DynamicClassLoaderManager;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.io.Closeable;

/**
 * This is a helper class used to pass several services/data to the resource
 * and value map implementations.
 */
public class HelperData implements Closeable {

    private volatile String[] namespacePrefixes;

    private final ServiceTracker<DynamicClassLoaderManager, DynamicClassLoaderManager> dclmTracker;

    public HelperData(final BundleContext bundleContext) {
        this.dclmTracker = new ServiceTracker<>(bundleContext, DynamicClassLoaderManager.class, null);
        this.dclmTracker.open();
    }

    public String[] getNamespacePrefixes(final Session session)
    throws RepositoryException {
        if ( this.namespacePrefixes == null ) {
            this.namespacePrefixes = session.getNamespacePrefixes();
        }
        return this.namespacePrefixes;
    }

    public ClassLoader getDynamicClassLoader() {
        DynamicClassLoaderManager dclm = dclmTracker.getService();
        if ( dclm == null ) {
            return null;
        }
        return dclm.getDynamicClassLoader();
    }

    @Override
    public void close() {
        dclmTracker.close();
    }
}
