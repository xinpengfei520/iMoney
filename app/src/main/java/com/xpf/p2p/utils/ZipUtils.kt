package com.xpf.p2p.utils

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * Created by xpf on 2017/11/28 :)
 * Function:Java utils 实现的Zip工具
 */
object ZipUtils {

    private const val BUFF_SIZE = 1024 * 1024

    @JvmStatic
    @Throws(IOException::class)
    fun zipFiles(resFileList: Collection<File>, zipFile: File) {
        val zos = ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile), BUFF_SIZE))
        for (resFile in resFileList) {
            zipFile(resFile, zos, "")
        }
        zos.close()
    }

    @JvmStatic
    @Throws(IOException::class)
    fun zipFiles(resFileList: Collection<File>, zipFile: File, comment: String) {
        val zos = ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile), BUFF_SIZE))
        for (resFile in resFileList) {
            zipFile(resFile, zos, "")
        }
        zos.setComment(comment)
        zos.close()
    }

    @JvmStatic
    @Throws(ZipException::class, IOException::class)
    fun upZipFile(zipFile: File, folderPath: String) {
        val desDir = File(folderPath)
        if (!desDir.exists()) {
            desDir.mkdirs()
        }
        val zf = ZipFile(zipFile)
        val entries = zf.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement() as ZipEntry
            val input = zf.getInputStream(entry)
            var str = folderPath + File.separator + entry.name
            str = String(str.toByteArray(charset("8859_1")), charset("GB2312"))
            val desFile = File(str)
            if (!desFile.exists()) {
                val fileParentDir = desFile.parentFile
                if (!fileParentDir!!.exists()) {
                    fileParentDir.mkdirs()
                }
                desFile.createNewFile()
            }
            val out = FileOutputStream(desFile)
            val buffer = ByteArray(BUFF_SIZE)
            var realLength: Int
            while (input.read(buffer).also { realLength = it } > 0) {
                out.write(buffer, 0, realLength)
            }
            input.close()
            out.close()
        }
    }

    @JvmStatic
    @Throws(ZipException::class, IOException::class)
    fun upZipSelectedFile(zipFile: File, folderPath: String, nameContains: String): ArrayList<File> {
        val fileList = ArrayList<File>()
        val desDir = File(folderPath)
        if (!desDir.exists()) {
            desDir.mkdir()
        }
        val zf = ZipFile(zipFile)
        val entries = zf.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement() as ZipEntry
            if (entry.name.contains(nameContains)) {
                val input = zf.getInputStream(entry)
                var str = folderPath + File.separator + entry.name
                str = String(str.toByteArray(charset("8859_1")), charset("GB2312"))
                val desFile = File(str)
                if (!desFile.exists()) {
                    val fileParentDir = desFile.parentFile
                    if (!fileParentDir!!.exists()) {
                        fileParentDir.mkdirs()
                    }
                    desFile.createNewFile()
                }
                val out = FileOutputStream(desFile)
                val buffer = ByteArray(BUFF_SIZE)
                var realLength: Int
                while (input.read(buffer).also { realLength = it } > 0) {
                    out.write(buffer, 0, realLength)
                }
                input.close()
                out.close()
                fileList.add(desFile)
            }
        }
        return fileList
    }

    @JvmStatic
    @Throws(ZipException::class, IOException::class)
    fun getEntriesNames(zipFile: File): ArrayList<String> {
        val entryNames = ArrayList<String>()
        val entries = getEntriesEnumeration(zipFile)
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement() as ZipEntry
            entryNames.add(String(getEntryName(entry).toByteArray(charset("GB2312")), charset("8859_1")))
        }
        return entryNames
    }

    @JvmStatic
    @Throws(ZipException::class, IOException::class)
    fun getEntriesEnumeration(zipFile: File): java.util.Enumeration<*> {
        val zf = ZipFile(zipFile)
        return zf.entries()
    }

    @JvmStatic
    @Throws(UnsupportedEncodingException::class)
    fun getEntryComment(entry: ZipEntry): String {
        return String(entry.comment.toByteArray(charset("GB2312")), charset("8859_1"))
    }

    @JvmStatic
    @Throws(UnsupportedEncodingException::class)
    fun getEntryName(entry: ZipEntry): String {
        return String(entry.name.toByteArray(charset("GB2312")), charset("8859_1"))
    }

    @Throws(IOException::class)
    private fun zipFile(resFile: File, zos: ZipOutputStream, rootPath: String) {
        var path = rootPath + (if (rootPath.trim().isEmpty()) "" else File.separator) + resFile.name
        path = String(path.toByteArray(charset("8859_1")), charset("GB2312"))
        if (resFile.isDirectory) {
            val fileList = resFile.listFiles()
            if (fileList != null) {
                for (file in fileList) {
                    zipFile(file, zos, path)
                }
            }
        } else {
            val buffer = ByteArray(BUFF_SIZE)
            val bis = BufferedInputStream(FileInputStream(resFile), BUFF_SIZE)
            zos.putNextEntry(ZipEntry(path))
            var realLength: Int
            while (bis.read(buffer).also { realLength = it } != -1) {
                zos.write(buffer, 0, realLength)
            }
            bis.close()
            zos.flush()
            zos.closeEntry()
        }
    }
}
