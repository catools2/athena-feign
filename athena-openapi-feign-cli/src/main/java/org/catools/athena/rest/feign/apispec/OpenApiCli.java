package org.catools.athena.rest.feign.apispec;

import com.beust.jcommander.JCommander;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.core.model.ProjectDto;
import org.catools.athena.rest.feign.apispec.helpers.ApiSpecLoader;
import org.catools.athena.rest.feign.core.client.CoreClient;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


@Slf4j
public class OpenApiCli {

  public static void main(String[] argc) throws IOException {
    Args args = getArgs(argc);
    args.loadConfig();

    CoreClient.getProject(new ProjectDto(args.getProjectCode(), args.getProjectName()));
    ApiSpecLoader.saveOpenApi(args.getName(), args.getUrl(), args.getProjectCode());
  }

  @NotNull
  private static Args getArgs(String[] args) {
    Args cmd = new Args();
    JCommander build = JCommander.newBuilder().addObject(cmd).programName("Athena OpenApi Data Loader").build();

    build.setExpandAtSign(false);
    build.parse(args);

    if (cmd.isHelp()) {
      build.usage();
    }

    return cmd;
  }
}