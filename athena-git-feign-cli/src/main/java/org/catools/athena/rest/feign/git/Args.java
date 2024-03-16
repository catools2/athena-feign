package org.catools.athena.rest.feign.git;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.IntegerConverter;
import com.beust.jcommander.converters.LongConverter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.rest.feign.apispec.configs.GitConfigs;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;

@Data
public class Args {

  @Parameter(names = {"-ah", "-athena-host"},
      description = "The Athena api endpoint to send information to")
  private String athenaHost;

  @Parameter(names = {"-u", "-username"},
      description = "The username to clone repository.")
  private String username;

  @Parameter(names = {"-p", "-password"},
      password = true,
      description = "The password to clone repository.")
  private String password;

  @Parameter(names = {"-n", "-name"},
      description = "The repository name.")
  private String name;

  @Parameter(names = {"-l", "-url"},
      description = "The url to the repository to clone project from.")
  private String url;

  @Parameter(names = {"-r", "-repo-info"},
      description = "Set of repositories name and url in json format i.e. [{\"name\": \"...\",\"url\": \"...\"}]")
  private String repoInfoSet;

  @Parameter(names = {"-lp", "-local-path"},
      description = "The path to the local folder where repository should be clone to.")
  private String localPath;

  @Parameter(names = {"-t", "-threads"},
      converter = IntegerConverter.class,
      description = "The number of total threads to use for parallel processing.")
  private Integer threadsCount;

  @Parameter(names = {"-m", "-timeout-in-minutes"},
      converter = LongConverter.class,
      description = "The total amount of wait for sync to be finished.")
  private Long timeoutInMinutes;

  @Parameter(names = "--help",
      help = true)
  private boolean help = false;

  public void loadConfig() {
    if (StringUtils.isNoneBlank(athenaHost)) {
      CoreConfigs.setAthenaHost(athenaHost);
    }
    if (threadsCount != null) {
      CoreConfigs.setThreadsCount(threadsCount);
    }
    if (timeoutInMinutes != null) {
      CoreConfigs.setTimeoutInMinutes(timeoutInMinutes);
    }
    if (StringUtils.isNoneBlank(username)) {
      GitConfigs.setUsername(username);
    }
    if (StringUtils.isNoneBlank(password)) {
      GitConfigs.setPassword(password);
    }
    if (StringUtils.isNoneBlank(name)) {
      GitConfigs.setName(name);
    }
    if (StringUtils.isNoneBlank(url)) {
      GitConfigs.setUrl(url);
    }
    if (StringUtils.isNoneBlank(localPath)) {
      GitConfigs.setLocalPath(localPath);
    }
    if (StringUtils.isNoneBlank(repoInfoSet)) {
      GitConfigs.setRepoInfo(repoInfoSet);
    }
  }

}