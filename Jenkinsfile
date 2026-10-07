pipeline {
    agent any

    environment {
        IMAGE_NAME = 'novabank-transfer'
        CONTAINER_NAME = 'novabank-transfer'
        APP_PORT = '8082'
    }

    stages {

        stage('Git Checkout') {
            steps {
                echo 'Checking out the repository...'
                checkout scm
            }
        }

        stage('JUnit Test') {
            steps {
                echo 'Running JUnit tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml',
                         allowEmptyResults: true
                }
            }
        }

        stage('Maven Build') {
            steps {
                echo 'Building application with Maven...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Gitleaks') {
            steps {
                echo 'Running Gitleaks security scan...'
                sh '''
                    mkdir -p reports

                    gitleaks detect \
                        --source . \
                        --report-format json \
                        --report-path reports/gitleaks.json \
                        --no-banner || true

                    echo "Gitleaks scan completed."
                '''
            }
            post {
                always {
                    archiveArtifacts artifacts: 'reports/gitleaks.json',
                                     allowEmptyArchive: true
                }
            }
        }

        stage('Docker Image Build') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .'
            }
        }

        stage('Trivy Scan') {
            steps {
                echo 'Running Trivy vulnerability scan...'
                sh '''
                    mkdir -p reports

                    trivy image \
                        --format json \
                        --output reports/trivy.json \
                        --severity HIGH,CRITICAL \
                        --exit-code 0 \
                        --scanners vuln \
                        ${IMAGE_NAME}:${BUILD_NUMBER} || true

                    echo "Trivy scan completed."
                '''
            }
            post {
                always {
                    archiveArtifacts artifacts: 'reports/trivy.json',
                                     allowEmptyArchive: true
                }
            }
        }

        stage('Docker Run') {
            steps {
                echo 'Starting Docker container...'
                sh '''
                    docker rm -f ${CONTAINER_NAME} || true

                    docker run -d \
                        --name ${CONTAINER_NAME} \
                        -p ${APP_PORT}:8082 \
                        ${IMAGE_NAME}:${BUILD_NUMBER}

                    echo "Waiting for application..."
                    sleep 15

                    curl --fail http://localhost:${APP_PORT}/actuator/health

                    echo "Application is healthy."
                '''
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar',
                             allowEmptyArchive: true
        }

        success {
            echo 'ALL CI/CD STAGES COMPLETED SUCCESSFULLY.'
        }

        failure {
            echo 'Pipeline failed.'
        }
    }
}
