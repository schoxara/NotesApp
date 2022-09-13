package com.tkusevic.stadiums.commons.utils

import com.tkusevic.stadiums.commons.constants.EMAIL_REGEX
import java.util.regex.Pattern

fun isValidEmail(input: String?): Boolean {
    return Pattern.matches(EMAIL_REGEX, input)
}

fun checkEmailEmpty(email: String): Boolean {
    return email.isEmpty()
}

fun checkPasswordEmpty(password: String): Boolean {
    return password.isEmpty()
}

fun checkNameEmpty(name: String): Boolean {
    return name.isEmpty()
}

fun isEmpty(vararg strings: String?): Boolean = strings.any { toCheck -> toCheck == null || toCheck.isEmpty() || toCheck.trim { it <= ' ' }.isEmpty() }


