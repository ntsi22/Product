pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }
    
    environment {
    DOCKER_IMAGE = 'ntsi/product'
    DOCKER_CREDENTIALS = 'dockerhub-credentials'
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
                        ./mvnw -B org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
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
        
        stage('Build Docker Image') {
    		steps {
    	    sh '''
            docker build \
              -t ${DOCKER_IMAGE}:${BUILD_NUMBER} \
              -t ${DOCKER_IMAGE}:latest \
              .
        '''
    }
    }
    stage('Push Docker Image') {
    steps {
        withCredentials([
            usernamePassword(
                credentialsId: 'dockerhub-credentials',
                usernameVariable: 'DOCKER_USERNAME',
                passwordVariable: 'DOCKER_TOKEN'
            )
        ]) {
            sh '''
                echo "$DOCKER_TOKEN" | docker login \
                  -u "$DOCKER_USERNAME" \
                  --password-stdin

                docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}
                docker push ${DOCKER_IMAGE}:latest

                docker logout
            '''
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