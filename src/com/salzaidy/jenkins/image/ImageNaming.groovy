package com.salzaidy.jenkins.image

class ImageNaming implements Serializable {
    def script

    ImageNaming(script) {
        this.script = script
    }

    String build(String projectName, String clearVersion) {
        def buildNum = script.env.BUILD_NUMBER
        def registry = (script.env.DOCKER_REGISTRY ?: 'dockerhub').toLowerCase()

        if (registry == 'ecr') {
            def ecrRepo = script.env.ECR_REPO
            if (!ecrRepo?.trim()) script.error("ECR_REPO is not set")
            // single ECR repo => project name goes in TAG
            return "${ecrRepo}:${projectName}-${clearVersion}-${buildNum}"
        }

        def hubRepo = script.env.DOCKERHUB_REPO
        if (!hubRepo?.trim()) script.error("DOCKERHUB_REPO is not set")
        return "${hubRepo}/${projectName}:${clearVersion}-${buildNum}"
    }
}