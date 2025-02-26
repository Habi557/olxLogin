pipeline {
    agent any
    tools {
		    maven 'myMaven'  // Use the name you set in Jenkins

	}

    environment {
        // Define environment variables
        APP_NAME = "olx-login"
       // DOCKER_IMAGE = "Habibulla/${olx-login-image}"
       DOCKER_IMAGE = "Habibulla/${APP_NAME}-image"
       DOCKER_TAG = "latest"
       MAIL_PASSWORD = "obrljvofqymhtpbd"
       MAIL_USERNAME = "habibullashaik9944@gmail.com"
       MYSQL_PASSWORD = "786786786"
       MYSQL_USERNAME = "root"
       SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3306/olx_login"
        
        
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
                git branch: 'main', url: 'https://github.com/Habi557/olxLogin.git'
            }
        }

        // Stage 2: Build the application
        stage('Build') {
            steps {
							echo 'maveen cleaning step!'
							                sh 'mvn -version'  // Uses the 'myMaven' installation

                sh 'mvn clean package' // For Maven
                // sh './gradlew build' // For Gradle
            }
        }

        // Stage 3: Build Docker image
        stage('Build Docker Image') {
            steps {
				echo 'Build Docker Image!'
                script {
                   // docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                   // sh "docker build -t ${DOCKER_IMAGE} ."
                   sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."


                }
            }
        }

        // Stage 4: Push Docker image to registry (optional)
        stage('Push Docker Image') {
            steps {
                script {
                  //  docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                      //  docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                      // sh 'docker-compose up -d'
                    //}
                      sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"

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