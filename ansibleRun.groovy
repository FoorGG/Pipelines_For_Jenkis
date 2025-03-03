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
                            branches: [[name: '${git_branch}']], 
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
        
        stage('Ansible Checkout') {
            steps {
                println("\033[38;2;138;43;226m[ansibleRun.Stage(\'Ansible Checkout\')] Проверка Ansible\033[0m")
                script {
                    try {
                        def otusLib = OtusLibrary(this)
                        
                        if (otusLib.checkAnsible()) {
                            println("\033[38;2;138;43;226m[Pipeline] Ansible is installed\033[0m")
                           
                        } else {
                            println("\033[38;2;255;0;0m[Pipeline] Ansible is not installed\033[0m")
                            error("Ansible is not installed")
                        }
                    } catch (Exception e) {
                        println("\033[38;2;255;0;0m[Pipeline] ERROR: ${e.getMessage()}\033[0m")
                        throw e
                    }
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