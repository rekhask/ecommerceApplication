pipeline {
    agent any

    tools {
         maven 'maven_3_6_3'
    }
    stages {
        stage('Build Stage') {
            steps {
                bat 'mvn clean compile'
            }
        }
        stage('Testing Stage') {
            steps {
                bat 'mvn test'
            }
        }
    }
}