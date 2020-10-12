package br.com.taian.bank.api.repository

import br.com.taian.bank.api.model.entities.*
import org.springframework.data.jpa.repository.*
import org.springframework.data.repository.*

interface ClientRepository : CrudRepository<Client, Long> {
    @EntityGraph(attributePaths = ["address", "document"])
    override fun findAll(): Iterable<Client>
    fun findByEmail(email: String): Client?
    fun findByCpf(email: String): Client?
}
