// String ansible_playbook = env.ANSIBLE_PLAYBOOK
// String ansible_inventory = env.ANSIBLE_INVENTORY
// String ansible_user = env.ANSIBLE_USER
// String ansible_password = env.ANSIBLE_PASSWORD
// String ansible_ssh_key = env.ANSIBLE_SSH_KEY
String git_url = env.GIT_URL
String git_branch = env.GIT_BRANCH
String git_credentials = env.GIT_CREDENTIALS

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
                script {
                    try {
                        sh 'ansible --version'
                    } catch (Exception e) {
                        println("\033[38;2;255;0;0m[ansibleRun.Ansible Checkout] ERROR: ansible not found \033[0m")
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