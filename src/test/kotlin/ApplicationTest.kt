package br.com.taian.bank.api

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.context.*
import org.springframework.boot.test.web.client.*
import org.springframework.http.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ApplicationTest(@Autowired val restTemplate: TestRestTemplate) {

    companion object {
        val headers = HttpHeaders()

        @BeforeAll
        @JvmStatic
        fun setupTests() {
            headers.contentType = MediaType.APPLICATION_JSON
        }

        @AfterAll
        @JvmStatic
        fun cleanTests() {
//            headers.contentType = MediaType.APPLICATION_JSON
        }

    }

    @Test
    @Order(1)
    fun `Assert empty database`() {
        val entity = restTemplate.getForEntity<String>("/api")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo("[]")
    }

    @Test
    @Order(2)
    fun `Assert client creation`() {
        val client: String = """
            {
                "name": "teste",
                "lastName": "teste",
                "email": "teste@teste.com",
                "cnh": "07021871297",
                "dob": "2000-01-01"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(entity.body).isEqualTo("""{"id":1}""")
    }

    @Test
    @Order(3)
    fun `Assert first client in database`() {
        val entity = restTemplate.getForEntity<String>("/api")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo("""[{"id":1,"name":"teste","lastName":"teste","email":"teste@teste.com","cnh":"07021871297","dob":"2000-01-01"}]""")
    }

    @Test
    @Order(4)
    fun `Assert failed duplicate client in database`() {
        val client: String = """
            {
                "name": "teste",
                "lastName": "teste",
                "email": "teste@teste.com",
                "cnh": "07021871297",
                "dob": "2000-01-01"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).isEqualTo("""{"invalidFields":{"email":"Email already in database","cnh":"CNH already in database"}}""")
    }

}