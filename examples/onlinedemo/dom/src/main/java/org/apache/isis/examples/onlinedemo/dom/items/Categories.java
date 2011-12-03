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

package org.apache.isis.examples.onlinedemo.dom.items;

import java.util.List;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.QueryOnly;

/**
 * A repository for {@link Category}.
 * 
 * <p>
 * The implementation depends on the configured object store.
 */
@Hidden
public interface Categories {

    @QueryOnly // no side-effects
    @MemberOrder(sequence="1")
    public List<Category> all();

    @QueryOnly
    @MemberOrder(sequence="2")
    public Category find(
        @Named("Description") String description);

    @Hidden // intended for fixtures only, so is hidden in the UI
    public Category newCategory(String description);

}