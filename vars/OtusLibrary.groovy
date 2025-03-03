
class OtusLibrary {

    def checkAnsible() {
        try {
            def process = new ProcessBuilder("ansible", "--version").start()
            def exitCode = process.waitFor()
            
            // Получаем вывод команды
            def output = new String(process.inputStream.readAllBytes())
            def error = new String(process.errorStream.readAllBytes())
            
            println("\033[38;2;138;43;226m[OtusLibrary] Ansible check output: ${output}\033[0m")
            
            if (exitCode != 0) {
                println("\033[38;2;255;0;0m[OtusLibrary] Ansible check error: ${error}\033[0m")
                println("\033[38;2;255;0;0m[OtusLibrary] Exit code: ${exitCode}\033[0m")
                return false
            }
            
            return true
        } catch (Exception e) {
            println("\033[38;2;255;0;0m[OtusLibrary] Error checking Ansible: ${e.getMessage()}\033[0m")
            return false
        }
    }

    def call() {
        return this
    }
}
