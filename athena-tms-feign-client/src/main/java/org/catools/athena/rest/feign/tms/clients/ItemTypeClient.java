package org.catools.athena.rest.feign.tms.clients;

import feign.*;
import org.catools.athena.tms.model.ItemTypeDto;

interface ItemTypeClient {

  @RequestLine("GET /tms/itemType?code={code}")
  ItemTypeDto getByCode(
      @Param("code")
      String code);

  @RequestLine("POST /tms/itemType")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(ItemTypeDto itemType);

}
