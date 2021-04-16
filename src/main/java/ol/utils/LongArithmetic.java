package ol.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

public class LongArithmetic {
    public static class Sum {

        public static <T> Collection<T> sum(T[] add1, T[] add2, BiFunction<T, T, T[]> funcSumTwoDigit) {

            if (add1.length < add2.length) {
                return LongArithmetic.Sum.sum(add2, add1, funcSumTwoDigit);
            }

            int minLenth = add2.length;
            int maxLenth = add1.length;

            ArrayList<T> result = new ArrayList<>();

            int i = 0;
            for (; i < maxLenth; i++) {
                T[] dig;
                if (i < minLenth) {
                    dig = funcSumTwoDigit.apply(add1[i], add2[i]);
                } else {
                    dig = Arrays.copyOfRange(add1, i, i + 1);
                }

                if (result.size() == i) {
                    result.add(dig[0]);
                } else {
                    T[] dig1 = funcSumTwoDigit.apply(result.get(i), dig[0]);
                    result.set(i, dig1[0]);

                    if (dig1.length > 1) {
                        result.add(dig1[1]);
                    }
                }
                if (dig.length > 1) {
                    result.add(dig[1]);
                }
            }


            return result;
        }

        public static <T> Collection<T> sum(Collection<T> add1, Collection<T> add2, BiFunction<T, T, T[]> funcSumTwoDigit) {
            ArrayList<T> add1Array = new ArrayList<>(add1);
            ArrayList<T> add2Array = new ArrayList<>(add2);
            return sum((T[]) add1Array.toArray(), (T[]) add2Array.toArray(), funcSumTwoDigit);
        }

        public static class SumTwo {
            public static BiFunction<Character, Character, Character[]> characterDecimal = (ch1, ch2) -> {
                if (!(Character.isDigit(ch1) && Character.isDigit(ch2))) {
                    throw new ArithmeticException("Not digit char found!");
                }

                int val = Character.digit(ch1, 10) + Character.digit(ch2, 10);
                //int val=(ch1.charValue() & 0x0f) + (ch2.charValue() & 0x0F);
                if (val < 10) {
                    return new Character[]{Character.forDigit(val, 10)};
                }
                //return new Character[]{(char)(val%10 | 0x30),(char)((val/10 &0xFF) | 0x30)};
                return new Character[]{Character.forDigit(val % 10, 10), Character.forDigit(val / 10, 10)};
            };

            public static BiFunction<Byte, Byte, Byte[]> bytes = (arg1, arg2) -> {

                int val = arg1 + arg2;
                if (val <= Byte.MAX_VALUE) {
                    return new Byte[]{(byte) val};
                }
                return new Byte[]{(byte) (val % (Byte.MAX_VALUE + 1)), (byte) (val / (Byte.MAX_VALUE + 1))};
            };

            public static BiFunction<Byte, Byte, Byte[]> unsignedByte = (arg1, arg2) -> {

                int val = Byte.toUnsignedInt(arg1) + Byte.toUnsignedInt(arg2);
                if ((0xFFFF & val) <= 0x00FF) {
                    return new Byte[]{(byte) val};
                }
                return new Byte[]{(byte) (val & 0x00FF), (byte) (val >> 8)};
            };

            public static BiFunction<Integer, Integer, Integer[]> unsignedInteger = (arg1, arg2) -> {

                long val = Integer.toUnsignedLong(arg1) + Integer.toUnsignedLong(arg2);
                if ((val >> 32) == 0x0000_0000) {
                    return new Integer[]{(int) (0x0000_0000_FFFF_FFFFL & val)};
                }
                return new Integer[]{(int) (0x0000_0000_FFFF_FFFFL & val), (int) (val >> 32)};
            };

        }
    }

    public static class Sub {
        public static <T> Collection<T> sub(T[] minuend, T[] subtrahend, BiFunction<T, T, T[]> funcDiffTwoDigit) {

            if (minuend.length < subtrahend.length) {
                throw new ArithmeticException("The lenght of minuend  should be longer or same as subtrahend!");
            }

            ArrayList<T> result = new ArrayList<>();

            int i = 0;
            for (; i < minuend.length; i++) {
                T[] dig;

                if (i < subtrahend.length) {
                    dig = funcDiffTwoDigit.apply(minuend[i], subtrahend[i]);
                } else {
                    dig = Arrays.copyOfRange(minuend, i, i + 1);
                }

                if (result.size() == i) {
                    result.add(dig[0]);
                } else {
                    dig = funcDiffTwoDigit.apply(dig[0], result.get(i));
                    result.set(i, dig[0]);
                }

                if (dig.length > 1) {
                    result.add(dig[1]);
                }
            }

            return result;
        }

        public static class SubTwo {
            public static BiFunction<Character, Character, Character[]> characterDecimal = (ch1, ch2) -> {
                if (!(Character.isDigit(ch1) && Character.isDigit(ch2))) {
                    throw new ArithmeticException("Not digit char found!");
                }

                int dig1 = Character.digit(ch1, 10);
                int dig2 = Character.digit(ch2, 10);
                if (dig1 == -1 || dig2 == -1) {
                    throw new ArithmeticException((dig1 == -1 ? ch1 : ch2) + "- is not digit char of 10 radix!");
                }

                int val = dig1 - dig2;

                if (val >= 0) {
                    return new Character[]{Character.forDigit(val, 10)};
                }
                return new Character[]{Character.forDigit(10 + val, 10), Character.forDigit(1, 10)};
            };

            public static BiFunction<Byte, Byte, Byte[]> bytes7bit = (arg1, arg2) -> {

                int val = arg1 - arg2;
                if (val >= 0) {
                    return new Byte[]{(byte) val};
                }
                return new Byte[]{(byte) ((Byte.MAX_VALUE + 1) + val), 1};
            };

            public static BiFunction<Byte, Byte, Byte[]> unsignedByte = (arg1, arg2) -> {

                int val = Byte.toUnsignedInt(arg1) - Byte.toUnsignedInt(arg2);
                if (val >= 0) {
                    return new Byte[]{(byte) val};
                }
                return new Byte[]{(byte) (0x0100 + val), (byte) 1};
            };

            public static BiFunction<Integer, Integer, Integer[]> unsignedInteger = (arg1, arg2) -> {

                long val = Integer.toUnsignedLong(arg1) - Integer.toUnsignedLong(arg2);
                if (val >= 0) {
                    return new Integer[]{(int) (0x0000_0000_FFFF_FFFFL & val)};
                }
                return new Integer[]{(int) (0x0000_0001_0000_0000L + val), 1};
            };

        }

    }

    public static class Production {

        public static <T> Collection<T> mul(T[] mul1, T[] mul2, BiFunction<T, T, T[]> funcMulTwoDigit, BiFunction<T, T, T[]> funcSumTwoDigit) {

            ArrayList<T> result = new ArrayList<>();


            for (int i = 0; i < mul1.length; i++) {
                for (T t : mul2) {

                    T[] dig = funcMulTwoDigit.apply(mul1[i], t);
                    Collection<T> res = Sum.sum(result.subList(i, result.size()), Arrays.asList(dig), funcSumTwoDigit);
                    Integer index = i;
                    AtomicReference<Integer> k = new AtomicReference<>(0);
                    res.forEach(item -> result.set(index + k.getAndSet(k.get() + 1), item));
                }
            }

            return result;
        }

        public static <T> Collection<T> mul(Collection<T> add1, Collection<T> add2, BiFunction<T, T, T[]> funcMulTwoDigit, BiFunction<T, T, T[]> funcSumTwoDigit) {
            ArrayList<T> add1Array = new ArrayList<>(add1);
            ArrayList<T> add2Array = new ArrayList<>(add2);
            return mul((T[]) add1Array.toArray(), (T[]) add2Array.toArray(), funcMulTwoDigit, funcSumTwoDigit);
        }

        public static class MulTwo {
            public static BiFunction<Character, Character, Character[]> characterDecimal = (ch1, ch2) -> {
                if (!(Character.isDigit(ch1) && Character.isDigit(ch2))) {
                    throw new ArithmeticException("Not digit char found!");
                }

                int val = Character.digit(ch1, 10) * Character.digit(ch2, 10);
                if (val < 10) {
                    return new Character[]{Character.forDigit(val, 10)};
                }
                return new Character[]{Character.forDigit(val % 10, 10), Character.forDigit(val / 10, 10)};
            };

            public static BiFunction<Byte, Byte, Byte[]> bytes = (arg1, arg2) -> {

                int val = arg1 * arg2;
                if (val <= Byte.MAX_VALUE) {
                    return new Byte[]{(byte) val};
                }
                return new Byte[]{(byte) (val % (Byte.MAX_VALUE + 1)), (byte) (val / (Byte.MAX_VALUE + 1))};
            };

            public static BiFunction<Byte, Byte, Byte[]> unsignedByte = (arg1, arg2) -> {

                int val = Byte.toUnsignedInt(arg1) * Byte.toUnsignedInt(arg2);
                if ((0xFFFF & val) <= 0x00FF) {
                    return new Byte[]{(byte) val};
                }
                return new Byte[]{(byte) (val & 0x00FF), (byte) (val >> 8)};
            };

            public static BiFunction<Integer, Integer, Integer[]> unsignedInteger = (arg1, arg2) -> {

                long val = Integer.toUnsignedLong(arg1) * Integer.toUnsignedLong(arg2);
                if ((val >> 32) == 0x0000_0000) {
                    return new Integer[]{(int) (0x0000_0000_FFFF_FFFFL & val)};
                }
                return new Integer[]{(int) (0x0000_0000_FFFF_FFFFL & val), (int) (val >> 32)};
            };

        }
    }
}
