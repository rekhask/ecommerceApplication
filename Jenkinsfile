pipeline {
    agent any //{ docker { image 'maven:3.3.3' } }
    stages {
        stage('build') {
            steps {
                withMaven (maven : 'maven_3_6_3') {
                    sh 'mvn clean compile'
                }
            }
        }
    }
}