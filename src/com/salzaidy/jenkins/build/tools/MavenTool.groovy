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


    String projectName() {
        return script.sh(returnStdout: true, script: "mvn -q -Dexpression=project.artifactId -DforceStdout help:evaluate").trim()
    }

    String bumpPatchSnapshot() {
        script.echo "Incrementing Maven patch version (keeping -SNAPSHOT)..."

        script.sh """
      mvn -q build-helper:parse-version versions:set \
        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion}-SNAPSHOT \
        versions:commit
    """

        def newVersion = script.sh(returnStdout: true, script: "mvn -q -Dexpression=project.version -DforceStdout help:evaluate").trim()
        script.echo "New Maven project version will be: ${newVersion}"
        return newVersion
    }

}
