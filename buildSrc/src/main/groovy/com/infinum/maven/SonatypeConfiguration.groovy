package com.infinum.maven

import com.infinum.maven.shared.BaseConfiguration

class SonatypeConfiguration implements BaseConfiguration {

    private Properties properties = new Properties()

    @Override
    void load() {
        File file = new File("publish.properties")
        if (file.exists()) {
            properties.load(new FileInputStream(file))
        } else {
            properties.setProperty("sonatype.name", "")
            properties.setProperty("sonatype.url", "")
            properties.setProperty("sonatype.user", "")
            properties.setProperty("sonatype.password", "")
        }
    }

    @Override
    String name() {
        return properties.getProperty("sonatype.name").toString()
    }

    @Override
    String url() {
        return properties.getProperty("sonatype.url").toString()
    }

    @Override
    String username() {
        return properties.getProperty("sonatype.user").toString()
    }

    @Override
    String password() {
        return properties.getProperty("sonatype.password").toString()
    }
}