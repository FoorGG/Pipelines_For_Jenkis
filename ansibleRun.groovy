String library_url = env.LIBRARY_URL
String git_url = env.GIT_URL
String git_branch = env.GIT_BRANCH
String git_credentials = env.GIT_CREDENTIALS
String ansible_playbook = env.ANSIBLE_PLAYBOOK
String ansible_inventory = env.ANSIBLE_INVENTORY
String ansible_cfg = env.ANSIBLE_CFG
String ansible_path = env.ANSIBLE_PATH
String ansible_become_password = env.ANSIBLE_BECOME_PASSWORD
String ansible_credentials = env.ANSIBLE_CREDENTIALS

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
        timeout(time: 15, unit: 'MINUTES')
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
                        "${ansible_inventory}",
                        "${ansible_playbook}",
                        "${ansible_cfg}"
                    ]
                    
                    if (!otusLib.checkDirectory(requiredFiles)) {
                        error("Required Ansible files are missing")
                    }
                }
            }
        }
        
        stage('Run Ansible') {
            steps {
                ansiblePlaybook(
                    playbook: "${ansible_playbook}",
                    inventory: "${ansible_inventory}",
                    credentialsId: "${ansible_credentials}",
                    colorized: true,
                    extras: '--ssh-extra-args="-o StrictHostKeyChecking=no -o ConnectTimeout=60 -o ServerAliveInterval=30" --forks=5',
                    extraVars: [
                        ansible_connection: 'ssh',
                        ansible_become_password: "${ansible_become_password}"
                    ]
                )
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