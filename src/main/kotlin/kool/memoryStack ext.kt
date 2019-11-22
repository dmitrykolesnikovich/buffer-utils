package kool

import org.lwjgl.system.Configuration
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.Pointer


inline fun MemoryStack.mByte(count: Int = 1) = BytePtr(nmalloc(Byte.SIZE_BYTES, Byte.SIZE_BYTES * count))
inline fun MemoryStack.mShort(count: Int = 1) = ShortPtr(nmalloc(Short.SIZE_BYTES, Short.SIZE_BYTES * count))
inline fun MemoryStack.mInt(count: Int = 1) = IntPtr(nmalloc(Int.SIZE_BYTES, Int.SIZE_BYTES * count))
inline fun MemoryStack.mLong(count: Int = 1) = LongPtr(nmalloc(Long.SIZE_BYTES, Long.SIZE_BYTES * count))
inline fun MemoryStack.mFloat(count: Int = 1) = FloatPtr(nmalloc(java.lang.Float.BYTES, java.lang.Float.BYTES * count))
inline fun MemoryStack.mDouble(count: Int = 1) = DoublePtr(nmalloc(java.lang.Double.BYTES, java.lang.Double.BYTES * count))
inline fun MemoryStack.mPointer(count: Int = 1) = PointerPtr(nmalloc(Long.SIZE_BYTES, Long.SIZE_BYTES * count))

inline fun MemoryStack.cByte(count: Int = 1) = BytePtr(ncalloc(Byte.SIZE_BYTES, count, Byte.SIZE_BYTES))
inline fun MemoryStack.cShort(count: Int = 1) = ShortPtr(ncalloc(Short.SIZE_BYTES, count, Short.SIZE_BYTES))
inline fun MemoryStack.cInt(count: Int = 1) = IntPtr(ncalloc(Int.SIZE_BYTES, count, Int.SIZE_BYTES))
inline fun MemoryStack.cLong(count: Int = 1) = LongPtr(ncalloc(Long.SIZE_BYTES, count, Long.SIZE_BYTES))
inline fun MemoryStack.cFloat(count: Int = 1) = FloatPtr(ncalloc(java.lang.Float.BYTES, count, java.lang.Float.BYTES))
inline fun MemoryStack.cDouble(count: Int = 1) = DoublePtr(ncalloc(java.lang.Double.BYTES, count, java.lang.Double.BYTES))
inline fun MemoryStack.cPointer(count: Int = 1) = PointerPtr(ncalloc(Long.SIZE_BYTES, count, Long.SIZE_BYTES))

fun MemoryStack.reset() {
    pointer = Configuration.STACK_SIZE.get(64) * 1024
}

fun MemoryStack.ptrOf(b: Byte): BytePtr = mByte(1).also { it[0] = b }
fun MemoryStack.ptrOf(b0: Byte, b1: Byte): BytePtr = mByte(2).also { it[0] = b0; it[1] = b1 }
fun MemoryStack.ptrOf(b0: Byte, b1: Byte, b2: Byte): BytePtr = mByte(3).also { it[0] = b0; it[1] = b1; it[2] = b2 }
fun MemoryStack.ptrOf(b0: Byte, b1: Byte, b2: Byte, b3: Byte): BytePtr = mByte(4).also { it[0] = b0; it[1] = b1; it[2] = b2; it[3] = b3 }
fun MemoryStack.ptrOf(vararg bytes: Byte): BytePtr = mByte(bytes.size).also { for(i in bytes.indices) it[i] = bytes[i] }

fun MemoryStack.ptrOf(s: Short): ShortPtr = mShort(1).also { it[0] = s }
fun MemoryStack.ptrOf(s0: Short, s1: Short): ShortPtr = mShort(2).also { it[0] = s0; it[1] = s1 }
fun MemoryStack.ptrOf(s0: Short, s1: Short, s2: Short): ShortPtr = mShort(3).also { it[0] = s0; it[1] = s1; it[2] = s2 }
fun MemoryStack.ptrOf(s0: Short, s1: Short, s2: Short, s3: Short): ShortPtr = mShort(4).also { it[0] = s0; it[1] = s1; it[2] = s2; it[3] = s3 }
fun MemoryStack.ptrOf(vararg shorts: Short): ShortPtr = mShort(shorts.size).also { for(i in shorts.indices) it[i] = shorts[i] }

fun MemoryStack.ptrOf(i: Int): IntPtr = mInt(1).also { it[0] = i }
fun MemoryStack.ptrOf(i0: Int, i1: Int): IntPtr = mInt(2).also { it[0] = i0; it[1] = i1 }
fun MemoryStack.ptrOf(i0: Int, i1: Int, i2: Int): IntPtr = mInt(3).also { it[0] = i0; it[1] = i1; it[2] = i2 }
fun MemoryStack.ptrOf(i0: Int, i1: Int, i2: Int, i3: Int): IntPtr = mInt(4).also { it[0] = i0; it[1] = i1; it[2] = i2; it[3] = i3 }
fun MemoryStack.ptrOf(vararg ints: Int): IntPtr = mInt(ints.size).also { for(i in ints.indices) it[i] = ints[i] }

fun MemoryStack.ptrOf(L: Long): LongPtr = mLong(1).also { it[0] = L }
fun MemoryStack.ptrOf(L0: Long, L1: Long): LongPtr = mLong(2).also { it[0] = L0; it[1] = L1 }
fun MemoryStack.ptrOf(L0: Long, L1: Long, L2: Long): LongPtr = mLong(3).also { it[0] = L0; it[1] = L1; it[2] = L2 }
fun MemoryStack.ptrOf(L0: Long, L1: Long, L2: Long, L3: Long): LongPtr = mLong(4).also { it[0] = L0; it[1] = L1; it[2] = L2; it[3] = L3 }
fun MemoryStack.ptrOf(vararg longs: Long): LongPtr = mLong(longs.size).also { for(i in longs.indices) it[i] = longs[i] }

fun MemoryStack.ptrOf(f: Float): FloatPtr = mFloat(1).also { it[0] = f }
fun MemoryStack.ptrOf(f0: Float, f1: Float): FloatPtr = mFloat(2).also { it[0] = f0; it[1] = f1 }
fun MemoryStack.ptrOf(f0: Float, f1: Float, f2: Float): FloatPtr = mFloat(3).also { it[0] = f0; it[1] = f1; it[2] = f2 }
fun MemoryStack.ptrOf(f0: Float, f1: Float, f2: Float, f3: Float): FloatPtr = mFloat(4).also { it[0] = f0; it[1] = f1; it[2] = f2; it[3] = f3 }
fun MemoryStack.ptrOf(vararg floats: Float): FloatPtr = mFloat(floats.size).also { for(i in floats.indices) it[i] = floats[i] }

fun MemoryStack.ptrOf(d: Double): DoublePtr = mDouble(1).also { it[0] = d }
fun MemoryStack.ptrOf(d0: Double, d1: Double): DoublePtr = mDouble(2).also { it[0] = d0; it[1] = d1 }
fun MemoryStack.ptrOf(d0: Double, d1: Double, d2: Double): DoublePtr = mDouble(3).also { it[0] = d0; it[1] = d1; it[2] = d2 }
fun MemoryStack.ptrOf(d0: Double, d1: Double, d2: Double, d3: Double): DoublePtr = mDouble(4).also { it[0] = d0; it[1] = d1; it[2] = d2; it[3] = d3 }
fun MemoryStack.ptrOf(vararg doubles: Double): DoublePtr = mDouble(doubles.size).also { for(i in doubles.indices) it[i] = doubles[i] }

fun MemoryStack.ptrOf(p: Pointer): PointerPtr = mPointer(1).also { it[0] = p }
//fun MemoryStack.ptrOf(d0: Double, d1: Double): DoublePtr = mDouble(2).also { it[0] = d0; it[1] = d1 }
//fun MemoryStack.ptrOf(d0: Double, d1: Double, d2: Double): DoublePtr = mDouble(3).also { it[0] = d0; it[1] = d1; it[2] = d2 }
//fun MemoryStack.ptrOf(d0: Double, d1: Double, d2: Double, d3: Double): DoublePtr = mDouble(4).also { it[0] = d0; it[1] = d1; it[2] = d2; it[3] = d3 }
//fun MemoryStack.ptrOf(vararg doubles: Double): DoublePtr = mDouble(doubles.size).also { for(i in doubles.indices) it[i] = doubles[i] }