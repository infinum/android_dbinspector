package com.infinum.maven.shared

interface BaseConfiguration {

    void load()

    String name()

    String url()

    String username()

    String password()
}