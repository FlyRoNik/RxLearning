package com.rxlearning.models.user

import android.os.Parcel
import com.rxlearning.EMPTY_STRING_VALUE
import com.rxlearning.models.BaseParcelable
import com.rxlearning.models.read
import com.rxlearning.models.write

interface User : BaseParcelable {
    var id: String?
    var firstName: String?
    var lastName: String?
    var email: String?
    var phone: String?
    var role: RoleType?
    var avatar: String?
}

data class UserModel(override var id: String? = null,
                     override var firstName: String? = EMPTY_STRING_VALUE,
                     override var lastName: String? = EMPTY_STRING_VALUE,
                     override var email: String? = EMPTY_STRING_VALUE,
                     override var phone: String? = EMPTY_STRING_VALUE,
                     override var role: RoleType? = RoleType.STUDENT,
                     override var avatar: String? = EMPTY_STRING_VALUE) : User {

    companion object {
        @JvmField
        val CREATOR = BaseParcelable.generateCreator {
            UserModel(it.read(), it.read(), it.read(), it.read(), it.read(), it.read(), it.read())
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = dest.write(id, firstName, lastName, email,
            phone, role, avatar)

}