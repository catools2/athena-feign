package org.catools.athena.rest.feign.kube;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.IntegerConverter;
import com.beust.jcommander.converters.LongConverter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;
import org.catools.athena.rest.feign.kube.configs.KubeConfigs;

import java.util.List;

@Data
public class Args {

  @Parameter(names = {"-ah", "-athena-host"},
      description = "The Athena api endpoint to send information to")
  private String athenaHost;

  @Parameter(names = {"-pn", "-project-name"},
      description = "The unique project name to use for project identification")
  private String projectName;

  @Parameter(names = {"-pc", "-project-code"},
      description = "The unique project code to use for project identification")
  private String projectCode;

  @Parameter(names = {"-t", "-threads"},
      converter = IntegerConverter.class,
      description = "The number of total threads to use for parallel processing.")
  private Integer threadsCount;

  @Parameter(names = {"-m", "-timeout-in-minutes"},
      converter = LongConverter.class,
      description = "The total amount of wait for sync to be finished.")
  private Long timeoutInMinutes;

  @Parameter(names = {"-ns", "-namespaces"},
      description = "The namespaces to read data from.")
  private List<String> namespaces;

  @Parameter(names = {"-ct", "-connection-type"},
      description = "The connection type to be used for kubernetes interaction. [DEFAULT, URL, CREDENTIAL, TOKEN, CONFIG]")
  private static String connectionType;

  @Parameter(names = {"-s", "-ssl"},
      description = "If connection should use SSL validation.")
  private static Boolean shouldValidateSSL;

  @Parameter(names = {"-l", "-connection-url"},
      description = "The connection url.")
  private static String connectionUrl;

  @Parameter(names = {"-u", "-username"},
      description = "The username to be used for connection.")
  private static String connectionUsername;

  @Parameter(names = {"-p", "-password"},
      description = "The password to be used for connection.")
  private static String connectionPassword;

  @Parameter(names = {"-tk", "-connection-token"},
      description = "The token to be used for connection.")
  private static String connectionToken;

  @Parameter(names = {"-f", "-config-file"},
      description = "The path to the config file location to be used for connection.")
  private static String kubeConfigPath;

  @Parameter(names = "--help",
      help = true)
  private boolean help = false;

  public void loadConfig() {
    if (StringUtils.isNoneBlank(athenaHost)) {
      CoreConfigs.setAthenaHost(athenaHost);
    }

    if (StringUtils.isNoneBlank(projectName)) {
      CoreConfigs.setProjectName(projectName);
    }

    if (StringUtils.isNoneBlank(projectCode)) {
      CoreConfigs.setProjectCode(projectCode);
    }

    if (threadsCount != null) {
      CoreConfigs.setThreadsCount(threadsCount);
    }

    if (timeoutInMinutes != null) {
      CoreConfigs.setTimeoutInMinutes(timeoutInMinutes);
    }

    if (namespaces != null && namespaces.isEmpty()) {
      KubeConfigs.setNamespaces(namespaces);
    }

    if (StringUtils.isNoneBlank(connectionType)) {
      KubeConfigs.setConnectionType(connectionType);
    }

    if (shouldValidateSSL != null) {
      KubeConfigs.setShouldValidateSSL(shouldValidateSSL);
    }

    if (StringUtils.isNoneBlank(connectionUrl)) {
      KubeConfigs.setConnectionUrl(connectionUrl);
    }

    if (StringUtils.isNoneBlank(connectionUsername)) {
      KubeConfigs.setConnectionUsername(connectionUsername);
    }

    if (StringUtils.isNoneBlank(connectionPassword)) {
      KubeConfigs.setConnectionPassword(connectionPassword);
    }

    if (StringUtils.isNoneBlank(connectionToken)) {
      KubeConfigs.setConnectionToken(connectionToken);
    }

    if (StringUtils.isNoneBlank(kubeConfigPath)) {
      KubeConfigs.setKubeConfigPath(kubeConfigPath);
    }

  }
}