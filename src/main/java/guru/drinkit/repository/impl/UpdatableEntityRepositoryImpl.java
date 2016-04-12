package guru.drinkit.repository.impl;

import java.io.Serializable;

import guru.drinkit.repository.UpdatableEntityRepositoryCustom;

/**
 * @author pkolmykov
 */
public class UpdatableEntityRepositoryImpl<T, ID extends Serializable> implements UpdatableEntityRepositoryCustom<T, ID> {


    @Override
    public void update(T entity) {
        System.out.println("test");
    }

}
