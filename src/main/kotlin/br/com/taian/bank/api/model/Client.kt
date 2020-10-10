package br.com.taian.bank.api.model

import com.fasterxml.jackson.annotation.*
import java.time.*
import javax.persistence.*

@Entity
@Table(name = "client")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties("document")
data class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L,
    val name: String = "",
    val lastName: String = "",
    @Column(unique = true)
    val email: String = "",
    @Column(unique = true)
    val cpf: String = "",
    val dob: LocalDate = LocalDate.now(),
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    var address: Address? = null,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    var document: PersistedImage? = null,
)
