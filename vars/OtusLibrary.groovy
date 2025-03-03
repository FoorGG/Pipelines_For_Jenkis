#!/usr/bin/env groovy

class OtusLibrary implements Serializable {
    def script

    OtusLibrary(script) {
        this.script = script
    }

    def checkAnsible() {
        println("\033[38;2;138;43;226m[OtusLibrary] Starting Ansible check...\033[0m")
        try {
            def result = script.sh(script: 'ansible --version', returnStatus: true)
            
            if (result == 0) {
                println("\033[38;2;138;43;226m[OtusLibrary] Ansible check passed\033[0m")
                return true
            } else {
                println("\033[38;2;255;0;0m[OtusLibrary] Ansible check failed with code: ${result}\033[0m")
                return false
            }
        } catch (Exception e) {
            println("\033[38;2;255;0;0m[OtusLibrary] Error checking Ansible: ${e.getMessage()}\033[0m")
            return false
        }
    }
}

def call(script) {
    return new OtusLibrary(script)
}
