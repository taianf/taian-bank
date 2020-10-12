package br.com.taian.bank.api.model.entities

import com.fasterxml.jackson.annotation.*
import javax.persistence.*

@Entity
@Table(name = "address")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties("id", "client")
data class Address(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    @OneToOne(mappedBy = "address")
    val client: Client? = null,
    val zip: String = "",
    val street: String = "",
    val area: String = "",
    val opt: String = "",
    val city: String = "",
    val state: String = "",
)
