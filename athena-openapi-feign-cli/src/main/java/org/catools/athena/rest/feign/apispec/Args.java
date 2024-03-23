package org.catools.athena.rest.feign.apispec;

import com.beust.jcommander.Parameter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.rest.feign.apispec.configs.OpenApiConfigs;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;

import java.util.List;

@Data
public class Args {

  @Parameter(names = {"-ah", "-athena-host"},
      description = "The Athena api endpoint to send information to")
  private String athenaHost;

  @Parameter(names = {"-n", "-names"},
      description = "The Open Api Spec Names")
  private List<String> specNames;

  @Parameter(names = {"-l", "-urls"},
      description = "The urls to the Open Api spec json file")
  private List<String> specUrls;

  @Parameter(names = {"-s", "-spec-info"},
      description = "Set of Open Api Spec name and url in json format i.e. [{\"name\": \"...\",\"url\": \"...\"}]")
  private String specInfoSet;

  @Parameter(names = {"-pn", "-project-name"},
      description = "The unique project name to use for project identification")
  private String projectName;

  @Parameter(names = {"-pc", "-project-code"},
      description = "The unique project code to use for project identification")
  private String projectCode;

  @Parameter(names = "--help",
      help = true)
  private boolean help = false;

  public void loadConfig() {
    if (StringUtils.isNoneBlank(athenaHost)) {
      CoreConfigs.setAthenaHost(athenaHost);
    }
    if (StringUtils.isNoneBlank(projectCode)) {
      CoreConfigs.setProjectCode(projectCode);
    }
    if (StringUtils.isNoneBlank(projectName)) {
      CoreConfigs.setProjectName(projectName);
    }
    if (specNames != null && !specNames.isEmpty()) {
      OpenApiConfigs.setSpecNames(specNames);
    }
    if (specUrls != null && !specUrls.isEmpty()) {
      OpenApiConfigs.setSpecUrls(specUrls);
    }
    if (StringUtils.isNoneBlank(specInfoSet)) {
      OpenApiConfigs.setSpecInfo(specInfoSet);
    }
  }
}