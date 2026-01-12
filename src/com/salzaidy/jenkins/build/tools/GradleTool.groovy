package com.salzaidy.jenkins.build.tools

class GradleTool implements Serializable {
    def script
    GradleTool(script) {
        this.script = script
    }

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


    String projectName() {
        def settings = script.readFile('settings.gradle')
        def nameMatcher = (settings =~ /rootProject\.name\s*=\s*['"](.+)['"]/)
        def name = nameMatcher.find() ? nameMatcher.group(1).trim() : "unknown-project"
        nameMatcher = null
        return name
    }

    String bumpPatchSnapshot() {
        def gradleFile = script.readFile('build.gradle')

        def versionMatcher = (gradleFile =~ /version\s*=\s*'(.+)'/)
        def version = versionMatcher.find() ? versionMatcher.group(1).trim() : "0.0.1-SNAPSHOT"
        versionMatcher = null

        script.echo "Raw version in build.gradle is: ${version}"

        def baseVersion = version.replace('-SNAPSHOT', '')
        def parts = baseVersion.tokenize('.')
        if (parts.size() != 3) {
            script.error("Version '${baseVersion}' is not in MAJOR.MINOR.PATCH format")
        }

        def major = parts[0].toInteger()
        def minor = parts[1].toInteger()
        def patch = parts[2].toInteger() + 1

        def newVersion = "${major}.${minor}.${patch}-SNAPSHOT"
        script.echo "New Gradle project version will be: ${newVersion}"

        def updatedGradleFile = gradleFile.replaceFirst(/version\s*=\s*'.+'/, "version = '${newVersion}'")
        script.writeFile file: 'build.gradle', text: updatedGradleFile

        return newVersion
    }

}


