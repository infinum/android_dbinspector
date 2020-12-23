package com.infinum.dbinspector.domain

import com.infinum.dbinspector.domain.shared.mappers.BaseCellMapper

internal interface Mappers {

    interface PragmaCell : BaseCellMapper

    interface SchemaCell : BaseCellMapper
}
