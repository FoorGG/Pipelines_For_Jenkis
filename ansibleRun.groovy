def str = ENV.STRING

pipeline{

    agent any

    stages{
        stage('print'){
            steps{
                script{
                    println(${str})
                }
            }
        }
    }
    post {
        always {
            cleanWs()
            dir("workdir") {
                deleteDir()
            }
        }
    }
}