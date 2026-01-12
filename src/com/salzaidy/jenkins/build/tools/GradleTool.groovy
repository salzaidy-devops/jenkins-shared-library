package com.salzaidy.jenkins.build.tools

class GradleTool implements Serializable {
    def script
    GradleTool(script) { this.script = script }

    void test() {
        script.echo "Testing with Gradle..."

        // If wrapper exists, use it
        if (script.fileExists('gradlew')) {
//            script.sh "chmod +x ./gradlew"
            script.sh "./gradlew test"
        } else {
            // Uses Jenkins tool config: tools { gradle '...' }
            script.sh "gradle test"
        }
    }


    void buildJar() {
        script.echo "Building jar with Gradle (Spring Boot bootJar)..."

        if (script.fileExists('gradlew')) {
//            script.sh "chmod +x ./gradlew"
            script.sh "./gradlew clean bootJar"
        } else {
            script.sh "gradle clean bootJar"
        }
    }


}


