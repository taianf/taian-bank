package br.com.taian.bank.api

import br.com.taian.bank.api.constants.*
import br.com.taian.bank.api.model.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.*
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.context.*
import org.springframework.boot.test.web.client.*
import org.springframework.http.*
import java.time.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ApplicationTest(@Autowired val restTemplate: TestRestTemplate) {

    companion object {
        val headers = HttpHeaders()
        val mapper = ObjectMapper()

        @BeforeAll
        @JvmStatic
        fun setupTests() {
            headers.contentType = MediaType.APPLICATION_JSON
            mapper.registerModule(JavaTimeModule())
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
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
        val client = Client(
            1,
            "teste",
            "teste",
            "teste@teste.com",
            "925.567.530-30",
            LocalDate.parse("2000-01-01"),
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(client), headers)
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
        val client = Client(
            1,
            "teste",
            "teste",
            "teste@teste.com",
            "925.567.530-30",
            LocalDate.parse("2000-01-01"),
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(client), headers)
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
        val client = Client(
            1,
            "",
            "teste",
            "teste@teste.com",
            "925.567.530-30",
            LocalDate.parse("2000-01-01"),
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(client), headers)
        val entity = restTemplate.postForEntity<String>("/api/create", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.EMPTY_NAME)
    }

    @Test
    @Order(7)
    fun `Assert invalid client lastName`() {
        val client = Client(
            1,
            "teste",
            "",
            "teste@teste.com",
            "925.567.530-30",
            LocalDate.parse("2000-01-01"),
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(client), headers)
        val entity = restTemplate.postForEntity<String>("/api/create", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.EMPTY_LAST_NAME)
    }

    @Test
    @Order(8)
    fun `Assert invalid client email`() {
        val client = Client(
            1,
            "teste",
            "teste",
            "teste.com",
            "925.567.530-30",
            LocalDate.parse("2000-01-01"),
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(client), headers)
        val entity = restTemplate.postForEntity<String>("/api/create", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.INVALID_EMAIL)
    }

    @Test
    @Order(9)
    fun `Assert invalid client cpf`() {
        val client = Client(
            1,
            "teste",
            "teste",
            "teste@teste.com",
            "11111111111",
            LocalDate.parse("2000-01-01"),
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(client), headers)
        val entity = restTemplate.postForEntity<String>("/api/create", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.INVALID_CPF)
    }

    @Test
    @Order(10)
    fun `Assert invalid client dob`() {
        val client = Client(
            1,
            "teste",
            "teste",
            "teste@teste.com",
            "925.567.530-30",
            LocalDate.parse("2100-01-01"),
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(client), headers)
        val entity = restTemplate.postForEntity<String>("/api/create", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.INVALID_DOB)
    }

    @Test
    @Order(11)
    fun `Assert invalid address zip`() {
        val address = Address(
            2,
            "41770395",
            "Rua Correta",
            "Válida",
            "Ap 999",
            "Salvador",
            "BA"
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(address), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.INVALID_ZIP)
    }

    @Test
    @Order(12)
    fun `Assert invalid address street`() {
        val address = Address(
            2,
            "41770-395",
            "",
            "Válida",
            "Ap 999",
            "Salvador",
            "BA"
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(address), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.EMPTY_STREET)
    }

    @Test
    @Order(13)
    fun `Assert invalid address area`() {
        val address = Address(
            2,
            "41770-395",
            "Rua Correta",
            "",
            "Ap 999",
            "Salvador",
            "BA"
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(address), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.EMPTY_AREA)
    }

    @Test
    @Order(14)
    fun `Assert invalid address opt`() {
        val address = Address(
            2,
            "41770-395",
            "Rua Correta",
            "Válida",
            "",
            "Salvador",
            "BA"
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(address), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.EMPTY_OPT)
    }

    @Test
    @Order(15)
    fun `Assert invalid address city`() {
        val address = Address(
            2,
            "41770-395",
            "Rua Correta",
            "Válida",
            "Ap 999",
            "",
            "BA"
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(address), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.EMPTY_CITY)
    }

    @Test
    @Order(16)
    fun `Assert invalid address state`() {
        val address = Address(
            2,
            "41770-395",
            "Rua Correta",
            "Válida",
            "Ap 999",
            "Salvador",
            ""
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(address), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).contains(ValidationConstants.EMPTY_STATE)
    }

    @Test
    @Order(17)
    fun `Assert valid address creation in wrong id`() {
        val address = Address(
            2,
            "41770-395",
            "Rua Correta",
            "Válida",
            "Ap 999",
            "Salvador",
            "BA"
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(address), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/2/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).isEqualTo("""{"invalidFields":{"id":"id not valid"}}""")
    }

    @Test
    @Order(18)
    fun `Assert document without address`() {
        val document = Document(
            3,
            "https://img.r7.com/images/novo-rg-sp-19082019164048588"
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(document), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addDocs", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
        assertThat(entity.body).isEqualTo("""{"invalidFields":{"address":"address not set"}}""")
    }

    @Test
    @Order(19)
    fun `Assert valid address creation`() {
        val address = Address(
            2,
            "41770-395",
            "Rua Correta",
            "Válida",
            "Ap 999",
            "Salvador",
            "BA"
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(address), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addAddress", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(entity.body).isEqualTo("""{"id":1}""")
        assertThat(entity.headers.location?.path).isEqualTo("/client/1/addDocs")
    }

    @Test
    @Order(20)
    fun `Assert client with address in database`() {
        val entity = restTemplate.getForEntity<String>("/api/client/1")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("Rua Correta")
    }

    @Test
    @Order(21)
    fun `Assert invalid document add`() {
        val document = Document(
            3,
            "http://www.teste.com"
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(document), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addDocs", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(entity.body).isEqualTo("""{"invalidFields":{"url":"Invalid URL"}}""")
    }

    @Test
    @Order(22)
    fun `Assert id not found`() {
        val document = Document(
            3,
            "https://img.r7.com/images/novo-rg-sp-19082019164048588"
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(document), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/-1/addDocs", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(entity.body).isEqualTo("""{"invalidFields":{"id":"id not found"}}""")
    }

    @Test
    @Order(23)
    fun `Assert accept account unavaible`() {
        val entity = restTemplate.getForEntity<String>("/api/client/1/acceptAccount")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
        assertThat(entity.body).isEqualTo(null)
    }

    @Test
    @Order(24)
    fun `Assert accept account not found`() {
        val entity = restTemplate.getForEntity<String>("/api/client/-1/acceptAccount")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(entity.body).isEqualTo(null)
    }

    @Test
    @Order(25)
    fun `Assert valid document add`() {
        val document = Document(
            3,
            "https://img.r7.com/images/novo-rg-sp-19082019164048588"
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(document), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/addDocs", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(entity.body).isEqualTo("""{"id":1}""")
        assertThat(entity.headers.location?.path).isEqualTo("/client/1/acceptAccount")
    }

    @Test
    @Order(26)
    fun `Assert valid get accept account`() {
        val entity = restTemplate.getForEntity<String>("/api/client/1/acceptAccount")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo("""{"id":1,"name":"teste","lastName":"teste","email":"teste@teste.com","cpf":"925.567.530-30","dob":"2000-01-01","address":{"zip":"41770-395","street":"Rua Correta","area":"Válida","opt":"Ap 999","city":"Salvador","state":"BA"},"document":{"url":"https://img.r7.com/images/novo-rg-sp-19082019164048588"}}""")
    }

    @Test
    @Order(27)
    fun `Assert not found proposal`() {
        val proposalAcceptation = ProposalAcceptation(
            1,
            true
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(proposalAcceptation), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/-1/acceptAccount", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(entity.body).isEqualTo("""{"answer":"Client not found"}""")
    }

    @Test
    @Order(28)
    fun `Assert positive proposal`() {
        val proposalAcceptation = ProposalAcceptation(
            1,
            true
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(proposalAcceptation), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/acceptAccount", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo("""{"answer":"Email with account creation will be sent soon"}""")
    }

    @Test
    @Order(29)
    fun `Assert negative proposal`() {
        val proposalAcceptation = ProposalAcceptation(
            1,
            false
        )
        val request = HttpEntity<String>(mapper.writeValueAsString(proposalAcceptation), headers)
        val entity = restTemplate.postForEntity<String>("/api/client/1/acceptAccount", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo("""{"answer":"Account proposal in stand by"}""")
    }

}