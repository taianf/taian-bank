package br.com.taian.bank.api.model.entities

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.*
import com.fasterxml.jackson.datatype.jsr310.deser.*
import com.fasterxml.jackson.datatype.jsr310.ser.*
import org.hibernate.annotations.*
import java.time.*
import javax.persistence.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "client")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val dob: LocalDate = LocalDate.now(),
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "address_id")
    var address: Address? = null,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "document_id")
    var document: Document? = null,
    var proposalAccepted: Boolean? = null,
    var proposalConfirmed: Boolean? = null,
)
