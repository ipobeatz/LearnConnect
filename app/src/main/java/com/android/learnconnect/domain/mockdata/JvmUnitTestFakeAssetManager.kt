package com.android.learnconnect.domain.mockdata

import androidx.annotation.VisibleForTesting
import com.android.learnconnect.domain.mockdata.fake.FakeAssetManager
import java.io.File
import java.io.InputStream

@VisibleForTesting
object JvmUnitTestFakeAssetManager : FakeAssetManager {
    private const val assetsDirectory = "src/main/assets"

    override fun open(fileName: String): InputStream {
        val file = File(assetsDirectory, fileName)
        require(file.exists()) { "Asset file not found: $fileName" }
        return file.inputStream()
    }
}
