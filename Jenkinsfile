pipeline {
    agent any

    stages {
        stage('build') {
            steps {
                withMaven ('maven_3_6_3') {
                    sh 'mvn clean compile'
                }
            }
        }
    }
}