package br.com.taian.bank.api.validation

import br.com.taian.bank.api.constants.*
import br.com.taian.bank.api.model.*
import br.com.taian.bank.api.repository.*
import org.apache.commons.validator.routines.*
import java.time.*

internal fun validateClient(client: Client, clientRepository: ClientRepository): ValidatedClient {
    val invalidFields = mutableMapOf<String, String>()
    validateClientName(client, invalidFields)
    validateClientLastName(client, invalidFields)
    validateClientEmail(client, clientRepository, invalidFields)
    validateClientCpf(client, clientRepository, invalidFields)
    validateClientDOB(client, invalidFields)
    return ValidatedClient(invalidFields)
}

private fun validateClientName(
    client: Client,
    invalidFields: MutableMap<String, String>,
) {
    if (client.name.isBlank()) {
        invalidFields["name"] = ValidationConstants.EMPTY_NAME
    }
}

private fun validateClientLastName(
    client: Client,
    invalidFields: MutableMap<String, String>,
) {
    if (client.lastName.isBlank()) {
        invalidFields["lastName"] = ValidationConstants.EMPTY_LAST_NAME
    }
}

private fun validateClientEmail(
    client: Client,
    clientRepository: ClientRepository,
    invalidFields: MutableMap<String, String>,
) {
    val emailValidator = EmailValidator.getInstance()
    val valid = emailValidator.isValid(client.email)
    if (!valid) {
        invalidFields["email"] = ValidationConstants.INVALID_EMAIL
    } else {
        val clientByEmail: Client? = clientRepository.findByEmail(client.email)
        if (clientByEmail != null) {
            invalidFields["email"] = ValidationConstants.USED_EMAIL
        }
    }
}

private fun validateClientCpf(
    client: Client,
    clientRepository: ClientRepository,
    invalidFields: MutableMap<String, String>,
) {
    if (!ValidaCPF.isCPF(client.cpf)) {
        invalidFields["cpf"] = ValidationConstants.INVALID_CPF
    } else {
        val clientByCpf: Client? = clientRepository.findByCpf(client.cpf)
        if (clientByCpf != null) {
            invalidFields["cpf"] = ValidationConstants.USED_CPF
        }
    }
}

private fun validateClientDOB(
    client: Client,
    invalidFields: MutableMap<String, String>,
) {
    if (client.dob.isAfter(LocalDate.now().minusYears(18))) {
        invalidFields["dob"] = ValidationConstants.INVALID_DOB
    }
}
