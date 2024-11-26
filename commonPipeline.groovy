def lint() {
    stage('Lint') {
        steps {
            sh 'pip install pylint'
            sh 'pylint --fail-under=5 .'
        }
    }
}

def security() {
    stage('Security') {
        steps {
            sh 'pip install safety'
            sh 'safety check'
        }
    }
}

def package() {
    stage('Package') {
        steps {
            script {
                docker.build("erfan25/your-service-name:${env.BUILD_ID}")
                    .push("latest")
            }
        }
    }
}

def deploy() {
    stage('Deploy') {
        steps {
            sshagent(['your-jenkins-ssh-credential-id']) {
                sh '''
                ssh user@3855-vm-ip-address << EOF
                cd /path/to/your/docker-compose-directory
                docker-compose pull
                docker-compose up -d
                EOF
                '''
            }
        }
    }
}
