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

    @GetMapping("/list")
    fun list(): List<Client> {
        return clientRepository.findAll().toList()
    }

    @PostMapping("/create")
    fun createClient(@RequestBody client: Client): ResponseEntity<Response> {
        val validatedClient = validateClient(client, clientRepository)
        return if (validatedClient.success) {
            clientRepository.save(client)
            ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, "location")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response(id = client.id))
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response(invalidFields = validatedClient.invalidFields))
        }
    }

    @PostMapping("/addAddress")
    fun addAddress(@RequestBody address: Address): ResponseEntity<Response> {
        val validatedAddress = validateAddress(address)
        val response: ResponseEntity<Response>
        if (validatedAddress.success) {
            val optionalClient = clientRepository.findById(address.id)
            val client = optionalClient.orElse(null)
            if (client != null) {
                client.address = address
                clientRepository.save(client)
                response = ResponseEntity.status(HttpStatus.CREATED)
                    .header(HttpHeaders.LOCATION, "location")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Response(id = client.id))

            } else {
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Response(invalidFields = mapOf("id" to "id not valid")))
            }
        } else {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response(invalidFields = validatedAddress.invalidFields))
        }
        return response
    }


}
