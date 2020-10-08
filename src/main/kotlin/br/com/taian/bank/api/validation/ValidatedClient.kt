package br.com.taian.bank.api.validation

data class ValidatedClient(
    val success: Boolean,
    val invalidFields: MutableMap<String, String>,
) {
    constructor(invalidFields: MutableMap<String, String>) : this(invalidFields.isEmpty(), invalidFields)
}
