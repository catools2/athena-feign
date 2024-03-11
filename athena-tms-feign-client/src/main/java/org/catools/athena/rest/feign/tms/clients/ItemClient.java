package org.catools.athena.rest.feign.tms.clients;

import feign.*;
import org.catools.athena.tms.model.ItemDto;

interface ItemClient {

  @RequestLine("GET /tms/item?keyword={keyword}")
  ItemDto search(
      @Param("keyword")
      String keyword);

  @RequestLine("POST /tms/item")
  @Headers("Content-Type: application/json")
  void saveOrUpdate(ItemDto item);

}
