package com.libs.core.data

import com.google.gson.*

/**
 * This class is used to create gSon.
 * @see
 */
open class JsonCreator {

    private var builder = GsonBuilder()

    /**
     * To create gSon with builder.
     *
     * @param prettyPrint default is true.
     * @param serializeNulls default is false.
     * @param excludeFieldsWithoutExpose default is false.
     * @param disableInnerClassSerialization default is false.
     * @param disableHtmlEscaping default is false.
     * @param fieldNamingPolicy default is null.
     * @param serializationExclusionStrategy default is null.
     * @param deserializationExclusionStrategy default is null.
     */
    fun create(prettyPrint: Boolean = true,
               serializeNulls: Boolean = false,
               excludeFieldsWithoutExpose: Boolean = false,
               disableInnerClassSerialization: Boolean = false,
               disableHtmlEscaping: Boolean = false,
               fieldNamingPolicy: FieldNamingPolicy? = null,
               serializationExclusionStrategy: ExclusionStrategy? = null,
               deserializationExclusionStrategy: ExclusionStrategy? = null): Gson {

        if (prettyPrint) builder.setPrettyPrinting()
        if (serializeNulls) builder.serializeNulls()
        if (excludeFieldsWithoutExpose) builder.excludeFieldsWithoutExposeAnnotation()
        if (disableInnerClassSerialization) builder.disableInnerClassSerialization()
        if (disableHtmlEscaping) builder.disableHtmlEscaping()

        fieldNamingPolicy?.let { builder.setFieldNamingPolicy(it) }
        serializationExclusionStrategy?.let { builder.addSerializationExclusionStrategy(it) }
        deserializationExclusionStrategy?.let { builder.addDeserializationExclusionStrategy(it) }

        // builder.registerTypeAdapter()
        // builder.registerTypeAdapterFactory()
        // builder.registerTypeHierarchyAdapter()

        return builder.create()
    }

    /**
     * Set date format to gSon.
     * @param pattern The pattern of date string.
     * @return The json creator instance.
     */
    fun setDateFormat(pattern: String): JsonCreator {
        builder.setDateFormat(pattern)
        return this
    }

    @Deprecated("Use setDateFormat(pattern: String) instead")
    fun setDateFormat(dateStyle: Int): JsonCreator {
        builder.setDateFormat(dateStyle)
        return this
    }

    @Deprecated("Use setDateFormat(pattern: String) instead")
    fun setDateFormat(dateStyle: Int, timeStyle: Int): JsonCreator {
        builder.setDateFormat(dateStyle, timeStyle)
        return this
    }
}

/**
 * Simple exclusion strategy of gSon fields.
 * @see ExclusionStrategy
 */
open class SimpleExclusionStrategy: ExclusionStrategy {

    open val skipClasses = mutableListOf<Class<*>>()
    open val skipFields = mutableListOf<String>()

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return skipClasses.contains(clazz)
    }

    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        return skipFields.contains(f?.name)
    }
}

