package br.com.taian.bank.api.model

import java.time.*
import javax.persistence.*

@Entity
data class Client(
    @Id @GeneratedValue
    val id: Long = 0L,
    val name: String = "",
    val lastName: String = "",
    @Column(unique = true)
    val email: String = "",
    @Column(unique = true)
    val cnh: String = "",
    val dob: LocalDate = LocalDate.now(),
)
