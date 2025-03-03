
class OtusLibrary {

    boolean checkAnsible() {
        
        boolean isInstalled;
        byte rest = sh returnStatus: true, script: 'ansible --version'
        
        if (rest == 0) {
            isInstalled = true
        } else {
            isInstalled = false
        }
        
        return isInstalled;

    }
}
