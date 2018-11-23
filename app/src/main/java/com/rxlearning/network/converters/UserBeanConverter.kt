package com.rxlearning.network.converters

import com.rxlearning.models.converters.BaseConverter
import com.rxlearning.models.user.User
import com.rxlearning.models.user.UserModel
import com.rxlearning.network.bean.UserBean

interface UserBeanConverter

class UserBeanConverterImpl : BaseConverter<UserBean, User>(), UserBeanConverter {

    override fun processConvertInToOut(inObject: UserBean) = inObject.run {
        UserModel(id, firstName, lastName, email, phone, role, avatar)
    }

    override fun processConvertOutToIn(outObject: User) = outObject.run {
        UserBean(id, firstName, lastName, email, phone, role)
    }

}