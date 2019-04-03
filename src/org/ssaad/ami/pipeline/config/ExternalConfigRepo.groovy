

package org.ssaad.ami.pipeline.config

class ExternalConfigRepo implements Serializable {

	String repoUrl = "https://github.com/samir-saad/ami-pipeline-configs.git"
	String branch = "master"
	String credentials = "\tami-github"
	String localDir = "external-config"

	String latestCommit = ""
}