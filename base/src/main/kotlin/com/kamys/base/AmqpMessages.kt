package com.kamys.base

import java.io.Serializable

class MessageProjectEditName(
    val email: String,
    val newName: String,
    val oldName: String
) : Serializable