#! /user/bin/env groovy
package com.salzaidy.jenkins.docker

class Docker implements Serializable {

    def script

    Docker(script) {
        this.script = script
    }

    def buildDockerImage(String imageName) {
        script.echo "building the docker image..."
            script.sh "docker build -t $imageName ."
    }


    def dockerLogin() {
        if (script.env.DOCKER_REGISTRY == 'ecr') {
            ecrLogin()
        } else {
            dockerHubLogin()
        }
    }


    private def dockerHubLogin() {
        script.echo "Logging in to Docker Hub"
        script.withCredentials([
                script.usernamePassword(
                        credentialsId: 'docker-hub-repo',
                        usernameVariable: 'USER',
                        passwordVariable: 'PASS'
                )
        ]) {
//            script.sh "echo ${script.PASS} | docker login -u ${script.USER} --password-stdin"
            script.sh 'echo "$PASS" | docker login -u "$USER" --password-stdin'
        }
    }



    private def ecrLogin() {
        def registry = script.env.DOCKER_ECR_REPO_SERVER
        def region = script.env.AWS_REGION ?: 'us-east-1'

        if (!registry) {
            script.error("DOCKER_ECR_REPO_SERVER is not set")
        }

        // Credential IDs are configurable, so Jenkinsfile stays provider-agnostic
        def accessKeyIdCred = script.env.AWS_ACCESS_KEY_ID_CRED_ID ?: 'jenkins_aws_access_key_id'
        def secretKeyCred   = script.env.AWS_SECRET_ACCESS_KEY_CRED_ID ?: 'jenkins_aws_secret_access_key'

        script.echo "Logging in to AWS ECR: ${registry}"

        script.withCredentials([
                script.string(credentialsId: accessKeyIdCred, variable: 'AWS_ACCESS_KEY_ID'),
                script.string(credentialsId: secretKeyCred, variable: 'AWS_SECRET_ACCESS_KEY')
        ]) {
            // Optional: support session token if you ever use assumed roles
            // If you have it, add another env var + credential and export AWS_SESSION_TOKEN.

            script.sh """
          aws ecr get-login-password --region ${region} \
          | docker login --username AWS --password-stdin ${registry}
        """
        }

        /*
        script.withCredentials([
                script.usernamePassword(
                        credentialsId: 'ecr-credentials',
                        usernameVariable: 'USER',
                        passwordVariable: 'PASS'
                )
        ]) {
            script.sh "echo \"\$PASS\" | docker login -u \"\$USER\" --password-stdin ${registry}"
        }
         */

    }


    def dockerPush(String imageName) {
        script.sh "docker push $imageName"
    }
}
