
class OtusLibrary {

    boolean checkAnsible() {
        boolean isInstalled;

        try {
            sh '/var/lib/jenkins/ansible/ansible --version'
            isInstalled = true
        } catch (Exception e) {
            isInstalled = false
        }
        return isInstalled;

    }
}
