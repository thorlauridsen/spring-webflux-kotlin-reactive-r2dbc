package com.github.thorlauridsen.persistence

import com.github.thorlauridsen.model.Customer
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

/**
 * Customer database entity data class.
 * This represents a customer in the database.
 *
 * The id is annotated with [Id] to indicate that it is the primary key.
 *
 * If id was non-nullable, and we generated a UUID in the code,
 * R2DBC would think it would be updating an existing entry and thus fail for new inserts.
 * This is why the id is nullable, however the toModel function will throw an error if id is null.
 *
 * R2DBC does not support auto-generating primary keys but instead relies on the database to do so.
 * We can use the Liquibase changelogs to set up the database to generate UUIDs for us.
 *
 * @property id [UUID] of customer.
 * @property mail Mail address of customer.
 */
@Table("customer")
data class CustomerEntity(
    @Id
    val id: UUID? = null,
    val mail: String,
) {

    /**
     * Convert this entity to a [Customer].
     * @throws IllegalStateException if id is null.
     * @return [Customer]
     */
    fun toModel(): Customer {
        return Customer(
            id = id ?: error("Customer id is null for mail: $mail"),
            mail = mail
        )
    }
}
