package java.lang;

// Number类里面就定义了返回各种类型值的intValue、byteValue等等方法
public final class Short extends Number implements Comparable<Short> {

    // 最大值最小值
    public static final short   MIN_VALUE = -32768;
    public static final short   MAX_VALUE = 32767;
    @SuppressWarnings("unchecked")
    public static final Class<Short>    TYPE = (Class<Short>) Class.getPrimitiveClass("short");

    // 好像比Integer短的整型都是调用的Integer的toString方法
    public static String toString(short s) {
        return Integer.toString((int)s, 10);
    }

    // 也是调用的Integer的方法
    public static short parseShort(String s, int radix)
        throws NumberFormatException {
        int i = Integer.parseInt(s, radix);
        if (i < MIN_VALUE || i > MAX_VALUE)
            throw new NumberFormatException(
                "Value out of range. Value:\"" + s + "\" Radix:" + radix);
        return (short)i;
    }
    public static short parseShort(String s) throws NumberFormatException {
        return parseShort(s, 10);
    }    
    public static Short valueOf(String s, int radix)
        throws NumberFormatException {
        return valueOf(parseShort(s, radix));
    }
    public static Short valueOf(String s) throws NumberFormatException {
        return valueOf(s, 10);
    }

    // Short的缓存也是[-128，127]，Byte、Integer也是
    private static class ShortCache {
        private ShortCache(){}

        static final Short cache[] = new Short[-(-128) + 127 + 1];

        // 静态代码块初始化
        static {
            for(int i = 0; i < cache.length; i++)
                cache[i] = new Short((short)(i - 128));
        }
    }

    // 缓存范围内的就直接返回缓存的实例。
    // 自动装箱的缓存范围内的相同的值对应相同的实例
    public static Short valueOf(short s) {
        final int offset = 128;
        int sAsInt = s;
        if (sAsInt >= -128 && sAsInt <= 127) { // must cache
            return ShortCache.cache[sAsInt + offset];
        }
        return new Short(s);
    }

    // 调用Integer
    public static Short decode(String nm) throws NumberFormatException {
        int i = Integer.decode(nm);
        if (i < MIN_VALUE || i > MAX_VALUE)
            throw new NumberFormatException(
                    "Value " + i + " out of range from input " + nm);
        return valueOf((short)i);
    }

    private final short value;

    public Short(short value) {
        this.value = value;
    }

    // 只接受10进制字符串
    public Short(String s) throws NumberFormatException {
        this.value = parseShort(s, 10);
    }

    // 下面一组Number中定义的方法
    public byte byteValue() {return (byte)value;}
    public short shortValue() {return value;}
    public int intValue() {return (int)value;}
    public long longValue() {return (long)value;}
    public float floatValue() {return (float)value;}
    public double doubleValue() {return (double)value;}

    // 转int
    public String toString() {
        return Integer.toString((int)value);
    }

    // 1.8复用了类方法，之前都是直接转int返回的
    @Override
    public int hashCode() {
        return Short.hashCode(value);
    }
    public static int hashCode(short value) {
        return (int)value;
    }
    
    // 记住这个equals方法的写法，基本都是这么写的
    public boolean equals(Object obj) {
        if (obj instanceof Short) {
            return value == ((Short)obj).shortValue();
        }
        return false;
    }

    // 复用类方法compare
    public int compareTo(Short anotherShort) {
        return compare(this.value, anotherShort.value);
    }

    public static int compare(short x, short y) {
        return x - y;
    }

    public static final int SIZE = 16;

    
    public static final int BYTES = SIZE / Byte.SIZE;

    /**
     * Returns the value obtained by reversing the order of the bytes in the
     * two's complement representation of the specified {@code short} value.
     *
     * @param i the value whose bytes are to be reversed
     * @return the value obtained by reversing (or, equivalently, swapping)
     *     the bytes in the specified {@code short} value.
     * @since 1.5
     */
    public static short reverseBytes(short i) {
        return (short) (((i & 0xFF00) >> 8) | (i << 8));
    }


    // 可以记一下这个写法，转int的是是高位补最高
    // 比如short的3的二进制是000...00011，转int的时候高位补的是0
    //    short的-1的二进制是111...11111，转int的时候高位补的是1
    // &0xffff之后相当于只取后2字节，也就是short的长度
    public static int toUnsignedInt(short x) {
        return ((int) x) & 0xffff;
    }
    public static long toUnsignedLong(short x) {
        return ((long) x) & 0xffffL;
    }

    private static final long serialVersionUID = 7515723908773894738L;
}
