package br.com.taian.bank.api.controller

import br.com.taian.bank.api.model.*
import br.com.taian.bank.api.repository.*
import br.com.taian.bank.api.validation.*
import org.springframework.beans.factory.annotation.*
import org.springframework.http.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api")
class APIController {

    @Autowired
    lateinit var clientRepository: ClientRepository

    @GetMapping
    fun list(): List<Client> {
        return clientRepository.findAll().toList()
    }

    @PostMapping
    fun add(@RequestBody client: Client): ResponseEntity<Response> {
        val validClient: ValidatedClient = validateClient(client, clientRepository)
        return if (validClient.success) {
            clientRepository.save(client)
            ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, "location")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response(client.id, null))
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response(null, validClient.invalidFields))
        }
    }

}
