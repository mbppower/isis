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

package org.apache.isis.viewer.wicket.ui.components.actions;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import org.apache.isis.viewer.wicket.model.models.ActionModel;
import org.apache.isis.viewer.wicket.ui.ComponentFactory;
import org.apache.isis.viewer.wicket.ui.ComponentFactoryAbstract;
import org.apache.isis.viewer.wicket.ui.ComponentType;

/**
 * {@link ComponentFactory} for {@link ActionParametersFormPanel}.
 */
public class ActionParametersFormPanelFactory extends ComponentFactoryAbstract {

    private static final long serialVersionUID = 1L;

    public ActionParametersFormPanelFactory() {
        super(ComponentType.PARAMETERS);
    }

    @Override
    public ApplicationAdvice appliesTo(final IModel<?> model) {
        return appliesIf(model instanceof ActionModel);
    }

    @Override
    public Component createComponent(final String id, final IModel<?> model) {
        final ActionModel actionModel = (ActionModel) model;
        return new ActionParametersFormPanel(id, actionModel);
    }

}