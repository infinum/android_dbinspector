package com.infinum.dbinspector.domain

import com.infinum.dbinspector.domain.shared.base.BaseControl

internal interface Control {

    interface Database : BaseControl<Mappers.DatabaseDescriptor, Converters.Database>

    interface Connection : BaseControl<Mappers.Connection, Converters.Connection>

    interface Schema : BaseControl<Mappers.Schema, Converters.Schema>

    interface Pragma : BaseControl<Mappers.Pragma, Converters.Pragma>

    interface Settings : BaseControl<Mappers.Settings, Converters.Settings>
}
