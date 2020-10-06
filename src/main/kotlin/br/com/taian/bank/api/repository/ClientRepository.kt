package br.com.taian.bank.api.repository

import br.com.taian.bank.api.model.*
import org.springframework.data.repository.*

interface ClientRepository : CrudRepository<Client, Long> {
    fun findByEmail(email: String): Client?
    fun findByCnh(email: String): Client?
}
