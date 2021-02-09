package com.infinum.maven

import com.infinum.maven.shared.BaseConfiguration

class BintrayConfiguration implements BaseConfiguration {

    private Properties properties = new Properties()

    @Override
    void load() {
        File file = new File("publish.properties")
        if (file.exists()) {
            properties.load(new FileInputStream(file))
        } else {
            properties.setProperty("bintray.name", "")
            properties.setProperty("bintray.url", "")
            properties.setProperty("bintray.user", "")
            properties.setProperty("bintray.apikey", "")
        }
    }

    @Override
    String name() {
        return properties.getProperty("bintray.name").toString()
    }

    @Override
    String url() {
        return properties.getProperty("bintray.url").toString()
    }

    @Override
    String username() {
        return properties.getProperty("bintray.user").toString()
    }

    @Override
    String password() {
        return properties.getProperty("bintray.apikey").toString()
    }
}