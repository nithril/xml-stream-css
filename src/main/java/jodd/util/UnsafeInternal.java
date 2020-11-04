package jodd.util;


import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Simplified netty class.
 * Thanx: Stephane Landelle!
 *
 * https://github.com/netty/netty/blob/master/common/src/main/java/io/netty/util/internal/PlatformDependent.java
 */
final class UnsafeInternal {

    private static final Unsafe UNSAFE;
    private static final long STRING_VALUE_FIELD_OFFSET;
    private static final long STRING_OFFSET_FIELD_OFFSET;
    private static final long STRING_COUNT_FIELD_OFFSET;

    static {
        Unsafe unsafe = null;
        long stringValueFieldOffset = -1L;
        long stringOffsetFieldOffset = -1L;
        long stringCountFieldOffset = -1L;

        if (System.getProperty("java.version").startsWith("1.8")) {
            try {
                final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                unsafe = (Unsafe) unsafeField.get(null);
            } catch (final Throwable cause) {
                unsafe = null;
            }

            if (unsafe != null) {
                try {
                    stringValueFieldOffset = unsafe.objectFieldOffset(String.class.getDeclaredField("value"));
                    stringOffsetFieldOffset = unsafe.objectFieldOffset(String.class.getDeclaredField("offset"));
                    stringCountFieldOffset = unsafe.objectFieldOffset(String.class.getDeclaredField("count"));
                } catch (final Throwable ignore) {
                }
            }
        }

        UNSAFE = unsafe;
        STRING_VALUE_FIELD_OFFSET = stringValueFieldOffset;
        STRING_OFFSET_FIELD_OFFSET = stringOffsetFieldOffset;
        STRING_COUNT_FIELD_OFFSET = stringCountFieldOffset;
    }

    static boolean hasUnsafe() {
        return UNSAFE != null;
    }

    static char[] unsafeGetChars(final String string) {
        final char[] value = (char[]) UNSAFE.getObject(string, STRING_VALUE_FIELD_OFFSET);

        if (STRING_OFFSET_FIELD_OFFSET != -1) {
            // old String version with offset and count
            final int offset = UNSAFE.getInt(string, STRING_OFFSET_FIELD_OFFSET);
            final int count = UNSAFE.getInt(string, STRING_COUNT_FIELD_OFFSET);

            if (offset == 0 && count == value.length) {
                // no need to copy
                return value;

            } else {
                final char[] result = new char[count];
                System.arraycopy(value, offset, result, 0, count);
                return result;
            }

        } else {
            return value;
        }
    }

}
