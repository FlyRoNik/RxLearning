package com.rxlearning.models.user

import android.os.Parcel
import com.rxlearning.EMPTY_STRING_VALUE
import com.rxlearning.models.BaseParcelable
import com.rxlearning.models.read
import com.rxlearning.models.write

class SignUp(val firstName: String = EMPTY_STRING_VALUE,
             val lastName: String = EMPTY_STRING_VALUE,
             val email: String = EMPTY_STRING_VALUE,
             val phone: String = EMPTY_STRING_VALUE) : BaseParcelable {
    companion object {
        @JvmField
        val CREATOR = BaseParcelable.generateCreator {
            SignUp(it.read(), it.read(), it.read(), it.read())
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = dest.write(firstName, lastName, email, phone)
}