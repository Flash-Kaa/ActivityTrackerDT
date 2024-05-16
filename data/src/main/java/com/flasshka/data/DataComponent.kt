package com.flasshka.data

import com.flasshka.data.api.RequestsImpl
import com.flasshka.data.db.DbImpl
import dagger.Component


@Component
interface DataComponent {
    fun getApi(): RequestsImpl
    fun getDb(): DbImpl
}
