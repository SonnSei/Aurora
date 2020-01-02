package java.lang;
public final class Boolean implements java.io.Serializable,
                                      Comparable<Boolean>
{
    
    // 两个类变量，可以认为是两个单例
    public static final Boolean TRUE = new Boolean(true);
    public static final Boolean FALSE = new Boolean(false);

    // 可以调用Boolean.TYPE
    @SuppressWarnings("unchecked")
    public static final Class<Boolean> TYPE = (Class<Boolean>) Class.getPrimitiveClass("boolean");

    // 值
    private final boolean value;

    // 序列化版本
    private static final long serialVersionUID = -3665804199014368530L;

    // 直接bool类型赋值
    public Boolean(boolean value) {
        this.value = value;
    }

    // 配合parseBoolean方法，判断逻辑是：不为true的就是false，包括null
    public Boolean(String s) {
        this(parseBoolean(s));
    }
    public static boolean parseBoolean(String s) {
        return ((s != null) && s.equalsIgnoreCase("true"));
    }

    // 返回基本数据类型的值
    public boolean booleanValue() {
        return value;
    }

    // TRUE和FALSE是两个实例，自动装箱的时候其实是调用的valueOf方
    // Boolean.TRUE==true 的逻辑是调用包装类的booleanValue，也就是拆箱了，而不是装箱
    // 别的包装类里也有拆箱调用的函数，比如Intege.intValue()、Long.longValue()
    public static Boolean valueOf(boolean b) {
        return (b ? TRUE : FALSE);
    }
    public static Boolean valueOf(String s) {
        return parseBoolean(s) ? TRUE : FALSE;
    }

    // 类方法，转字符串
    public static String toString(boolean b) {
        return b ? "true" : "false";
    }
    public String toString() {
        return value ? "true" : "false";
    }

    // 就两个hashCode值
    public int hashCode() {
        return Boolean.hashCode(value);
    }
    public static int hashCode(boolean value) {
        return value ? 1231 : 1237;
    }

    // 相等：1. 是Boolean实例；2. value相等
    // null instanceof Boolean 结果是false 
    public boolean equals(Object obj) {
        if (obj instanceof Boolean) {
            return value == ((Boolean)obj).booleanValue();
        }
        return false;
    }
    
    // 用来获取系统配置的，并不是针对字符串，传入的name其实是key
    public static boolean getBoolean(String name) {
        boolean result = false;
        try {
            result = parseBoolean(System.getProperty(name));
        } catch (IllegalArgumentException | NullPointerException e) {
        }
        return result;
    }

    // :)  TRUE > FALSE 
    public int compareTo(Boolean b) {
        return compare(this.value, b.value);
    }
    public static int compare(boolean x, boolean y) {
        return (x == y) ? 0 : (x ? 1 : -1);
    }

    // a && b
    public static boolean logicalAnd(boolean a, boolean b) {
        return a && b;
    }

    // a||b
    public static boolean logicalOr(boolean a, boolean b) {
        return a || b;
    }

    // 异或
    public static boolean logicalXor(boolean a, boolean b) {
        return a ^ b;
    }
}
