pipeline {
    agent any

    tools {
         maven 'maven_3_6_3'
    }
    stages {
        stage('build') {
            steps {
                sh 'mvn clean compile'
            }
        }
    }
}