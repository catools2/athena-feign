package org.catools.athena.rest.feign.apispec.helpers;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.rest.feign.apispec.exception.GitClientException;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

@Slf4j
@UtilityClass
public class GitCloneClient {

  /**
   * Clone repository from specified url to the directory in storage with repository name.
   *
   * @param name repository/directory name
   * @param url  the source repository to clone
   * @return JGit client to work with
   */
  public static Git clone(String localPath, String name, String url) {
    return clone(localPath, name, url, Git.cloneRepository().setURI(url));
  }

  /**
   * Clone repository from specified url to the directory in storage with repository name.
   *
   * @param name     repository/directory name
   * @param url      the source repository to clone
   * @param username the git user
   * @param password the password for the git user
   * @return JGit client to work with
   */
  public static Git clone(String localPath, String name, String url, String username, String password) {
    CloneCommand command = Git.cloneRepository().setURI(url).setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
    return clone(localPath, name, url, command);
  }

  private static synchronized Git clone(String localPath, String name, String url, CloneCommand command) {
    File gitDir = new File(localPath);

    try {
      log.info("Start cloning {} repository from {}.", name, url);
      return command.setDirectory(gitDir).call();
    }
    catch (GitAPIException e) {
      throw new GitClientException("Failed to clone the git repository", e);
    }
    finally {
      log.info("Finish cloning {} repository from {}.", name, url);
    }
  }
}
