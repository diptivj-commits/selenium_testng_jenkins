pipeline {
    agent { label 'selenium' }
    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }
    options {
        disableConcurrentBuilds()
        timeout(time: 20, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '15', artifactNumToKeepStr: '10'))
        skipDefaultCheckout(true)
    }
    stages {
        stage('Checkout') {
            steps {
                // Remove stale reports before checkout so a failed checkout cannot publish an old result.
                dir('reports') { deleteDir() }
                checkout scm
            }
        }
        stage('Verify tools') {
            steps {
                script {
                    if (isUnix()) { sh 'java -version && mvn -version' }
                    else { bat 'java -version && mvn -version' }
                }
            }
        }
        stage('Run Selenium TestNG suite') {
            steps {
                script {
                    if (isUnix()) { sh 'mvn -B -ntp clean test -Dheadless=true' }
                    else { bat 'mvn -B -ntp clean test -Dheadless=true' }
                }
            }
        }
    }
    post {
        always {
            // Surefire generates JUnit-format XML even when the tests use TestNG.
            junit testResults: 'reports/testng/TEST-*.xml', allowEmptyResults: true
            archiveArtifacts artifacts: 'reports/**/*', allowEmptyArchive: true
        }
    }
}
