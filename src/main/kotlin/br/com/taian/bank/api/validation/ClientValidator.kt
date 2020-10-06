package br.com.taian.bank.api.validation

import br.com.taian.bank.api.model.*
import br.com.taian.bank.api.repository.*
import org.apache.commons.validator.routines.*
import java.time.*

internal fun validateClient(client: Client, clientRepository: ClientRepository): ValidatedClient {
    val invalidFields = mutableMapOf<String, String>()
    validateClientName(client, invalidFields)
    validateClientLastName(client, invalidFields)
    validateClientEmail(client, clientRepository, invalidFields)
    validateClientCNH(client, clientRepository, invalidFields)
    validateClientDOB(client, invalidFields)
    return ValidatedClient(invalidFields)
}

private fun validateClientName(
    client: Client,
    invalidFields: MutableMap<String, String>,
) {
    if (client.name.isBlank()) {
        invalidFields["name"] = "Empty name"
    }
}

private fun validateClientLastName(
    client: Client,
    invalidFields: MutableMap<String, String>,
) {
    if (client.lastName.isBlank()) {
        invalidFields["lastName"] = "Empty last name"
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
        invalidFields["email"] = "Invalid email"
    } else {
        val clientByEmail: Client? = clientRepository.findByEmail(client.email)
        if (clientByEmail != null) {
            invalidFields["email"] = "Email already in database"
        }
    }
}

private fun validateClientCNH(
    client: Client,
    clientRepository: ClientRepository,
    invalidFields: MutableMap<String, String>,
) {
    if (!validateCNH(client.cnh)) {
        invalidFields["cnh"] = "Invalid CNH"
    } else {
        val clientByCnh: Client? = clientRepository.findByCnh(client.cnh)
        if (clientByCnh != null) {
            invalidFields["cnh"] = "CNH already in database"
        }
    }
}

private fun validateClientDOB(
    client: Client,
    invalidFields: MutableMap<String, String>,
) {
    if (client.dob.isAfter(LocalDate.now().minusYears(18))) {
        invalidFields["dob"] = "Invalid Date of Birth"
    }
}
