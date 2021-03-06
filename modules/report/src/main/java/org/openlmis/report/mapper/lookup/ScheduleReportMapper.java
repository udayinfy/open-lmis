/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openlmis.report.mapper.lookup;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.openlmis.report.model.dto.Schedule;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScheduleReportMapper {

  @Select("SELECT id, name, description, code " +
          "   FROM " +
          "       processing_schedules order by name")
  List<Schedule> getAll();

  @Select("SELECT id, name, description, code " +
    "   FROM " +
    "       processing_schedules s " +
    "   where s.id in " +
    "         (select scheduleId from requisition_group_program_schedules where programId = #{programId}) " +
    "     order by name")
  List<Schedule> getSchedulesForProgram(@Param("programId")long programId);
}
