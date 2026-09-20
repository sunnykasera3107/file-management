pipeline {
    agent any

     environment {
        MYSQL_HOST='localhost'
        APIGATEWAY='localhost'
        MySQL_DATABASE='user_manager'
        MySQL_USERNAME='who_will_manage'
        MySQL_PASSWORD='whose_Secrets'
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