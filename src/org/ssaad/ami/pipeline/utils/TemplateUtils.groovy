package org.ssaad.ami.pipeline.utils

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.Template
import org.ssaad.ami.pipeline.common.types.CreationPolicyType

class TemplateUtils implements Serializable {

    static final String QUAY_DOCKERCONFIG_SECRET_TEMPLATE = "quay-dockerconfig-secret-template"
    static final String DOCKERHUB_SECRET_TEMPLATE = "dockerhub-secret-template"
    static final String S2I_BUILD_TEMPLATE = "s2i-build-template"
    static final String SPRING_CLOUD_CONFIG_SERVER_TEMPLATE = "spring-cloud-config-server-template"
    static final String SPRING_BOOT_TEMPLATE = "spring-boot-template"
    static final String SPRING_BOOT_WITH_CLOUD_CONFIG = "spring-boot-with-cloud-config"
    static final String HORIZONTAL_POD_AUTOSCALER = "horizontal-pod-autoscaler"

    @NonCPS
    static Map<String, String> getCommonParams() {
        Map<String, String> params = new HashMap<>()
        params.put("APP_NAME", '${app.id}')
        params.put("APP_VERSION", '${app.version}')
        params.put("NAMESPACE", '${platform.project}')

        return params
    }

    @NonCPS
    static Map<String, String> getDeploymentCommonParams() {
        Map<String, String> params = getCommonParams()
        params.put("OCP_OBJECT_NAME", '${app.id}')
//        params.put("IMAGE_NAME", 'docker.io/samirsaad/${app.id}')
        params.put("IMAGE_NAME", 'quay.io/samir.k.saad/${app.id}')
        params.put("IMAGE_TAG", '${app.version}')
        params.put("REPLICAS", '${deployment.replicas}')

        // Resources
        params.put("MEMORY_REQUEST", '300Mi')
        params.put("MEMORY_LIMIT", '1Gi')
        params.put("CPU_REQUEST", '100m')
        params.put("CPU_LIMIT", '1')

        return params
    }

    @NonCPS
    static Map<String, String> getJavaCommonParams() {
        Map<String, String> params = getDeploymentCommonParams()
        params.put("JAVA_OPTIONS", '-XshowSettings:vm -Xms256m -Xmx900m')

        return params
    }

    @NonCPS
    static Map<String, String> getSpringCommonParams() {
        Map<String, String> params = getJavaCommonParams()
        params.put("APP_PROFILE", '${deployment.environmentType.lowerCase}')

        return params
    }

    @NonCPS
    static Map<String, String> getCommonLabels() {
        Map<String, String> labels = new HashMap<>()
        labels.put("template", '${template.id.lowerCase}')
        // Config repo
        labels.put("configRepo-id", '${configRepo.id.lowerCase}')
        labels.put("configRepo-branch", '${configRepo.branch.normalizeLabel.lowerCase}')
        labels.put("configRepo-commit", '${configRepo.latestCommit}')

        return labels
    }

    @NonCPS
    static Map<String, String> getApplicationLabels() {
        Map<String, String> labels = getCommonLabels()
        labels.put("app", '${app.id.lowerCase}')
        labels.put("app-version", '${app.version.lowerCase}')
        labels.put("app-branch", '${app.branch.normalizeLabel.lowerCase}')
        labels.put("app-commit", '${app.latestCommit}')

        return labels
    }

    @NonCPS
    static Map<String, String> getDeploymentLabels() {
        Map<String, String> labels = getApplicationLabels()
        labels.put("deployment-type", '${deployment.deploymentType.lowerCase}')
        labels.put("environment-type", '${deployment.environmentType.lowerCase}')
        labels.put("deployment-tag", '${deployment.deploymentTag.lowerCase}')

        return labels
    }

    @NonCPS
    static Template getQuayDockerconfigSecretTemplate() {
        Template template = new Template()
        template.id = QUAY_DOCKERCONFIG_SECRET_TEMPLATE
        template.name = QUAY_DOCKERCONFIG_SECRET_TEMPLATE
        template.type = "secret"
        template.creationPolicy = CreationPolicyType.ENFORCE_RECREATE
        template.filePath = "/deploy/openshift/templates/quay-dockerconfig-secret-template.yaml"

        template.params = new HashMap<>()
        template.params.put("OCP_OBJECT_NAME", '${engine.ocpSecretId}')
        template.params.put("TOKEN", '')

        template.labels = getCommonLabels()

        return template
    }

    @NonCPS
    static Template getDockerHubSecretTemplate() {
        Template template = new Template()
        template.id = DOCKERHUB_SECRET_TEMPLATE
        template.name = DOCKERHUB_SECRET_TEMPLATE
        template.type = "secret"
        template.creationPolicy = CreationPolicyType.ENFORCE_RECREATE
        template.filePath = "/deploy/openshift/templates/dockerhub-secret-template.yaml"

        template.params = new HashMap<>()
        template.params.put("OCP_OBJECT_NAME", '${engine.ocpSecretId}')
        template.params.put("TOKEN", '')

        template.labels = getCommonLabels()

        return template
    }

    @NonCPS
    static Template getS2iBuildTemplate() {
        Template template = new Template()
        template.id = S2I_BUILD_TEMPLATE
        template.name = S2I_BUILD_TEMPLATE
        template.type = "bc"
        template.creationPolicy = CreationPolicyType.CREATE_IF_NOT_EXIST
        template.filePath = "/build/s2i/build-template.yaml"
        template.params = getCommonParams()
        template.params.put("BUILD_NAME", '${app.id}-${app.version}.lowerCase.normalizeName')
//        template.params.put("IMAGE_NAME", 'docker.io/samirsaad/${app.id}')
        template.params.put("IMAGE_NAME", 'quay.io/samir.k.saad/${app.id}')
        template.params.put("IMAGE_TAG", '${app.version}')
        template.params.put("INPUT_STREAM_NAME", '${engine.imageStream.name}')
        template.params.put("INPUT_STREAM_TAG", '${engine.imageStream.tag}')
        template.params.put("INPUT_STREAM_IMAGE", '${engine.imageStream.image}')

        template.labels = getApplicationLabels()

        return template
    }

    @NonCPS
    static Template getSpringCloudConfigServerTemplate() {
        Template template = new Template()
        template.id = SPRING_CLOUD_CONFIG_SERVER_TEMPLATE
        template.name = SPRING_CLOUD_CONFIG_SERVER_TEMPLATE
        template.type = "dc"
        template.creationPolicy = CreationPolicyType.ENFORCE_RECREATE
        template.filePath = "/deploy/openshift/templates/spring-cloud-config-server-template.yaml"
        template.params = getSpringCommonParams()
        template.params.put("GIT_URI", 'https://github.com/samir-saad/ami-apps-configs.git')
        template.params.put("GIT_USERNAME", '')
        template.params.put("GIT_PASSWORD", '')
        template.params.put("APP_DOMAIN", 'AMI')
        template.params.put("APP_LOB", 'AMI')

        template.labels = getDeploymentLabels()

        return template
    }

    @NonCPS
    static Template getSpringBootTemplate() {
        Template template = new Template()
        template.id = SPRING_BOOT_TEMPLATE
        template.name = SPRING_BOOT_TEMPLATE
        template.type = "dc"
        template.creationPolicy = CreationPolicyType.ENFORCE_RECREATE
        template.filePath = "/deploy/openshift/templates/spring-boot-template.yaml"
        template.params = getSpringCommonParams()

        template.labels = getDeploymentLabels()

        return template
    }

    @NonCPS
    static Template getSpringBootWithCloudConfigTemplate() {
        Template template = new Template()
        template.id = SPRING_BOOT_WITH_CLOUD_CONFIG
        template.name = SPRING_BOOT_WITH_CLOUD_CONFIG
        template.type = "dc"
        template.creationPolicy = CreationPolicyType.ENFORCE_RECREATE
        template.filePath = "/deploy/openshift/templates/spring-boot-with-cloud-config-template.yaml"
        template.params = getSpringCommonParams()
        template.params.put("CONFIG_SERVER_URI", 'http://spring-config-server:8080')
        template.params.put("CONFIGS_BRANCH", '${deployment.environmentType.lowerCase}')

        template.labels = getDeploymentLabels()

        return template
    }

    @NonCPS
    static Template getHorizontalPodAutoscalerTemplate() {
        Template template = new Template()
        template.id = HORIZONTAL_POD_AUTOSCALER
        template.name = HORIZONTAL_POD_AUTOSCALER
        template.type = "hpa"
        template.creationPolicy = CreationPolicyType.ENFORCE_RECREATE
        template.filePath = "/deploy/openshift/templates/horizontal-pod-autoscaler-template.yaml"

        template.params = new HashMap<>()
        template.params.put("OCP_OBJECT_NAME", '${app.id}')
        template.params.put("MIN_REPLICAS", '${deployment.autoScaling.minReplicas}')
        template.params.put("MAX_REPLICAS", '${deployment.autoScaling.maxReplicas}')
        template.params.put("RESOURCE_TYPE", '${deployment.autoScaling.resourceType.lowerCase}')
        template.params.put("AVG_UTILIZATION", '${deployment.autoScaling.thresholdPercentage}')

        template.labels = getDeploymentLabels()

        return template
    }

    @NonCPS
    static Template getTemplateById(String id) {
        if (QUAY_DOCKERCONFIG_SECRET_TEMPLATE.equals(id))
            return getQuayDockerconfigSecretTemplate()

        if (DOCKERHUB_SECRET_TEMPLATE.equals(id))
            return getDockerHubSecretTemplate()

        if (S2I_BUILD_TEMPLATE.equals(id))
            return getS2iBuildTemplate()

        if (SPRING_CLOUD_CONFIG_SERVER_TEMPLATE.equals(id))
            return getSpringCloudConfigServerTemplate()

        if (SPRING_BOOT_TEMPLATE.equals(id))
            return getSpringBootTemplate()

        if (SPRING_BOOT_WITH_CLOUD_CONFIG.equals(id))
            return getSpringBootWithCloudConfigTemplate()

        if (HORIZONTAL_POD_AUTOSCALER.equals(id))
            return getHorizontalPodAutoscalerTemplate()

        return new Template()
    }
}
