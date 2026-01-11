#! /user/bin/env groovy

import com.salzaidy.jenkins.docker.Docker;

def call(String imageName) {
    return new Docker(this).buildDockerImage(imageName)
}

