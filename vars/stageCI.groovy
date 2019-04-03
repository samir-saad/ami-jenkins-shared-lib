#!/usr/bin/groovy

def call(config) {
	
	stage("build") {
		println "build"
	}
	
	stage("test") {
		println "test"
	}
	
}