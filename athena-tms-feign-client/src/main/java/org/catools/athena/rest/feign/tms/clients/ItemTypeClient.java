package org.catools.athena.rest.feign.tms.clients;

import feign.*;
import org.catools.athena.tms.model.ItemTypeDto;

interface ItemTypeClient {

  @RequestLine("GET /tms/itemType?keyword={keyword}")
  ItemTypeDto search(
      @Param("keyword")
      String keyword);

  @RequestLine("POST /tms/itemType")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(ItemTypeDto itemType);

}
