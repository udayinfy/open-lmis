/*
 * This program was produced for the U.S. Agency for International Development. It was prepared by the USAID | DELIVER PROJECT, Task Order 4. It is part of a project which utilizes code originally licensed under the terms of the Mozilla Public License (MPL) v2 and therefore is licensed under MPL v2 or later.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the Mozilla Public License as published by the Mozilla Foundation, either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Mozilla Public License for more details.
 *
 * You should have received a copy of the Mozilla Public License along with this program. If not, see http://www.mozilla.org/MPL/
 */
package org.openlmis.report.builder;

import org.openlmis.report.model.params.FacilityReportParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.facilityTypeIsFilteredBy;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.programIsFilteredBy;

public class FacilityReportQueryBuilder {

  public static String getQuery(Map params) {

    FacilityReportParam filter = (FacilityReportParam) params.get("filterCriteria");
    Long userId = (Long) params.get("userId");

    BEGIN();
    SELECT("DISTINCT F.id, F.code, F.name, F.active as active, FT.name as facilityType, GZ.district_name as region, FO.code as owner,F.latitude::text ||',' ||  F.longitude::text  ||', ' || F.altitude::text gpsCoordinates,F.mainphone as phoneNumber, F.fax as fax ");
    FROM("facilities F");
    JOIN("facility_types FT on FT.id = F.typeid");
    LEFT_OUTER_JOIN("programs_supported ps on ps.facilityid = F.id");
    LEFT_OUTER_JOIN("vw_districts GZ on GZ.district_id = F.geographiczoneid");
    LEFT_OUTER_JOIN("facility_operators FO on FO.id = F.operatedbyid");
    WHERE("F.geographicZoneId in (select distinct district_id from vw_user_facilities where user_id = " + userId + " )");
    if (filter.getStatus() != null) {
      WHERE("F.active = " + filter.getStatus().toString());
    }
    if (filter.getZone() != 0) {
      WHERE("( F.geographicZoneId = #{filterCriteria.zone} or GZ.region_id = #{filterCriteria.zone} or GZ.zone_id = #{filterCriteria.zone} or GZ.parent = #{filterCriteria.zone} ) ");
    }
    if (filter.getFacilityType() != 0) {
      WHERE(facilityTypeIsFilteredBy("F.typeId"));
    }
    if (filter.getProgram() != 0) {
      WHERE(programIsFilteredBy("ps.programId"));
    }
    return SQL();
  }

}
