package br.com.taian.bank.api.model

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Response(
    var id: Long? = null,
    var invalidFields: Map<String, String> = mapOf(),
    var answer: String = "",
)
