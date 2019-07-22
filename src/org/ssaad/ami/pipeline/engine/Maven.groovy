package org.ssaad.ami.pipeline.engine

import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsProvider
import jenkins.model.Jenkins
import org.ssaad.ami.pipeline.common.Pipeline

class Maven extends Engine {

    String settingsFile
    String options = ""
    String goals = ""
    String params = ""

    @Override
    void customize(Map config) {

    }

    @Override
    void execute(steps, Pipeline myPipeline) {
        if (steps != null) {
            if ( credentialsId != null){
                def credentialsList = CredentialsProvider.lookupCredentials(Credentials.class, Jenkins.instance, null, null)
                credentials = credentialsList.find {it.id == credentialsId}

                steps.echo credentials?.toString()
                steps.echo credentials?.username
            }

            //Resolve vars
            
            steps.sh "mvn ${this.options} ${this.goals} ${this.params}"
        }
    }
}
