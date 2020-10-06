package br.com.taian.bank.api.repository

import br.com.taian.bank.api.model.Client
import org.springframework.data.repository.CrudRepository

interface ClientRepository : CrudRepository<Client, Long>
