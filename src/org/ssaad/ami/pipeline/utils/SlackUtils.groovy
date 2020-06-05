package org.ssaad.ami.pipeline.utils

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.common.types.MessageType

class SlackUtils {

    @NonCPS
    static void send(MessageType messageType, String message, String buildId, boolean appendBuildInfo) {
        send(messageType, message, null, buildId, appendBuildInfo)
    }

    @NonCPS
    static void send(MessageType messageType, String message, String channel, String buildId, boolean appendBuildInfo) {
        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)
        def steps = pipeline.steps

        if (appendBuildInfo) {
            message = message +
                    "\n Job: ${pipeline.env.JOB_NAME}" +
                    "\n <${pipeline.env.RUN_DISPLAY_URL}|Build #${pipeline.env.BUILD_NUMBER}>  |  <${pipeline.env.BUILD_URL}changes|Changes>  |  <${pipeline.env.BUILD_URL}console|Logs>"
        }

        String messageColor
        switch (messageType) {
            case MessageType.INFO:
                messageColor = "46c9e2"
                break
            case MessageType.SUCCESS:
                messageColor = "good"
                break
            case MessageType.WARNING:
                messageColor = "warning"
                break
            case [MessageType.ERROR, MessageType.FAILURE]:
                messageColor = "danger"
                break
            default:
                messageColor = "46c9e2"
                break;
        }

        if (channel?.trim())
            steps.slackSend color: messageColor, message: message, channel: channel
        else
            steps.slackSend color: messageColor, message: message
    }
}
