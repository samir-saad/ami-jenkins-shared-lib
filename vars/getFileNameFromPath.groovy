#!/usr/bin/groovy

def call(String filePath) {
    return (filePath.tokenize('/').last()).tokenize('.').first()
}
