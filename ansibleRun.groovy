// String ansible_playbook = env.ANSIBLE_PLAYBOOK
// String ansible_inventory = env.ANSIBLE_INVENTORY
// String ansible_user = env.ANSIBLE_USER
// String ansible_password = env.ANSIBLE_PASSWORD
// String ansible_ssh_key = env.ANSIBLE_SSH_KEY
String git_url = env.GIT_URL
String git_branch = env.GIT_BRANCH

pipeline{

    agent any

    options {
        ansiColor('xterm')
    }

    stages{
        stage('Checkout'){
            steps{
                println("\033[38;2;0;205;0mChecking out ${git_url} ${git_branch}\033[0m")
                checkout scmGit(
                    branches: [[name: '*/main']], 
                    userRemoteConfigs: [[
                        url: 'https://github.com/your-repo/your-project.git',
                        credentialsId: 'github-credentials'
                    ]]
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