package kool


import org.lwjgl.system.MemoryUtil
import java.nio.*

/**
 * Created by elect on 05/03/17.
 */

fun Buffer.free() = MemoryUtil.memFree(this)

inline val Buffer.adr: Adr
    get() = MemoryUtil.memAddress(this)

inline var Buffer.pos: Int
    get() = position()
    set(value) {
        position(value)
    }

inline val Buffer.cap: Int
    get() = capacity()

inline val Buffer.rem: Int
    get() = remaining()

inline val Buffer.remSize: Int
    get() = rem * when {
        this is ByteBuffer -> 1
        this is ShortBuffer || this is CharBuffer -> 2
        this is IntBuffer || this is FloatBuffer -> 4
        this is LongBuffer || this is DoubleBuffer -> 8
        else -> throw Error("Invalid buffer type")
    }

inline var Buffer.lim: Int
    get() = limit()
    set(value) {
        limit(value)
    }