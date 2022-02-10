package com.kamys.base

import java.io.Serializable

class MessageProjectEditName(
    val email: String,
    val newName: String,
    val oldName: String
) : Serializable {
    override fun toString(): String {
        return "MessageProjectEditName(email='$email', newName='$newName', oldName='$oldName')"
    }
}