package com.github.thorlauridsen.persistence

import com.github.thorlauridsen.model.Customer
import com.github.thorlauridsen.model.CustomerInput
import java.util.UUID
import org.springframework.stereotype.Repository

/**
 * Customer repository facade.
 *
 * This repository is responsible for:
 * - Saving customers to the database.
 * - Fetching customers from the database.
 *
 * This is a facade which allows service classes to easily interact with the
 * database without having to know about any of the database entities.
 * Services can simply input model classes and get model classes back.
 *
 * @property customerRepo [CustomerRepo] to use.
 */
@Repository
class CustomerRepoFacade(private val customerRepo: CustomerRepo) {

    /**
     * Save a customer to the database.
     * @param customer [CustomerInput] to save.
     * @throws IllegalStateException if customer id not found in database after saving.
     * @return [Customer] retrieved from database.
     */
    suspend fun save(customer: CustomerInput): Customer {
        val entity = CustomerEntity(
            mail = customer.mail,
        )
        val created = customerRepo.save(entity)
        return created.toModel()
    }

    /**
     * Get a customer from the database given an id.
     * @param id [UUID] to fetch customer.
     * @return [Customer] or null if not found.
     */
    suspend fun find(id: UUID): Customer? {
        val entity = customerRepo.findById(id)
        return entity?.toModel()
    }
}
