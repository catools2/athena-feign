package org.catools.athena.rest.feign.tms.clients;

import feign.*;
import org.catools.athena.tms.model.SyncInfoDto;

interface SyncInfoClient {
  @RequestLine("GET /tms/syncInfo?action={action}&component={component}&project={projectCode}")
  SyncInfoDto search(
      @Param("action")
      String action,
      @Param("component")
      String component,
      @Param("projectCode")
      String projectCode);

  @RequestLine("POST /tms/syncInfo")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(SyncInfoDto syncInfo);
}
