package com.salzaidy.jenkins.build.tools

class BuildToolDetector implements Serializable {
    def script

    BuildToolDetector(script) {
        this.script = script
    }

    String detect() {
        // Prefer Maven if pom.xml exists (common in multi-build repos)
        if (script.fileExists('pom.xml')) return 'maven'

        // Gradle
        if (script.fileExists('build.gradle') || script.fileExists('build.gradle.kts')) return 'gradle'

        script.error("Cannot detect build tool: no pom.xml or build.gradle(.kts) found in workspace")
    }
}
