package br.com.taian.bank.api.model.entities

import com.fasterxml.jackson.annotation.*
import javax.persistence.*

@Entity
@Table(name = "document")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties("id", "client")
data class Document(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    @OneToOne(mappedBy = "document")
    val client: Client? = null,
    val url: String = "",
)
