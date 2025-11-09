pipeline {
    agent any
      // Force build on every push
    triggers {
        githubPush() // Trigger on GitHub push events
    }
    tools {
		    maven 'myMaven'  // Use the name you set in Jenkins

	}
	

   environment {
        DOCKER_HUB_USER = "habibulla"
        IMAGE_NAME = "olx-login-image"
        DOCKER_HUB_REPO= "olxloginrepo"
    }
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
                //git branch: '*/**', url: 'https://github.com/Habi557/olxLogin.git' // build on main branch
                checkout scm // build on the branch where the push happened
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
                   
                def tag = env.BUILD_NUMBER   // ✅ Clean way

            sh """
                docker build -t ${DOCKER_HUB_USER}/${IMAGE_NAME}:${tag} .
                docker tag ${DOCKER_HUB_USER}/${IMAGE_NAME}:${tag} ${DOCKER_HUB_USER}/${IMAGE_NAME}:latest
            """


                }
            }
        }

        // Stage 4: Push Docker image to registry (optional)
        stage('Push Docker Image') {
              steps {
        script {
            withCredentials([usernamePassword(credentialsId: 'dockerhub-password', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                sh 'echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin'
                sh "docker push ${DOCKER_HUB_USER}/${DOCKER_HUB_REPO}:${BUILD_NUMBER}"
                sh "docker push ${DOCKER_HUB_USER}/${DOCKER_HUB_REPO}:latest"
            }
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