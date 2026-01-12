package com.salzaidy.jenkins.build.tools

class MavenTool implements Serializable {
    def script
    MavenTool(script) { this.script = script }

    void test() {
        script.echo "Testing with Maven..."
        script.sh "mvn -q test"
    }

    void buildJar() {
        script.echo "Building jar with Maven (package)..."
        script.sh "mvn -q -DskipTests package"
    }


}
