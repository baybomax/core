package com.libs.core

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/**
 * We need to create this class cos this class allows us to use GlideApp
 */
@GlideModule
class LibGlideModule: AppGlideModule() {
    override fun isManifestParsingEnabled() = false
}