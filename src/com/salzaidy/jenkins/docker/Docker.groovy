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
        if (!registry) {
            script.error("DOCKER_ECR_REPO_SERVER is not set")
        }

        script.echo "Logging in to AWS ECR: ${registry}"

        script.withCredentials([
                script.usernamePassword(
                        credentialsId: 'ecr-credentials',
                        usernameVariable: 'USER',
                        passwordVariable: 'PASS'
                )
        ]) {
            script.sh "echo \"\$PASS\" | docker login -u \"\$USER\" --password-stdin ${registry}"
        }
    }


    def dockerPush(String imageName) {
        script.sh "docker push $imageName"
    }
}
