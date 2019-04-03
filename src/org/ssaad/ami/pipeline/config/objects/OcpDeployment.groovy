package org.ssaad.ami.pipeline.config.objects

class OcpDeployment extends Stage implements Serializable{

    String project = ""
    String env = ""
    String type = "recreate" //rollout, blue-green, canary, a-b, recreate
    String tag = ""
    String oldTag = ""
    String volumes = ""
    String volumeMounts = ""
    Integer replicas = 1
    String imageTag = "latest"
    String buildVersion = ""
    String routeIPsWhitelist = ""
    List<Template> templates = new ArrayList<>()
    Map<String, String> secretsMap = new HashMap<>()
    Map<String, String> testSecretsMap = new HashMap<>()
    //Stage testing = new Stage()
    AutoScaling autoScaling = new AutoScaling()

    OcpDeployment(){
        super()

        /*this.templates.add(TemplateUtils.getSpringAppDC())

        this.templates.add(TemplateUtils.getExternalRoute())

        this.templates.add(TemplateUtils.getExternalMgmtRoute())*/
    }
}
