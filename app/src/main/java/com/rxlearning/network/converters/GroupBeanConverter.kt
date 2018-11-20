package com.rxlearning.network.converters

import com.rxlearning.models.converters.BaseConverter
import com.rxlearning.models.group.Group
import com.rxlearning.models.group.GroupModel
import com.rxlearning.models.subject.SubjectModel
import com.rxlearning.network.SUBJECT_INFO_ID_KEY
import com.rxlearning.network.SUBJECT_INFO_NAME_KEY
import com.rxlearning.network.bean.GroupBean

interface GroupBeanConverter

class GroupBeanConverterImpl : BaseConverter<GroupBean, Group>(), GroupBeanConverter {

    override fun processConvertInToOut(inObject: GroupBean) = inObject.run {
        GroupModel(id, ownerId, name, infoSubjects?.map { SubjectModel(it[SUBJECT_INFO_ID_KEY], it[SUBJECT_INFO_NAME_KEY]) })
    }

    override fun processConvertOutToIn(outObject: Group) = outObject.run {
        GroupBean(id, name, ownerId, listOf(), listOf(), listOf(), listOf())
    }

}