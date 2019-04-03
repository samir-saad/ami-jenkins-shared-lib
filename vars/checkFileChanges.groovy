#!/usr/bin/groovy

def call(String repoDir, String oldCommit, String newCommit, String fileName) {

    dir(repoDir) {
        try {
            println("Check file changes for: " + fileName)
            fileName = fileName.replace(fileName.tokenize("/").first() + "/", "")
            println("Check file changes for: " + fileName)

            String changedFiles = sh(script: "git diff ${oldCommit} ${newCommit} --name-only", returnStdout: true).trim()

            println("Changed files: " + changedFiles)

            if (changedFiles.contains(fileName)) {
                return true
            } else {
                return false
            }
        } catch (Exception e) {
            println("Exception happened ${e}")
            return true
        }
    }
}
