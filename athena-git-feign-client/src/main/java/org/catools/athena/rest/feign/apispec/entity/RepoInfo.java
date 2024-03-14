package org.catools.athena.rest.feign.apispec.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepoInfo {
  private String name;

  private String url;
}
