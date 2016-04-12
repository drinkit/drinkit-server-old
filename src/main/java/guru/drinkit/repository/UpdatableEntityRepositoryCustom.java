package guru.drinkit.repository;

import java.io.Serializable;

/**
 * @author pkolmykov
 */
public interface UpdatableEntityRepositoryCustom<T, ID extends Serializable> {

    void update(T entity);
}
