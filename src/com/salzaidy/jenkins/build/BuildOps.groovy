package com.salzaidy.jenkins.build

import com.salzaidy.jenkins.build.tools.BuildToolDetector
import com.salzaidy.jenkins.build.tools.GradleTool
import com.salzaidy.jenkins.build.tools.MavenTool

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



}
