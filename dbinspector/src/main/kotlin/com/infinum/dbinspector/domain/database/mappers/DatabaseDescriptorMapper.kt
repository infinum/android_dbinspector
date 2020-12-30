package com.infinum.dbinspector.domain.database.mappers

import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import java.io.File

internal class DatabaseDescriptorMapper : Mappers.DatabaseDescriptor {

    override suspend fun invoke(model: File): DatabaseDescriptor =
        DatabaseDescriptor(
            exists = model.exists(),
            parentPath = model.parentFile?.absolutePath.orEmpty(),
            name = model.nameWithoutExtension,
            extension = model.extension
        )
}
