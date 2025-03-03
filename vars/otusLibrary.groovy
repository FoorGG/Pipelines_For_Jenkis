#!/usr/bin/env groovy

class OtusLibrary {
    def checkAnsible() {
        def result = sh(script: 'ansible --version', returnStatus: true)
        return result == 0
    }
}