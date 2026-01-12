package com.salzaidy.jenkins.build.tools

class GradleTool implements Serializable {
    def script
    GradleTool(script) { this.script = script }

    void test() {
        script.echo "Testing with Gradle..."

        // If wrapper exists, prefer it (most reproducible)
        if (script.fileExists('gradlew')) {
//            script.sh "chmod +x ./gradlew"
            script.sh "./gradlew test"
        } else {
            // Uses Jenkins tool config: tools { gradle '...' }
            script.sh "gradle test"
        }
    }
}


