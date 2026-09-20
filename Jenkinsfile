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