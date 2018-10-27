package com.rxlearning.models

interface Model<T> : BaseParcelable {
    var id: T?
}