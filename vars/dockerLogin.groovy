#! /user/bin/env groovy

import com.salzaidy.jenkins.docker.Docker;

def call() {
    return new Docker(this).dockerLogin()
}


