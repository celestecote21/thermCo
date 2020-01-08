package com.celeste.thermco.UIinterface

interface UIUpdaterInterface {
    fun mqttError(error: String, complete: Boolean)
    fun update(message: String, topic: String?)
}