package com.salzaidy.jenkins.build

import com.salzaidy.jenkins.build.tools.BuildToolDetector
import com.salzaidy.jenkins.build.tools.GradleTool
import com.salzaidy.jenkins.build.tools.MavenTool
import com.salzaidy.jenkins.image.ImageNaming   // adjust package if yours differs


class BuildOps implements Serializable {
    def script

    BuildOps(script) {
        this.script = script
    }

    void test() {
        def type = new BuildToolDetector(script).detect()
        script.echo "Running tests using build tool: ${type}"

        if (type == 'gradle') {
            new GradleTool(script).test()
        } else {
            new MavenTool(script).test()
        }
    }


    void buildJar() {
        def type = new BuildToolDetector(script).detect()
        script.echo "Building jar using build tool: ${type}"

        if (type == 'gradle') {
            new GradleTool(script).buildJar()
        } else {
            new MavenTool(script).buildJar()
        }
    }


    void bumpVersionAndPrepareImage() {
        def type = new BuildToolDetector(script).detect()
        script.echo "Bumping version and preparing IMAGE_NAME using build tool: ${type}"

        def tool = (type == 'gradle') ? new GradleTool(script) : new MavenTool(script)

        // 1) bump version, return newVersion like 0.0.8-SNAPSHOT
        def newVersion = tool.bumpPatchSnapshot()

        // 2) clearVersion for image tags like 0.0.8
        def clearVersion = newVersion.replace('-SNAPSHOT', '')
        script.echo "Clear version (for image tag) is: ${clearVersion}"

        // 3) project name
        def projectName = tool.projectName()
        script.env.PROJECT_NAME = projectName
        script.echo "PROJECT_NAME is: ${script.env.PROJECT_NAME}"

        // 4) image name (DockerHub vs ECR handled here)
        script.env.IMAGE_NAME = new ImageNaming(script).build(projectName, clearVersion)
        script.echo "IMAGE_NAME will be: ${script.env.IMAGE_NAME}"
    }





}
