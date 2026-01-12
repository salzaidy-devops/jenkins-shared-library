#! /user/bin/env groovy
import com.salzaidy.jenkins.docker.Docker

def build(String imageName) {
    new Docker(this).buildDockerImage(imageName)
}

def login() {
    new Docker(this).dockerLogin()
}

def push(String imageName) {
    new Docker(this).dockerPush(imageName)
}
