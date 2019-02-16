package org.seasar.doma.jdbc.query;

import java.util.List;
import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.TenantIdPropertyType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;
import org.seasar.doma.message.Message;

public abstract class AutoModifyQuery<ENTITY> extends AbstractQuery implements ModifyQuery {

  protected static final String[] EMPTY_STRINGS = new String[] {};

  protected String[] includedPropertyNames = EMPTY_STRINGS;

  protected String[] excludedPropertyNames = EMPTY_STRINGS;

  protected final EntityType<ENTITY> entityType;

  protected ENTITY entity;

  protected PreparedSql sql;

  protected List<EntityPropertyType<ENTITY, ?>> targetPropertyTypes;

  protected List<EntityPropertyType<ENTITY, ?>> idPropertyTypes;

  protected VersionPropertyType<ENTITY, ?, ?> versionPropertyType;

  protected TenantIdPropertyType<ENTITY, ?, ?> tenantIdPropertyType;

  protected boolean optimisticLockCheckRequired;

  protected boolean autoGeneratedKeysSupported;

  protected boolean executable;

  protected SqlExecutionSkipCause sqlExecutionSkipCause = SqlExecutionSkipCause.STATE_UNCHANGED;

  protected SqlLogType sqlLogType;

  protected AutoModifyQuery(EntityType<ENTITY> entityType) {
    AssertionUtil.assertNotNull(entityType);
    this.entityType = entityType;
  }

  protected void prepareSpecialPropertyTypes() {
    idPropertyTypes = entityType.getIdPropertyTypes();
    versionPropertyType = entityType.getVersionPropertyType();
    tenantIdPropertyType = entityType.getTenantIdPropertyType();
  }

  protected void validateIdExistent() {
    if (idPropertyTypes.isEmpty()) {
      throw new JdbcException(Message.DOMA2022, entityType.getName());
    }
  }

  protected void prepareOptions() {
    if (queryTimeout <= 0) {
      queryTimeout = config.getQueryTimeout();
    }
  }

  protected boolean isTargetPropertyName(String name) {
    if (includedPropertyNames.length > 0) {
      for (String includedName : includedPropertyNames) {
        if (includedName.equals(name)) {
          for (String excludedName : excludedPropertyNames) {
            if (excludedName.equals(name)) {
              return false;
            }
          }
          return true;
        }
      }
      return false;
    }
    if (excludedPropertyNames.length > 0) {
      for (String excludedName : excludedPropertyNames) {
        if (excludedName.equals(name)) {
          return false;
        }
      }
      return true;
    }
    return true;
  }

  public void setEntity(ENTITY entity) {
    this.entity = entity;
  }

  public ENTITY getEntity() {
    return entity;
  }

  public void setIncludedPropertyNames(String... includedPropertyNames) {
    this.includedPropertyNames = includedPropertyNames;
  }

  public void setExcludedPropertyNames(String... excludedPropertyNames) {
    this.excludedPropertyNames = excludedPropertyNames;
  }

  public void setSqlLogType(SqlLogType sqlLogType) {
    this.sqlLogType = sqlLogType;
  }

  @Override
  public PreparedSql getSql() {
    return sql;
  }

  @Override
  public boolean isOptimisticLockCheckRequired() {
    return optimisticLockCheckRequired;
  }

  @Override
  public boolean isExecutable() {
    return executable;
  }

  @Override
  public SqlExecutionSkipCause getSqlExecutionSkipCause() {
    return sqlExecutionSkipCause;
  }

  @Override
  public boolean isAutoGeneratedKeysSupported() {
    return autoGeneratedKeysSupported;
  }

  @Override
  public SqlLogType getSqlLogType() {
    return sqlLogType;
  }

  @Override
  public String toString() {
    return sql != null ? sql.toString() : null;
  }
}
