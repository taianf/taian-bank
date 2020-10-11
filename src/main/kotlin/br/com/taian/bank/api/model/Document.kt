package br.com.taian.bank.api.model

import com.fasterxml.jackson.annotation.*
import javax.persistence.*

@Entity
@Table(name = "address")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties("id")
data class Document(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L,
    val url: String = "",
)
