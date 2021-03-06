package br.com.taian.bank.api.controller

import br.com.taian.bank.api.model.*
import br.com.taian.bank.api.model.entities.*
import br.com.taian.bank.api.repository.*
import br.com.taian.bank.api.validation.*
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.*
import org.springframework.data.repository.*
import org.springframework.http.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api")
class APIController {

    @Autowired
    lateinit var clientRepository: ClientRepository

    @ApiOperation(value = "Lists all clients")
    @GetMapping("/list", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun list(): List<Client> {
        return clientRepository.findAll().toList()
    }

    @ApiOperation(value = "Create a client")
    @PostMapping("/create", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createClient(@RequestBody client: Client): ResponseEntity<Response> {
        val validatedClient = validateClient(client, clientRepository)
        return if (validatedClient.success) {
            clientRepository.save(client)
            ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, "/client/${client.id}/addAddress")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response(answer = "Created client with ${client.id}"))
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response(invalidFields = validatedClient.invalidFields))
        }
    }

    @ApiOperation(value = "Return a client info")
    @GetMapping("/client/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getClient(@PathVariable(value = "id") id: Long): ResponseEntity<Client> {
        val client = clientRepository.findByIdOrNull(id)
        return if (client == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(client)
        } else {
            ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(client)
        }
    }

    @ApiOperation(value = "Add address to a client")
    @PostMapping("/client/{id}/addAddress", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addAddress(
        @PathVariable(value = "id") id: Long,
        @RequestBody address: Address
    ): ResponseEntity<Response> {
        val validatedAddress = validateAddress(address)
        return if (validatedAddress.success) {
            val client = clientRepository.findByIdOrNull(id)
            if (client != null) {
                client.address = address
                clientRepository.save(client)
                ResponseEntity.status(HttpStatus.CREATED)
                    .header(HttpHeaders.LOCATION, "/client/$id/addDocs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Response(answer = "Added address to client with ${client.id}"))
            } else {
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Response(invalidFields = mapOf("id" to "id not valid")))
            }
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response(invalidFields = validatedAddress.invalidFields))
        }
    }

    @ApiOperation(value = "Add document to a client")
    @PostMapping("/client/{id}/addDocs", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addDocs(
        @PathVariable(value = "id") id: Long,
        @RequestBody document: Document
    ): ResponseEntity<Response> {
        val validatedDocument = validateDocument(document)
        val client = clientRepository.findByIdOrNull(id)
        return when {
            !validatedDocument.success -> {
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Response(invalidFields = validatedDocument.invalidFields))

            }
            client == null -> {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Response(invalidFields = mapOf("id" to "id not found")))
            }
            client.address == null -> {
                ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Response(invalidFields = mapOf("address" to "address not set")))
            }
            else -> {
                client.document = document
                clientRepository.save(client)
                ResponseEntity.status(HttpStatus.CREATED)
                    .header(HttpHeaders.LOCATION, "/client/$id/acceptAccount")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Response(answer = "Added document to client with ${client.id}"))
            }
        }
    }

    @ApiOperation(value = "View account proposal")
    @GetMapping("/client/{id}/acceptAccount", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun acceptAccountView(
        @PathVariable(value = "id") id: Long
    ): ResponseEntity<Client> {
        val client = clientRepository.findByIdOrNull(id)
        return when {
            client == null -> {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(client)
            }
            client.address == null || client.document == null -> {
                ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(null)
            }
            else -> {
                ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(client)
            }
        }
    }

    @ApiOperation(value = "Accept account proposal")
    @PostMapping("/client/{id}/acceptAccount", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun acceptAccountConfirm(
        @PathVariable(value = "id") id: Long,
        @RequestBody proposalAcceptation: ProposalAcceptation
    ): ResponseEntity<Response> {
        return when (val client = clientRepository.findByIdOrNull(id)) {
            null -> {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Response(answer = "Client not found"))
            }
            else -> {
                val acceptation = proposalAcceptation.acceptation ?: false
                client.proposalAccepted = acceptation
                clientRepository.save(client)
                if (acceptation) {
                    ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Proposal-Acceptation", "$acceptation")
                        .body(Response(answer = "Email with account creation will be sent soon"))
                } else {
                    ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Proposal-Acceptation", "$acceptation")
                        .body(Response(answer = "Account proposal in stand by"))
                }
            }
        }
    }

}
