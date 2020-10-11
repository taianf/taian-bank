package br.com.taian.bank.api.validation

data class ValidatedDocument(
    val success: Boolean,
    val invalidFields: Map<String, String>,
) {
    constructor(invalidFields: MutableMap<String, String>) : this(invalidFields.isEmpty(), invalidFields)
}
