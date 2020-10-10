package br.com.taian.bank.api.validation

import br.com.taian.bank.api.constants.*
import br.com.taian.bank.api.model.*

internal fun validateAddress(address: Address): ValidatedAddress {
    val invalidFields = mutableMapOf<String, String>()
    validateZip(address, invalidFields)
    validateStreet(address, invalidFields)
    validateArea(address, invalidFields)
    validateOpt(address, invalidFields)
    validateCity(address, invalidFields)
    validateState(address, invalidFields)
    return ValidatedAddress(invalidFields)
}

// Validando apenas CEPs brasileiros.
fun validateZip(address: Address, invalidFields: MutableMap<String, String>) {
    val brazilZipCodeRegex = "^\\d{5}[-]\\d{3}$".toRegex()
    if (!address.zip.matches(brazilZipCodeRegex)) {
        invalidFields["zip"] = ValidationConstants.INVALID_ZIP
    }
}

fun validateStreet(address: Address, invalidFields: MutableMap<String, String>) {
    if (address.street.isBlank()) {
        invalidFields["street"] = ValidationConstants.EMPTY_STREET
    }
}

fun validateArea(address: Address, invalidFields: MutableMap<String, String>) {
    if (address.area.isBlank()) {
        invalidFields["area"] = ValidationConstants.EMPTY_AREA
    }
}

fun validateOpt(address: Address, invalidFields: MutableMap<String, String>) {
    if (address.opt.isBlank()) {
        invalidFields["opt"] = ValidationConstants.EMPTY_OPT
    }
}

fun validateCity(address: Address, invalidFields: MutableMap<String, String>) {
    if (address.city.isBlank()) {
        invalidFields["city"] = ValidationConstants.EMPTY_CITY
    }
}

fun validateState(address: Address, invalidFields: MutableMap<String, String>) {
    if (address.state.isBlank()) {
        invalidFields["state"] = ValidationConstants.EMPTY_STATE
    }
}
