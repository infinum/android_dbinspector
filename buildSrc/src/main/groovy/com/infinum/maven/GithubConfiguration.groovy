package com.infinum.maven

import com.infinum.maven.shared.BaseConfiguration

class GithubConfiguration implements BaseConfiguration {

    private Properties properties = new Properties()

    @Override
    void load() {
        File file = new File("publish.properties")
        if (file.exists()) {
            properties.load(new FileInputStream(file))
        } else {
            properties.setProperty("github.name", "")
            properties.setProperty("github.url", "")
            properties.setProperty("github.user", "")
            properties.setProperty("github.token", "")
        }
    }

    @Override
    String name() {
        return properties.getProperty("github.name").toString()
    }

    @Override
    String url() {
        return properties.getProperty("github.url").toString()
    }

    @Override
    String username() {
        return properties.getProperty("github.user").toString()
    }

    @Override
    String password() {
        return properties.getProperty("github.token").toString()
    }
}