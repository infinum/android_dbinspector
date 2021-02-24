package com.infinum.dbinspector.domain

import com.infinum.dbinspector.domain.shared.base.BaseControl

internal interface Control {

    interface Database : BaseControl<Mappers.DatabaseDescriptor, Converters.Database>

    interface Connection : BaseControl<Mappers.Connection, Converters.Connection>

    interface Content : BaseControl<Mappers.Content, Converters.Content>

    interface Pragma : BaseControl<Mappers.Pragma, Converters.Pragma>

    interface Settings : BaseControl<Mappers.Settings, Converters.Settings>

    interface History : BaseControl<Mappers.History, Converters.History>
}
