#!/usr/bin/env groovy

class OtusLibraryImpl implements Serializable {
    def script

    OtusLibraryImpl(script) {
        this.script = script
    }

    def checkAnsible() {
        script.sh "echo '\033[38;2;138;43;226m[OtusLibrary.checkAnsible] INFO: Starting Ansible check...\033[0m'"
        try {
            def result = script.sh(script: 'ansible --version', returnStatus: true)
            
            if (result == 0) {
                script.sh "echo '\033[38;2;138;43;226m[OtusLibrary.checkAnsible] INFO: Ansible check passed\033[0m'"
                return true
            } else {
                script.sh "echo '\033[38;2;255;0;0m[OtusLibrary.checkAnsible] ERROR: Ansible check failed with code: ${result}\033[0m'"
                return false
            }
        } catch (Exception e) {
            script.sh "echo '\033[38;2;255;0;0m[OtusLibrary.checkAnsible] ERROR: checking Ansible: ${e.getMessage()}\033[0m'"
            return false
        }
    }
}

def call(script) {
    return new OtusLibraryImpl(script)
}
