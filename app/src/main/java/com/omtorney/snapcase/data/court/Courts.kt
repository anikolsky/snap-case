package com.omtorney.snapcase.data.court

import com.omtorney.snapcase.data.page.PageType

sealed class Courts {

    // NoMSK
    object Dmitrov : Court, Courts() {

        override val type = PageType.NoMsk
        override val basicUrl = "https://dmitrov--mo.sudrf.ru"

        override fun getScheduleQuery(date: String): String {
            return if (date.isEmpty()) "$basicUrl/modules.php?name=sud_delo"
            else "$basicUrl/modules.php?name=sud_delo&srv_num=1&H_date=$date"
        }

        override fun getSearchQuery(sideName: String, caseNumber: String) = basicUrl +
                "/modules.php?name=sud_delo" +
                "&srv_num=1" +
                "&name_op=r" +
                "&delo_id=1540005" +
                "&case_type=0" +
                "&new=0" +
                "&G1_PARTS__NAMESS=" + sideName +
                "&g1_case__CASE_NUMBERSS=" + caseNumber +
                "&g1_case__JUDICIAL_UIDSS=" +
                "&delo_table=g1_case" +
                "&g1_case__ENTRY_DATE1D=" +
                "&g1_case__ENTRY_DATE2D=" +
                "&G1_CASE__JUDGE=" +
                "&g1_case__RESULT_DATE1D=" +
                "&g1_case__RESULT_DATE2D=" +
                "&G1_CASE__RESULT=" +
                "&G1_CASE__BUILDING_ID=" +
                "&G1_CASE__COURT_STRUCT=" +
                "&G1_EVENT__EVENT_NAME=" +
                "&G1_EVENT__EVENT_DATEDD=" +
                "&G1_PARTS__PARTS_TYPE=" +
                "&G1_PARTS__INN_STRSS=" +
                "&G1_PARTS__KPP_STRSS=" +
                "&G1_PARTS__OGRN_STRSS=" +
                "&G1_PARTS__OGRNIP_STRSS=" +
                "&g1_requirement__ACCESSION_DATE1D=" +
                "&g1_requirement__ACCESSION_DATE2D=" +
                "&G1_REQUIREMENT__CATEGORY=" +
                "&g1_requirement__ESSENCESS=" +
                "&g1_requirement__JOIN_END_DATE1D=" +
                "&g1_requirement__JOIN_END_DATE2D=" +
                "&G1_REQUIREMENT__PUBLICATION_ID=" +
                "&G1_DOCUMENT__PUBL_DATE1D=" +
                "&G1_DOCUMENT__PUBL_DATE2D=" +
                "&G1_CASE__VALIDITY_DATE1D=" +
                "&G1_CASE__VALIDITY_DATE2D=" +
                "&G1_ORDER_INFO__ORDER_DATE1D=" +
                "&G1_ORDER_INFO__ORDER_DATE2D=" +
                "&G1_ORDER_INFO__ORDER_NUMSS=" +
                "&G1_ORDER_INFO__STATE_ID=" +
                "&G1_ORDER_INFO__RECIP_ID="
    }

    // MSK
    object Dorogomilovskij : Court, Courts() {

        override val type = PageType.Msk
        override val basicUrl = "https://mos-gorsud.ru/rs/dorogomilovskij"

        override fun getScheduleQuery(date: String): String {
            TODO("Not yet implemented")
        }

        override fun getSearchQuery(sideName: String, caseNumber: String): String {
            TODO("Not yet implemented")
        }
    }
}
