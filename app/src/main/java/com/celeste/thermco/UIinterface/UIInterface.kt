package com.celeste.thermco.UIinterface

interface UIUpdaterInterface {

    fun resetUIWithConnection(status: Boolean)
    fun updateStatusViewWith(status: String)
    fun update(message: String, topic: String?)
}