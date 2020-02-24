pipeline {
    agent any

    tools {
         maven 'maven_3_6_3'
    }
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