package br.com.taian.bank.api

import br.com.taian.bank.api.constants.*
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
            // Nada por enquanto
        }

    }

    @Test
    @Order(1)
    fun `Assert empty database`() {
        val entity = restTemplate.getForEntity<String>("/api/list")
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
                "cpf": "925.567.530-30",
                "dob": "2000-01-01"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/create", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(entity.body).isEqualTo("""{"id":1}""")
        assertThat(entity.headers.location?.path).isEqualTo("/client/1/addAddress")
    }

    @Test
    @Order(3)
    fun `Assert first client in database`() {
        val entity = restTemplate.getForEntity<String>("/api/client/1")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo("""{"id":1,"name":"teste","lastName":"teste","email":"teste@teste.com","cpf":"925.567.530-30","dob":"2000-01-01"}""")
    }

    @Test
    @Order(4)
    fun `Assert failed duplicate client in database`() {
        val client: String = """
            {
                "name": "teste",
                "lastName": "teste",
                "email": "teste@teste.com",
                "cpf": "925.567.530-30",
                "dob": "2000-01-01"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/create", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).isEqualTo("""{"invalidFields":{"email":"Email already in database","cpf":"CPF already in database"}}""")
    }

    @Test
    @Order(5)
    fun `Assert client not found in database`() {
        val entity = restTemplate.getForEntity<String>("/api/client/2")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(entity.body).isEqualTo(null)
    }

    @Test
    @Order(6)
    fun `Assert invalid client name`() {
        val client: String = """
            {
                "name": "",
                "lastName": "teste",
                "email": "teste@teste.com",
                "cpf": "925.567.530-30",
                "dob": "2000-01-01"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/create", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.EMPTY_NAME)
    }

    @Test
    @Order(7)
    fun `Assert invalid client lastName`() {
        val client: String = """
            {
                "name": "teste",
                "lastName": "",
                "email": "teste@teste.com",
                "cpf": "925.567.530-30",
                "dob": "2000-01-01"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/create", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.EMPTY_LAST_NAME)
    }

    @Test
    @Order(8)
    fun `Assert invalid client email`() {
        val client: String = """
            {
                "name": "teste",
                "lastName": "teste",
                "email": "teste.com",
                "cpf": "925.567.530-30",
                "dob": "2000-01-01"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/create", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.INVALID_EMAIL)
    }

    @Test
    @Order(9)
    fun `Assert invalid client cpf`() {
        val client: String = """
            {
                "name": "teste",
                "lastName": "teste",
                "email": "teste.com",
                "cpf": "11111111111",
                "dob": "2000-01-01"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/create", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.INVALID_CPF)
    }

    @Test
    @Order(10)
    fun `Assert invalid client dob`() {
        val client: String = """
            {
                "name": "teste",
                "lastName": "teste",
                "email": "teste.com",
                "cpf": "11111111111",
                "dob": "2100-01-01"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/create", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.INVALID_DOB)
    }

    @Test
    @Order(11)
    fun `Assert invalid address zip`() {
        val client: String = """
            {
                "zip": "41770395",
                "street": "Rua Correta",
                "area": "Válida",
                "opt": "Ap 999",
                "city": "Salvador",
                "state": "BA"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.INVALID_ZIP)
    }

    @Test
    @Order(12)
    fun `Assert invalid address street`() {
        val client: String = """
            {
                "zip": "41770-395",
                "street": "",
                "area": "Válida",
                "opt": "Ap 999",
                "city": "Salvador",
                "state": "BA"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.EMPTY_STREET)
    }

    @Test
    @Order(13)
    fun `Assert invalid address area`() {
        val client: String = """
            {
                "zip": "41770-395",
                "street": "Rua Correta",
                "area": "",
                "opt": "Ap 999",
                "city": "Salvador",
                "state": "BA"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.EMPTY_AREA)
    }

    @Test
    @Order(14)
    fun `Assert invalid address opt`() {
        val client: String = """
            {
                "zip": "41770-395",
                "street": "Rua Correta",
                "area": "Válida",
                "opt": "",
                "city": "Salvador",
                "state": "BA"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.EMPTY_OPT)
    }

    @Test
    @Order(15)
    fun `Assert invalid address city`() {
        val client: String = """
            {
                "zip": "41770-395",
                "street": "Rua Correta",
                "area": "Válida",
                "opt": "Ap 999",
                "city": "",
                "state": "BA"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.EMPTY_CITY)
    }

    @Test
    @Order(16)
    fun `Assert invalid address state`() {
        val client: String = """
            {
                "zip": "41770-395",
                "street": "Rua Correta",
                "area": "Válida",
                "opt": "Ap 999",
                "city": "Salvador",
                "state": ""
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.EMPTY_STATE)
    }

    @Test
    @Order(17)
    fun `Assert valid address creation in wrong id`() {
        val client: String = """
            {
                "zip": "41770-395",
                "street": "Rua Correta",
                "area": "Válida",
                "opt": "Ap 999",
                "city": "Salvador",
                "state": "BA"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/client/2/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).isEqualTo("""{"invalidFields":{"id":"id not valid"}}""")
    }

    @Test
    @Order(18)
    fun `Assert valid address creation`() {
        val client: String = """
            {
                "zip": "41770-395",
                "street": "Rua Correta",
                "area": "Válida",
                "opt": "Ap 999",
                "city": "Salvador",
                "state": "BA"
            }
        """.trimIndent()
        val request = HttpEntity<String>(client, headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(entity.body).isEqualTo("""{"id":1}""")
        assertThat(entity.headers.location?.path).isEqualTo("/client/1/sendDocs")
    }

    @Test
    @Order(19)
    fun `Assert client with address in database`() {
        val entity = restTemplate.getForEntity<String>("/api/client/1")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("Rua Correta")
    }

}