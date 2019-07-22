package org.ssaad.ami.pipeline.utils

import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsProvider
import jenkins.model.Jenkins

class JenkinsUtils {

    static Credentials getCredentials(String credentialsId) {
        def credentialsList = CredentialsProvider.lookupCredentials(Credentials.class, Jenkins.instance, null, null)
        Credentials credentials = credentialsList.find { it.id == credentialsId }

        return credentials
    }
}
