package br.com.taian.bank.api.constants

// Maybe move these constants to a conf file or a database

object ValidationConstants {

    const val EMPTY_NAME = "Empty name"
    const val EMPTY_LAST_NAME = "Empty last name"
    const val INVALID_EMAIL = "Invalid email"
    const val USED_EMAIL = "Email already in database"
    const val INVALID_CPF = "Invalid CPF"
    const val USED_CPF = "CPF already in database"
    const val INVALID_DOB = "Invalid Date of Birth"

    const val INVALID_ZIP = "Invalid ZIP"
    const val EMPTY_STREET = "Empty street"
    const val EMPTY_AREA = "Empty area"
    const val EMPTY_OPT = "Empty optional"
    const val EMPTY_CITY = "Empty city"
    const val EMPTY_STATE = "Empty state"

    const val INVALID_URL = "Invalid URL"

}