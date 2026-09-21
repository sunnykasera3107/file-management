pipeline {
    agent any

    environment {
        Test='test'
    }

    stages {
        
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build User Service') {
            steps {
                dir('users') {
                    bat 'mvn test'
                }
            }
        }

        stage('Test User Service') {
            steps {
                dir('users') {
                    bat 'mvn test'
                }
            }
        }
    }
}