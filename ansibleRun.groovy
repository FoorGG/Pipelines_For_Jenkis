pipeline{

    agent any

    stages{
        stage('Checkout'){
            steps{
                script{
                    println("Hello World")
                }
            }
        }
    }

}