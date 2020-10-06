package br.com.taian.bank.api.model

data class ValidatedClient(
    val success: Boolean,
    val invalidFields: MutableMap<String, String>,
) {
    constructor(invalidFields: MutableMap<String, String>) : this(invalidFields.isEmpty(), invalidFields)
}
