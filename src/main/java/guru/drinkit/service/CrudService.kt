package guru.drinkit.service

import guru.drinkit.domain.Entity
import guru.drinkit.exception.RecordNotFoundException
import org.springframework.security.access.prepost.PreAuthorize

/**
 * @author pkolmykov
 */
interface CrudService<ID, T : Entity<ID>> {

    fun find(id: ID): T?

    /**
     * @throws RecordNotFoundException if recipe not exist
     */
    fun findSafe(id: ID): T = find(id) ?: throw RecordNotFoundException("")

    fun insert(entity: T): T = if (entity.id == null) save(entity) else throw IllegalArgumentException("")

    fun update(id: Any, entity: T): T = if (id == entity.id) save(entity) else throw IllegalArgumentException()

    @PreAuthorize("isAuthenticated()")
    fun save(entity: T): T

    /**
     * @throws RecordNotFoundException if recipe not exist
     */
    fun delete(id: ID) = delete(findSafe(id))

    fun delete(entity: T)

}