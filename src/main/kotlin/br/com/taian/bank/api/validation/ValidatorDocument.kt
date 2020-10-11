package br.com.taian.bank.api.validation

import br.com.taian.bank.api.constants.*
import br.com.taian.bank.api.model.*
import com.github.kittinunf.fuel.*
import com.github.kittinunf.result.*

internal fun validateDocument(document: Document): ValidatedDocument {
    val invalidFields = mutableMapOf<String, String>()
    validateUrl(document, invalidFields)
    return ValidatedDocument(invalidFields)
}

fun validateUrl(document: Document, invalidFields: MutableMap<String, String>) {
    val (_, response, result) = document.url.httpHead().response()
    when (result) {
        is Result.Failure -> {
            invalidFields["url"] = ValidationConstants.INVALID_URL
        }
        is Result.Success -> {
            val contentType = response.headers["Content-Type"].firstOrNull()
            val contains = contentType?.contains("image") ?: false
            if (!contains) {
                invalidFields["url"] = ValidationConstants.INVALID_URL
            }
        }
    }
}
