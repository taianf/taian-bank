package br.com.taian.bank.api.validation

import br.com.taian.bank.api.model.*

internal fun validateAddress(address: Address): ValidatedAddress {
    val invalidFields = mutableMapOf<String, String>()
    // TODO validar endereço
    return ValidatedAddress(invalidFields)
}
