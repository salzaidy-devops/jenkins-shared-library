#! /user/bin/env groovy

import com.example.Docker;

def call(String imageName) {
    return new Docker(this).buildDockerImage(imageName)
}

// old method w/out Docker Groovy Class
//def call(String imageName) {
//    echo "building the docker image..."
//    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
//        sh "docker build -t $imageName ."
////        sh 'docker build -t salzaidy/coffee-api:3.2 .'
//        sh 'echo $PASS | docker login -u $USER --password-stdin'
//        sh "docker push $imageName"
////        sh 'docker push salzaidy/coffee-api:3.2'
//    }
//}

