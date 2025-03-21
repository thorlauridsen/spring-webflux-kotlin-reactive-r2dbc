package com.github.thorlauridsen

import com.github.thorlauridsen.controller.CUSTOMER_BASE_ENDPOINT
import com.github.thorlauridsen.dto.CustomerDto
import com.github.thorlauridsen.dto.CustomerInputDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

/**
 * Test class for testing the CustomerController.
 * The test methods use [WebTestClient] to make requests to the controller endpoints.
 * [WebTestClient] has support for testing reactive web applications (Spring Boot Webflux).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CustomerControllerTest(
    @Autowired private val client: WebTestClient
) {

    @Test
    fun `get customer - random id - returns not found`() {
        val id = UUID.randomUUID()
        client.get()
            .uri("$CUSTOMER_BASE_ENDPOINT/$id")
            .accept(org.springframework.http.MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "alice@gmail.com",
            "bob@gmail.com",
        ]
    )
    fun `post customer - get customer - success`(mail: String) {
        val customer = CustomerInputDto(mail)

        val response = client.post()
            .uri(CUSTOMER_BASE_ENDPOINT)
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .bodyValue(customer)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CustomerDto::class.java)
            .returnResult()
            .responseBody

        assertNotNull(response)
        assertCustomer(response!!, mail)

        val response2 = client.get()
            .uri("$CUSTOMER_BASE_ENDPOINT/${response.id}")
            .accept(org.springframework.http.MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(CustomerDto::class.java)
            .returnResult()
            .responseBody

        assertNotNull(response2)
        assertCustomer(response2!!, mail)
    }

    /**
     * Ensure that customer is not null and that the id is not null.
     * Assert that the mail is equal to the expected mail.
     * @param customer [CustomerDto]
     * @param expectedMail Expected mail of the customer.
     */
    private fun assertCustomer(customer: CustomerDto, expectedMail: String) {
        assertNotNull(customer)
        assertNotNull(customer.id)
        assertEquals(expectedMail, customer.mail)
    }
}
