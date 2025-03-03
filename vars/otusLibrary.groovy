public class OtusLibrary {

    public boolean checkAnsible() {

        boolean isInstalled;

        try {
            sh 'ansible --version'
            isInstalled = true
        } catch (Exception e) {
            isInstalled = false
        }

        return isInstalled;
    }




}