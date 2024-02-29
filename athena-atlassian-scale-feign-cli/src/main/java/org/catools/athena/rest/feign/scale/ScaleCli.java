package org.catools.athena.rest.feign.scale;

import com.beust.jcommander.JCommander;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.core.model.ProjectDto;
import org.catools.athena.rest.feign.core.client.CoreClient;
import org.catools.atlassian.etl.scale.ScaleSyncClient;
import org.catools.atlassian.etl.scale.configs.ScaleConfigs;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class ScaleCli {

  public static void main(String[] args) {
    getArgs(args).loadConfig();
    CoreClient.getProject(new ProjectDto(ScaleConfigs.getProjectCode(), ScaleConfigs.getProjectName()));

    ScaleSyncClient.syncTestCases();
    ScaleSyncClient.syncTestRuns();
  }

  @NotNull
  private static Args getArgs(String[] argc) {
    Args cmd = new Args();

    JCommander build = JCommander.newBuilder().programName("Athena Scale Data Loader").acceptUnknownOptions(false).addObject(cmd).build();

    build.setExpandAtSign(false);
    build.parse(argc);

    if (cmd.isHelp()) {
      build.usage();
    }

    return cmd;
  }
}