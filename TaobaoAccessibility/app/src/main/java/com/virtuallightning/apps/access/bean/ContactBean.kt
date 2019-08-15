package com.virtuallightning.apps.access.bean

data class ContactBean (
    val name: String,
    val phoneNum: String
) {
    override fun equals(other: Any?): Boolean {
        if(other is ContactBean) {
            if(other.phoneNum == phoneNum)
                return true
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + phoneNum.hashCode()
        return result
    }
}