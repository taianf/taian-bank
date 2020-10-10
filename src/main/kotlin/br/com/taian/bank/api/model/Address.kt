package br.com.taian.bank.api.model

import com.fasterxml.jackson.annotation.*
import javax.persistence.*

@Entity
@Table(name = "address")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties("id")
data class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L,
    val zip: String = "",
    val street: String = "",
    val area: String = "",
    val opt: String = "",
    val city: String = "",
    val state: String = "",
)
