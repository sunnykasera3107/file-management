pipeline {
    agent any

    stages {
        
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build User Service') {
            steps {
                dir('users') {
                    bat 'mvn package -DskipTests'
                }
            }
        }

        stage('Test User Service') {
            steps {
                dir('users') {
                    bat 'mvn test'
                }
            }

            post {
                success {
                    echo 'CI pipeline succeeded'
                }

                failure {
                    echo 'CI pipeline failed'
                }

                always {
                    junit 'users/target/surefire-reports/*.xml'
                }
            }
        }
    }
}