package br.com.taian.bank.api.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Client(@Id
             @GeneratedValue
             val id: Long = 0L,
             val name: String = "",
             val lastName: String = "",
             val email: String = "",
             val cnh: String = "",
             val dob: String = "")
