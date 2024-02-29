package org.catools.athena.rest.feign.apispec;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.URLConverter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;

import java.net.URL;

@Data
public class Args {

  @Parameter(names = {"-ah", "-athena-host"},
             description = "The Athena api endpoint to send information to")
  private String athenaHost;

  @Parameter(names = {"-n", "-name"},
             description = "The Open Api Spec Name")
  private String name;

  @Parameter(names = {"-l", "-url"},
             converter = URLConverter.class,
             description = "The url to the Open Api spec in json format")
  private URL url;

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
  }
}