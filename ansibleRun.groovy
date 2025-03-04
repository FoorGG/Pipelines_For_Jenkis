String library_url = 'https://github.com/FoorGG/Pipelines_For_Jenkis.git'

String git_url = env.GIT_URL
String git_branch = env.GIT_BRANCH
String git_credentials = env.GIT_CREDENTIALS

library identifier: 'OtusLibrary@main', 
        retriever: modernSCM([
            $class: 'GitSCMSource',
            remote: "${library_url}",
            credentialsId: "${git_credentials}"
        ])

pipeline {

    agent any

    options {
        ansiColor('xterm')
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                println("\033[38;2;138;43;226m[ansibleRun.Checkout] INFO: Checking out ${git_url} \"${git_branch}\" \033[0m")
                script {
                    try {
                        checkout scmGit(
                            branches: [[name: "${git_branch}"]], 
                            userRemoteConfigs: [[
                                url: "${git_url}",
                                credentialsId: "${git_credentials}"
                            ]]
                        )
                    } catch (Exception e) {
                        println("\033[38;2;255;0;0m[ansibleRun.Checkout] ERROR: checking out ${git_url} \"${git_branch}\" \033[0m")
                        throw e
                    }
                }
            }
        }
        
        stage('Check Environment') {
            steps {
                script {
                    def otusLib = OtusLibrary(this)
                    

                    if (!otusLib.checkAnsible()) {
                        error("Ansible is not installed")
                    }
                    

                    def requiredFiles = [
                        'hosts.ini',
                        'site.yml',
                        'ansible.cfg'
                    ]
                    
                    if (!otusLib.checkDirectory('ansible', requiredFiles)) {
                        error("Required Ansible files are missing")
                    }
                }
            }
        }

    
    post {
        always {
            deleteDir()
            dir("workdir") {
                deleteDir()
            }
        }
    }
}