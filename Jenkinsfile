pipeline {
    agent any
      // Force build on every push
    triggers {
        githubPush() // Trigger on GitHub push events
    }
    tools {
		    maven 'myMaven'  // Use the name you set in Jenkins

	}
	

    //environment {
            
        
    //}
    stages {
		//Delete the workspace
		stage('Delete Workspace'){
			steps {
				cleanWs()
			}
			
		}
        // Stage 1: Checkout code
        stage('Checkout') {
            steps {
                git branch: '*/**', url: 'https://github.com/Habi557/olxLogin.git'
            }
        }

        // Stage 2: Build the application
        // stage('Build') {
        //     steps {
		// 			echo 'maveen cleaning step!'
		// 			sh 'mvn -version'  // Uses the 'myMaven' installation
        //             sh 'mvn clean package' 
               
        //     }
        // }

        // Stage 3: Build Docker image
        stage('Build Docker Image') {
            steps {
				echo 'Build Docker Image!'
                script {
                   // docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                   // sh "docker build -t ${DOCKER_IMAGE} ."
                   //sh "docker --version"
                  // sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                 // docker.build("Habibulla/olx-login-image:latest", ".")
                 sh "docker build -t olx-login-image ."


                }
            }
        }

        // Stage 4: Push Docker image to registry (optional)
        stage('Push Docker Image') {
            steps {
                script {
                    echo 'Push Docker Image to Registry!'
                  //  docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                      //  docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                      // sh 'docker-compose up -d'
                    //}
                     // sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"


                }
            }
        }

        // Stage 5: Deploy the application (optional)
        stage('Deploy') {
            steps {
                sh 'echo "Deploying application..."'
                // Add deployment steps here (e.g., deploy to Kubernetes, AWS, etc.)
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
    
    
    
    
    
    
}