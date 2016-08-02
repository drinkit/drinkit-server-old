package guru.drinkit.service.impl

import guru.drinkit.domain.Entity
import guru.drinkit.service.CrudService
import org.springframework.data.mongodb.repository.MongoRepository
import java.io.Serializable

/**
 * @author pkolmykov
 */
abstract class CrudServiceImpl<ID : Serializable, T : Entity<ID>>(val mongoRepository: MongoRepository<T, ID>) : CrudService<ID, T> {
    override fun find(id: ID): T? = mongoRepository.findOne(id)
    override fun save(entity: T): T = mongoRepository.save(entity)
    override fun delete(entity: T) = mongoRepository.delete(entity)
    override fun findAll() = mongoRepository.findAll()
}
