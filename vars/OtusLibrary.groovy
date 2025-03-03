#!/usr/bin/env groovy

class OtusLibraryImpl implements Serializable {
    def script

    OtusLibraryImpl(script) {
        this.script = script
    }

    def checkAnsible() {
        try {
            def result = script.sh(script: 'ansible --version', returnStatus: true)
            
            if (result == 0) {
                return true
            } else {
                return false
            }
        } catch (Exception e) {
            return false
        }
    }
}

def call(script) {
    return new OtusLibraryImpl(script)
}
