package br.com.taian.bank.api.controller

import br.com.taian.bank.api.model.Client
import br.com.taian.bank.api.repository.ClientRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api")
class NoteController {

    @Autowired
    lateinit var clientRepository: ClientRepository

    @GetMapping
    fun list(): List<Client> {
        return clientRepository.findAll().toList()
    }

    @PostMapping
    fun add(@RequestBody client: Client): ResponseEntity<Client> {
        val save = clientRepository.save(client)
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.TEXT_PLAIN)
                .body(save)
    }

}
