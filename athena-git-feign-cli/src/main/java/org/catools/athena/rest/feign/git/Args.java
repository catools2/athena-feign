package org.catools.athena.rest.feign.git;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.IntegerConverter;
import com.beust.jcommander.converters.LongConverter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.catools.athena.rest.feign.common.configs.ConfigUtils;
import org.catools.athena.rest.feign.core.configs.CoreConfigs;

@Data
public class Args {

  @Parameter(names = {"-ah", "-athena-host"},
             description = "The Athena api endpoint to send information to")
  private String athenaHost;

  @Parameter(names = {"-u", "-username"},
             description = "The username to clone repository.")
  private String username = ConfigUtils.getString("athena.git.username");

  @Parameter(names = {"-p", "-password"},
             password = true,
             description = "The password to clone repository.")
  private String password = ConfigUtils.getString("athena.git.password");

  @Parameter(names = {"-n", "-name"},
             description = "The repository name.")
  private String name = ConfigUtils.getString("athena.git.repo_name");

  @Parameter(names = {"-l", "-url"},
             description = "The url to the repository to clone project from.")
  private String url = ConfigUtils.getString("athena.git.repo_url");

  @Parameter(names = {"-lp", "-local-path"},
             description = "The path to the local folder where repository should be clone to.")
  private String localPath = ConfigUtils.getString("athena.git.local_path");

  @Parameter(names = {"-t", "-threads"},
             converter = IntegerConverter.class,
             description = "The number of total threads to use for parallel processing.")
  private int threadsCount = ConfigUtils.getInteger("athena.threads_count", 10);

  @Parameter(names = {"-m", "-timeout-in-minutes"},
             converter = LongConverter.class,
             description = "The total amount of wait for sync to be finished.")
  private Long timeoutInMinutes = ConfigUtils.getLong("athena.timeout", 600L);

  @Parameter(names = "--help",
             help = true)
  private boolean help = false;

  public void loadConfig() {
    if (StringUtils.isNoneBlank(athenaHost)) {
      CoreConfigs.setAthenaHost(athenaHost);
    }
  }

}