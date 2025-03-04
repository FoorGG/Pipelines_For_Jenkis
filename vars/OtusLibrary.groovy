#!/usr/bin/env groovy

class OtusLibraryImpl implements Serializable {
    def script

    OtusLibraryImpl(script) {
        this.script = script
    }

    def checkAnsible() {
        script.sh "echo '\033[38;2;138;43;226m[OtusLibrary.checkAnsible] INFO: Starting Ansible check...\033[0m'"
        try {
            def result = script.sh(script: 'ansible --version', returnStatus: true)
            
            if (result == 0) {
                script.sh "echo '\033[38;2;138;43;226m[OtusLibrary.checkAnsible] INFO: Ansible check passed\033[0m'"
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

    def checkDirectory(String path, List<String> requiredFiles = []) {
        script.sh "echo '\033[38;2;138;43;226m[OtusLibrary.checkDirectory] INFO: Checking directory: ${path}\033[0m'"
        try {
            def dirExists = script.sh(
                script: "test -d ${path}",
                returnStatus: true
            ) == 0

            if (!dirExists) {
                script.sh "echo '\033[38;2;255;0;0m[OtusLibrary.checkDirectory] ERROR: Directory ${path} does not exist\033[0m'"
                return false
            }

            if (requiredFiles.isEmpty()) {
                script.sh "echo '\033[38;2;138;43;226m[OtusLibrary.checkDirectory] ERROR: Directory is empty\033[0m'"
                return true
            }

            def missingFiles = []
            requiredFiles.each { file ->
                def filePath = "${path}/${file}"
                def fileExists = script.sh(
                    script: "test -f ${filePath}",
                    returnStatus: true
                ) == 0

                if (!fileExists) {
                    missingFiles.add(file)
                    script.sh "echo '\033[38;2;255;0;0m[OtusLibrary.checkDirectory] ERROR: File not found: ${filePath}\033[0m'"
                } else {
                    script.sh "echo '\033[38;2;138;43;226m[OtusLibrary.checkDirectory] INFO: File found: ${filePath}\033[0m'"
                }
            }

            if (missingFiles.isEmpty()) {
                script.sh "echo '\033[38;2;138;43;226m[OtusLibrary.checkDirectory] INFO: All required files found in ${path}\033[0m'"
                return true
            } else {
                script.sh "echo '\033[38;2;255;0;0m[OtusLibrary.checkDirectory] ERROR: Missing files in ${path}: ${missingFiles.join(', ')}\033[0m'"
                return false
            }

        } catch (Exception e) {
            script.sh "echo '\033[38;2;255;0;0m[OtusLibrary.checkDirectory] ERROR: checking directory: ${e.getMessage()}\033[0m'"
            return false
        }
    }

    def runPlaybook(Map config) {
        script.sh "echo '\033[38;2;138;43;226m[OtusLibrary] Starting Ansible playbook execution...\033[0m'"
        
        try {
            // Проверяем обязательные параметры
            if (!config.playbook) {
                script.sh "echo '\033[38;2;255;0;0m[OtusLibrary] Error: playbook path is required\033[0m'"
                return false
            }

            if (!config.inventory) {
                script.sh "echo '\033[38;2;255;0;0m[OtusLibrary] Error: inventory path is required\033[0m'"
                return false
            }

            // Формируем команду
            def cmd = [
                'ansible-playbook',
                config.playbook,
                "-i ${config.inventory}"
            ]

            // Добавляем опциональные параметры
            if (config.tags) {
                cmd.add("--tags '${config.tags}'")
            }

            if (config.extraVars) {
                cmd.add("--extra-vars '${config.extraVars}'")
            }

            if (config.limit) {
                cmd.add("--limit '${config.limit}'")
            }

            if (config.verbose) {
                cmd.add('-v')
            }

            if (config.checkMode) {
                cmd.add('--check')
            }

            // Выполняем команду
            script.sh "echo '\033[38;2;138;43;226m[OtusLibrary] Executing: ${cmd.join(' ')}\033[0m'"
            
            def result = script.sh(
                script: cmd.join(' '),
                returnStatus: true
            )

            if (result == 0) {
                script.sh "echo '\033[38;2;138;43;226m[OtusLibrary] Playbook execution completed successfully\033[0m'"
                return true
            } else {
                script.sh "echo '\033[38;2;255;0;0m[OtusLibrary] Playbook execution failed with code: ${result}\033[0m'"
                return false
            }

        } catch (Exception e) {
            script.sh "echo '\033[38;2;255;0;0m[OtusLibrary] Error executing playbook: ${e.getMessage()}\033[0m'"
            return false
        }
    }
}

def call(script) {
    return new OtusLibraryImpl(script)
}
