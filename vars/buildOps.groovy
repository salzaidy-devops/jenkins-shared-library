#! /user/bin/env groovy
import com.salzaidy.jenkins.build.BuildOps


def test() {
    new BuildOps(this).test()
}

def buildJar() {
    new BuildOps(this).buildJar()
}

/*

def bumpVersionAndPrepareImage() {
    new BuildOps(this).setupImageNameAndBumpVersion()
}

buildOps.bumpVersionAndPrepareImage()

 */