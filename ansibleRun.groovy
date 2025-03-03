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
                println("\033[38;2;138;43;226m[ansibleRun.Checkout] Checking out ${git_url} \"${git_branch}\" \033[0m")
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