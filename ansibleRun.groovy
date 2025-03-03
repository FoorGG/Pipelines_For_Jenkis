String library_url = 'https://github.com/FoorGG/Pipelines_For_Jenkis.git'
String library_credentials = '12445fd4-62fb-4b17-81d0-8fba8e27be48'

String git_url = env.GIT_URL
String git_branch = env.GIT_BRANCH
String git_credentials = env.GIT_CREDENTIALS

library identifier: 'OtusLibrary@main', 
        retriever: modernSCM([
            $class: 'GitSCMSource',
            remote: '${library_url}',
            credentialsId: '${library_credentials}'
        ])

pipeline {

    agent any

    options {
        ansiColor('xterm')
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
                println("\033[38;2;138;43;226m[ansibleRun.Ansible Checkout] Проверка Ansible\033[0m")
                script {
                    try {
                        if (OtusLibrary.checkAnsible()) {
                            println("\033[38;2;138;43;226m[ansibleRun.Ansible Checkout] INFO: Ansible is installed\033[0m")
                        } else {
                            println("\033[38;2;255;0;0m[ansibleRun.Ansible Checkout] ERROR: Ansible is not installed\033[0m")
                            throw new Exception("Ansible is not installed")
                        }   
                    } catch (Exception e) {
                        println("\033[38;2;255;0;0m[ansibleRun.Ansible Checkout] ERROR: Ansible playbook check failed\033[0m")
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