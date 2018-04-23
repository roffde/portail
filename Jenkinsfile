pipeline {
    agent any

    tools {
        jdk 'JDK9'
    }

    parameters {
        booleanParam(defaultValue: false, description: 'Skip tests', name: 'SkipTests')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh "mvn -B clean install -DskipTests"
            }
        }
        stage('Tests') {
        	when { expression { !params.SkipTests } }
            steps {
                parallel (
                        "unit" : {
				sh "mvn -B surefire:test -DtestFailureIgnore=true"
			},
                        "integration" : {
				sh "mvn -B failsafe:integration-test -DskipAfterFailureCount=999"
			}
                    )
            }
        }
        stage('SonarQube analysis') {
        	when { branch 'master' }
            steps {
            	withSonarQubeEnv('SonarDeroffal') {
            		sh "mvn -B clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar -Dsonar.organization=deroffal-github -DskipTests"
            	}
            }
		}
    }
}