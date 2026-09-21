pipeline {
    agent any

    stages {
        
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Remove Old Infrastructure') {
            steps {
                bat 'docker compose down -v'
                
                dir("database") {
                    bat 'docker compose down -v'
                }

                bat 'docker network inspect file-management-network >nul 2>&1 && docker network rm file-management-network'
            }
        }

        stage('Setup Infrastructure') {
            steps {
                bat 'docker network create file-management-network'
                
                dir("database") {
                    bat 'docker compose up --build -d'
                }

                sleep(15)
            }

            post {
                success {
                    echo 'Database initiated.'
                }

                failure {
                    echo 'Failed to start databases'
                    dir('database') {
                        bat 'docker compose down -v'
                    }
                    bat 'docker network rm file-management-network'
                }
                always {
                    echo 'Database always works'
                }
            }
        }

        stage('Compile Services') {
            steps {
                dir('users') {
                    bat 'mvn clean compile -DskipTests'
                }

                dir('files') {
                    bat 'mvn clean compile -DskipTests'
                }

                dir('api') {
                    bat 'mvn clean compile -DskipTests'
                }
            }
        }

        stage('Parallel checks') {
            parallel {
                stage('Test Service') {
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

                        dir('files') {
                            bat 'mvn verify -DskipTests'
                        }

                        dir('api') {
                            bat 'mvn verify -DskipTests'
                        }
                    }
                }

                stage('Security scan') {
                    steps {
                        dir('users') {
                            bat 'mvn dependency:tree'
                        }

                        dir('files') {
                            bat 'mvn dependency:tree'
                        }

                        dir('api') {
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

                dir('files') {
                    bat 'mvn package -DskipTests'
                }

                dir('api') {
                    bat 'mvn package -DskipTests'
                }
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker compose up --build -d'
            }

            post {
                success {
                    echo 'CI pipeline succeeded'
                }

                failure {
                    echo 'CI pipeline failed'
                    bat 'docker compose down -v'
                    dir("database") {
                        bat 'docker compose down -v'
                    }
                }

                always {
                    junit 'users/target/surefire-reports/*.xml'
                }
            }
        }
    }
}