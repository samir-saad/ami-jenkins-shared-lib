package org.ssaad.ami.pipeline.utils

import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsProvider
import jenkins.model.Jenkins
import org.ssaad.ami.pipeline.common.ScmRepository
import org.ssaad.ami.pipeline.common.types.ScmType

class JenkinsUtils {

    static Credentials getCredentials(String credentialsId) {
        def credentialsList = CredentialsProvider.lookupCredentials(Credentials.class, Jenkins.instance, null, null)
        Credentials credentials = credentialsList.find { it.id == credentialsId }

        return credentials
    }

    static void cloneScmRepo(ScmRepository repo, steps) {
        if (ScmType.GIT.equals(repo.scmType))
            cloneGitRepo(repo, steps)
    }

    static void cloneGitRepo(ScmRepository repo, steps) {

        // Checkout
        steps.checkout([$class                           : 'GitSCM',
                        branches                         : [[name: "*/" + repo.branch]],
                        doGenerateSubmoduleConfigurations: false,
                        extensions                       : [[$class: 'RelativeTargetDirectory', relativeTargetDir: repo.localDir]],
                        submoduleCfg                     : [],
                        userRemoteConfigs                : [[credentialsId: repo.credentialsId, url: repo.url]]
        ])

        // Get commit id
        steps.dir(repo.localDir) {
            repo.latestCommit = steps.sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
        }
    }
}
