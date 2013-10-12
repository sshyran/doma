/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import org.seasar.doma.internal.jdbc.query.SelectQuery;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * @author nakamura-to
 * 
 */
public class EntityResultProvider<ENTITY, CONTAINER> implements
        ResultProvider<CONTAINER> {

    protected final EntityType<ENTITY> entityType;

    protected final SelectQuery query;

    protected final Function<ENTITY, CONTAINER> mapper;

    protected final EntityBuilder<ENTITY> builder;

    /**
     * @param entityType
     * @param query
     * @param mapper
     */
    public EntityResultProvider(EntityType<ENTITY> entityType,
            SelectQuery query, Function<ENTITY, CONTAINER> mapper) {
        assertNotNull(entityType, query, mapper);
        this.entityType = entityType;
        this.query = query;
        this.mapper = mapper;
        this.builder = new EntityBuilder<ENTITY>(query, entityType,
                query.isResultMappingEnsured());
    }

    @Override
    public CONTAINER get(ResultSet resultSet) throws SQLException {
        EntityBuilder<ENTITY> builder = new EntityBuilder<ENTITY>(query,
                entityType, query.isResultMappingEnsured());
        ENTITY entity = builder.build(resultSet);
        return mapper.apply(entity);
    }

    @Override
    public CONTAINER getDefault() {
        return mapper.apply(null);
    }

}
