pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Compilation') {
            steps {
           		sh 'java -version'
        		sh 'javac -version'
        		sh './mvnw -version'

                sh 'chmod +x mvnw'
                sh './mvnw -B clean compile'
            }
        }

        stage('Tests') {
            steps {
                sh './mvnw -B test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml',
                          allowEmptyResults: true
                }
            }
        }

        stage('Package') {
            steps {
                sh './mvnw -B package -DskipTests'
            }
        }

        stage('SonarQube') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        ./mvnw -B sonar:sonar \
                          -Dsonar.projectKey=mon-projet-springboot \
                          -Dsonar.projectName=mon-projet-springboot
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        success {
            echo '✅ Compilation, tests et analyse SonarQube réussis.'
        }

        failure {
            echo '❌ Échec de la compilation, des tests ou de la Quality Gate.'
        }

        always {
            archiveArtifacts artifacts: 'target/*.jar',
                             allowEmptyArchive: true,
                             fingerprint: true
        }
    }
}