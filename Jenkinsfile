pipeline {
    agent any

    stages {
        
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Compile User Service') {
            steps {
                dir('users') {
                    bat 'mvn clean compile -DskipTests'
                }
            }
        }

        stage('Parallel checks') {
            parallel {
                stage('Test User Service') {
                    steps {
                        dir('users') {
                            bat 'mvn test'
                        }
                    }
                }

                stage('Code Quality') {
                    steps {
                        dir('users') {
                            bat 'mvn verify -DskipTests'
                        }
                    }
                }

                stage('Security scan') {
                    steps {
                        dir('users') {
                            bat 'mvn dependency:tree'
                        }
                    }
                }
            }
        }

        stage('Build User Service') {
            steps {
                dir('users') {
                    bat 'mvn package -DskipTests'
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

        stage('Docker Build') {
            steps {
                dir('users') {
                    bat 'docker build -t file-management-users:${BUILD_NUMBER} .'
                }
            }
        }

        stage('Docker Run') {
            steps {
                dir('users') {
                    bat 'docker run -p 8081:8080 file-management-users:${BUILD_NUMBER}'
                }
            }
        }
    }
}