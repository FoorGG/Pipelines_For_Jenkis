

class OtusLibrary {
    boolean checkAnsible() {
        boolean isInstalled;
        try {
            def result = sh(script: 'ansible --version', returnStatus: true)
            isInstalled = true
        } catch (Exception e) {
            isInstalled = false
        }
        return isInstalled;
    }
}