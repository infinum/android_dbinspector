package com.infinum.dbinspector.server

public interface Server {

    public fun start(port: Int): Boolean

    public fun stop(): Boolean
}
