package org.catools.athena.rest.feign.apispec.configs;

import lombok.Getter;
import lombok.Setter;
import org.catools.athena.rest.feign.apispec.entity.RepoInfoSet;
import org.catools.athena.rest.feign.common.configs.ConfigUtils;
import org.catools.athena.rest.feign.common.utils.JsonUtils;

public class GitConfigs {

  @Setter
  @Getter
  private static String username = ConfigUtils.getString("athena.git.username");

  @Setter
  @Getter
  private static String password = ConfigUtils.getString("athena.git.password");

  @Setter
  @Getter
  private static String name = ConfigUtils.getString("athena.git.repo.name");

  @Setter
  @Getter
  private static String url = ConfigUtils.getString("athena.git.repo.url");

  @Setter
  @Getter
  private static RepoInfoSet repoInfoSet = ConfigUtils.asModel("athena.git.repo.set", RepoInfoSet.class);

  @Setter
  @Getter
  private static String localPath = ConfigUtils.getString("athena.git.local_path", "./tmp/repository/");

  public static void setRepoInfo(String input) {
    repoInfoSet = JsonUtils.readValue(input, RepoInfoSet.class);
  }
}
