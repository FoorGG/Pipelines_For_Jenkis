
class OtusLibraryImpl implements Serializable {
    
    def script
    def playbook
    def inventory
    def credentials
    def become_password
    def path


    OtusLibraryImpl(script) {

        this.script = script

    }

    def checkAnsible() {
        
        script.sh "echo '\033[38;2;0;0;255m[OtusLibrary.checkAnsible] INFO: Starting Ansible check...\033[0m'"
        
        try {

            def result = script.sh(script: 'ansible --version', returnStatus: true)
            
            if (result == 0) {

                script.sh "echo '\033[38;2;0;0;255m[OtusLibrary.checkAnsible] INFO: Ansible check passed\033[0m'"
                return true
            
            } else {

                script.sh "echo '\033[38;2;255;0;0m[OtusLibrary.checkAnsible] ERROR: Ansible check failed with code: ${result}\033[0m'"
                return false
            
            }
        } catch (Exception e) {

            script.sh "echo '\033[38;2;255;0;0m[OtusLibrary.checkAnsible] ERROR: checking Ansible: ${e.getMessage()}\033[0m'"
            return false
        
        }
    }

    def runAnsible(String playbook, String inventory, String credentials, String become_password) {

        this.playbook = playbook
        this.inventory = inventory
        this.credentials = credentials
        this.become_password = become_password

        script.ansiblePlaybook(
            playbook: "${this.playbook}",
            inventory: "${this.inventory}",
            credentialsId: "${this.credentials}",
            colorized: true,
            extras: '--ssh-extra-args="-o StrictHostKeyChecking=no -o ConnectTimeout=60 -o ServerAliveInterval=30" --forks=5',
            extraVars: [
                ansible_connection: 'ssh',
                ansible_become_password: "${this.become_password}"
            ]
        )
    }

    def runAnsible(String playbook, String inventory, String credentials, String become_password, String path) {

        this.playbook = playbook
        this.inventory = inventory
        this.credentials = credentials
        this.become_password = become_password
        this.path = path

        script.ansiblePlaybook(
            playbook: "${this.path}/${this.playbook}",
            inventory: "${this.path}/${this.inventory}",
            credentialsId: "${this.credentials}",
            colorized: true,
            extras: '--ssh-extra-args="-o StrictHostKeyChecking=no -o ConnectTimeout=60 -o ServerAliveInterval=30" --forks=5',
            extraVars: [
                ansible_connection: 'ssh',
                ansible_become_password: "${this.become_password}"
            ]
        )   
    }
        
}

def call(script) {

    return new OtusLibraryImpl(script)

}
