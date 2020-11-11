package com.infinum.dbinspector.sample.data

interface DatabaseProvider {

    fun names(): List<String>

    fun copy()
}