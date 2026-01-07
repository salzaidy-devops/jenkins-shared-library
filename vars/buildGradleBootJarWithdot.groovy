#! /user/bin/env groovy

def call() {
    echo "building the application for branch $BRANCH_NAME"
    sh "./gradlew bootJar" // using wrapper version of gradle
}