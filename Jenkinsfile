pipeline {
  agent any

  options {
    timestamps()
    disableConcurrentBuilds()
  }

  environment {
    SONAR_HOST_URL = 'https://sonarcloud.io'
    // Configure these in Jenkins credentials/parameters as needed.
    SONAR_PROJECT_KEY = credentials('sonar-project-key')
    SONAR_ORGANIZATION = credentials('sonar-organization')
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build and Test') {
      steps {
        sh 'chmod +x mvnw'
        sh './mvnw -B clean verify'
      }
    }

    stage('SonarQube Analysis') {
      steps {
        withSonarQubeEnv('sonarcloud') {
          sh """
            ./mvnw -B sonar:sonar \
              -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
              -Dsonar.organization=${SONAR_ORGANIZATION} \
              -Dsonar.host.url=${SONAR_HOST_URL}
          """
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
    always {
      junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
      archiveArtifacts allowEmptyArchive: true, artifacts: 'target/*.jar'
    }
  }
}
