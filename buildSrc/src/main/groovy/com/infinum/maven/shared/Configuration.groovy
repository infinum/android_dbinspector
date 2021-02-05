package com.infinum.maven.shared

interface Configuration {

    void load()

    String name()

    String url()

    String username()

    String password()
}