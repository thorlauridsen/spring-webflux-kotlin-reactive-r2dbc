package com.github.thorlauridsen

import java.util.UUID
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

/**
 * Coroutine customer CRUD repository.
 * This repository provides a lot of default functions for CRUD operations.
 * We can also easily define custom queries using auto-completion in our IDE.
 */
@Repository
interface CustomerRepo : CoroutineCrudRepository<CustomerEntity, UUID>
