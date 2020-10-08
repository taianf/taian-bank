package br.com.taian.bank.api.model

import com.fasterxml.jackson.annotation.*
import javax.persistence.*

@Entity
@Table(name = "address")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L,
    val cep: String = "",
    val rua: String = "",
    val bairro: String = "",
    val complemento: String = "",
    val cidade: String = "",
    val estado: String = "",
)
