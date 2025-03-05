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
                println("\033[38;2;0;0;255m[ansibleRun.Checkout] INFO: Checking out ${git_url} \"${git_branch}\" \033[0m")
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

                }
            }
        }
        
        stage('Run Ansible') {
            steps {
                script {

                    def otusLib = OtusLibrary(this)

                    if (ansible_path.isEmpty()) {
                        
                        println("\033[38;2;0;0;255m[Run Ansible.runAnsible] INFO: Running ansible playbook ${ansible_playbook} with inventory ${ansible_inventory} and credentials ******** and become password ********\033[0m")
                        otusLib.runAnsible(ansible_playbook, ansible_inventory, ansible_credentials, ansible_become_password)
                    
                    } else {

                        println("\033[38;2;0;0;255m[Run Ansible.runAnsible] INFO: Running ansible playbook ${ansible_playbook} with inventory ${ansible_inventory} and credentials ********** and become password ******** and path ${ansible_path}\033[0m")
                        otusLib.runAnsible(ansible_playbook, ansible_inventory, ansible_credentials, ansible_become_password, ansible_path)
                    
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