package org.catools.athena.rest.feign.git.helpers;

import lombok.extern.slf4j.Slf4j;
import org.catools.athena.rest.feign.git.model.RepositoryInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
class GitLoaderTest {

  @Test
  void loadRepository() throws GitAPIException, IOException, InterruptedException {
    String localPath = "./target/repository/selenium" + Instant.now();
    String repoName = "selenium";
    String repoUrl = "https://github.com/SeleniumHQ/selenium.git";

    Git git = GitCloneClient.clone(localPath, repoName, repoUrl);
    RepositoryInfo repositoryInfo = new RepositoryInfo(git, repoName, repoUrl);
    repositoryInfo.readRepository(50, 2, TimeUnit.HOURS);

    GitLoader.persistRepository(repositoryInfo);
  }
}