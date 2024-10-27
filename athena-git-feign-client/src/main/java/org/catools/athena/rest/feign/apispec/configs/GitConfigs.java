package org.catools.athena.rest.feign.apispec.configs;

import lombok.Getter;
import lombok.Setter;
import org.catools.athena.rest.feign.apispec.entity.RepoInfoSet;
import org.catools.athena.rest.feign.common.configs.ConfigUtils;
import org.catools.athena.rest.feign.common.utils.JsonUtils;

public class GitConfigs {

  static {
    reload();
  }

  @Setter
  @Getter
  private static String username;

  @Setter
  @Getter
  private static String password;

  @Setter
  @Getter
  private static String name;

  @Setter
  @Getter
  private static String url;

  @Setter
  @Getter
  private static RepoInfoSet repoInfoSet;

  @Setter
  @Getter
  private static String localPath;

  public static void setRepoInfo(String input) {
    repoInfoSet = JsonUtils.readValue(input, RepoInfoSet.class);
  }

  public static void reload() {
    username = ConfigUtils.getString("athena.git.username");
    password = ConfigUtils.getString("athena.git.password");
    name = ConfigUtils.getString("athena.git.repo.name");
    url = ConfigUtils.getString("athena.git.repo.url");
    repoInfoSet = ConfigUtils.asModel("athena.git.repo.set", RepoInfoSet.class);
    localPath = ConfigUtils.getString("athena.git.local_path", "./tmp/repository/");
  }
}
