package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

public class DefaultQueryMeta extends AbstractQueryMeta {

  public DefaultQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitDefaultQueryMeta(this);
  }
}
