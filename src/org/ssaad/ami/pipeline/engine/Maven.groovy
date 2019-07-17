package org.ssaad.ami.pipeline.engine

import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsProvider
import jenkins.model.Jenkins

class Maven extends Engine {

    String settingsFile
    String options = ""
    String goals = ""
    String params = ""

    @Override
    void init(Map config) {

    }

    @Override
    void execute(steps) {
        if (steps != null) {
            if ( credentialsId != null){
                def credentialsList = CredentialsProvider.lookupCredentials(Credentials.class, Jenkins.instance, null, null)
                credentials = credentialsList.find {it.id == credentialsId}

                steps.echo credentials
                steps.echo credentials?.username
            }

            //Resolve vars
            
            steps.sh "mvn ${this.options} ${this.goals} ${this.params}"
        }
    }
}
