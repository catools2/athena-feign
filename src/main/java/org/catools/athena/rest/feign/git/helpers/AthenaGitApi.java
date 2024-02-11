package org.catools.athena.rest.feign.git.helpers;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.catools.athena.core.model.UserDto;
import org.catools.athena.git.model.CommitDto;
import org.catools.athena.git.model.GitRepositoryDto;
import org.catools.athena.rest.feign.common.utils.FeignUtils;
import org.catools.athena.rest.feign.core.client.UserClient;
import org.catools.athena.rest.feign.git.client.CommitClient;
import org.catools.athena.rest.feign.git.client.RepositoryClient;

import java.util.ArrayList;
import java.util.List;

import static org.catools.athena.rest.feign.common.utils.FeignUtils.getEntityId;

@Slf4j
@UtilityClass
public class AthenaGitApi {
  private static final List<String> ADDED_USERS = new ArrayList<>();
  private static final RepositoryClient repositoryClient = FeignUtils.getClient(RepositoryClient.class);
  private static final UserClient userClient = FeignUtils.getClient(UserClient.class);
  private static final CommitClient commitClient = FeignUtils.getClient(CommitClient.class);

  public static void persistRepository(GitRepositoryDto repository) {
    getEntityId(repositoryClient.save(repository)).ifPresent(repository::setId);
  }

  public static void persistCommit(CommitDto c) {
    getEntityId(commitClient.save(c)).ifPresent(c::setId);
  }

  public static void persistUser(UserDto u) {
    String username = u.getUsername();
    if (!ADDED_USERS.contains(username) && userClient.search(username) == null) {
      getEntityId(userClient.save(u)).ifPresent(u::setId);
      ADDED_USERS.add(username);
    }
  }
}
