/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */


package org.apache.isis.runtimes.dflt.objectstores.sql;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.commons.debug.DebuggableWithTitle;
import org.apache.isis.core.commons.debug.DebugString;
import org.apache.isis.core.commons.exceptions.IsisException;
import org.apache.isis.core.commons.factory.InstanceCreationException;
import org.apache.isis.core.commons.factory.InstanceUtil;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.runtimes.dflt.runtime.context.IsisContext;
import org.apache.isis.runtimes.dflt.runtime.transaction.ObjectPersistenceException;


public class ObjectMappingLookup implements DebuggableWithTitle {
    private static final Logger LOG = Logger.getLogger(ObjectMappingLookup.class);
    private DatabaseConnectorPool connectionPool;
    private final Map<ObjectSpecification, ObjectMapping> mappings = new HashMap<ObjectSpecification, ObjectMapping>();
    private ObjectMappingFactory objectMappingFactory;
    private FieldMappingLookup fieldMappingLookup;

    public ObjectMapping getMapping(final ObjectSpecification spec, DatabaseConnector connection) {
        ObjectMapping mapping = mappings.get(spec);
        if (mapping == null) {
            String propertiesBase = SqlObjectStore.BASE_NAME + ".automapper.default";
            mapping = objectMappingFactory.createMapper(spec.getFullIdentifier(), propertiesBase, fieldMappingLookup, this);
            add(spec, mapping, connection); 
        }
        LOG.debug("  mapper for " + spec.getSingularName() + " -> " + mapping);
        if (mapping == null) {
            throw new IsisException("No mapper for " + spec + " (no default mapper)");
        }
        return mapping;
    }

    public ObjectMapping getMapping(final ObjectAdapter object, DatabaseConnector connection) {
        return getMapping(object.getSpecification(), connection);
    }

    public void setConnectionPool(final DatabaseConnectorPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    // / ???
    public void setObjectMappingFactory(final ObjectMappingFactory mapperFactory) {
        this.objectMappingFactory = mapperFactory;
    }

    public void setValueMappingLookup(FieldMappingLookup fieldMappingLookup) {
        this.fieldMappingLookup = fieldMappingLookup;
    }

    private void add(final String className, final ObjectMapping mapper) {
        ObjectSpecification spec = IsisContext.getSpecificationLoader().loadSpecification(className);
        if (spec.getProperties().size() == 0) {
            throw new SqlObjectStoreException(spec.getFullIdentifier() + " has no fields to persist: " + spec);
        }
        add(spec, mapper, null);
    }

    public void add(final ObjectSpecification specification, final ObjectMapping mapper, DatabaseConnector connection) {
        LOG.debug("add mapper " + mapper + " for " + specification);
        if (connection == null){
        	connection = connectionPool.acquire();
        }
        mapper.startup(connection, this);
        connectionPool.release(connection);
        mappings.put(specification, mapper);
    }

    public void init() {
        fieldMappingLookup.init();
        
        String prefix = SqlObjectStore.BASE_NAME + ".mapper.";
        IsisConfiguration subset = IsisContext.getConfiguration().createSubset(prefix);
        for (String className : subset) {
            String value = subset.getString(className);
            
            if (value.startsWith("auto.")) {
                String propertiesBase = SqlObjectStore.BASE_NAME + ".automapper." + value.substring(5) + ".";
                add(className, objectMappingFactory.createMapper(className, propertiesBase, fieldMappingLookup, this));
            } else if (value.trim().equals("auto")) {
                String propertiesBase = SqlObjectStore.BASE_NAME + ".automapper.default";
                add(className, objectMappingFactory.createMapper(className, propertiesBase, fieldMappingLookup, this));
            } else {
                LOG.debug("mapper " + className + "=" + value);
                
                try {
                    add(className, InstanceUtil.createInstance(value, ObjectMapping.class));
                } catch (ObjectPersistenceException ex) {
                    throw new InstanceCreationException("Failed to set up mapper for " + className, ex);
                }
            }
        }
    }

    public void shutdown() {
        for (ObjectMapping mapping : mappings.values()) {
            try {
                mapping.shutdown();
            } catch (ObjectPersistenceException ex) {
                LOG.error("Shutdown mapper " + mapping, ex);
            }
        }
    }

    @Override
    public void debugData(DebugString debug) {
        debug.appendln("field mapping lookup", fieldMappingLookup);
        debug.appendln("object mapping factory", objectMappingFactory);
        debug.appendTitle("Mappings");
        int i = 1;
        for (ObjectSpecification specification : mappings.keySet()) {
            debug.appendln(i++ + ". " + specification.getShortIdentifier());
           ObjectMapping mapper = mappings.get(specification);
           debug.indent();
           debug.append(mapper);
           debug.unindent();
         }
    }

    @Override
    public String debugTitle() {
        return "Object Mapping Lookup";
    }

}
