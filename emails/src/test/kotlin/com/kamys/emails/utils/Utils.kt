package com.kamys.emails.utils

fun getRandomString(length: Int = 50) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun getRandomEmail(length: Int = 20) : String {
    return getRandomString(length) + "@mail.com"
}