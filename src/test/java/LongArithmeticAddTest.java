import ol.utils.LongArithmetic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class LongArithmeticAddTest {
    Object arg1;
    Object arg2;
    Object expected;

    public LongArithmeticAddTest(Object arg1, Object arg2, Object expected) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Object[][] parameters1() {
        return new Object[][]{
                {"99", "1", "001"},
                {"99", "99", "891"},
                {"001", "1", "101"},
                {"19", "3", "49"},
                {new Byte[]{127}, new Byte[]{127}, new Byte[]{-2}},

                //in hex (-1)0xFF + (1)0x01 = 0x0100 ((low byte)0x00 (hight byte)0x01 )
                {new Byte[]{-1}, new Byte[]{1}, new Byte[]{0, 1}},
                {new Integer[]{-1}, new Integer[]{2}, new Integer[]{1, 1}},
        };
    }

    @Before
    public void initialise() {
    }

    @Test
    public void testMethod() {
        long start = System.currentTimeMillis();
        if (arg1 instanceof String) {
            assertString();
        } else if (arg1 instanceof Byte[]) {
            assertBytes();
        } else if (arg1 instanceof Integer[]) {
            assertIntegers();
        }
        System.out.println(System.currentTimeMillis() - start);
        System.out.println("BigInteger");
        if (arg1 instanceof String) {
            start = System.currentTimeMillis();
            BigInteger bigInteger1 = new BigInteger(new StringBuilder((String) arg1).reverse().toString(), 10);
            BigInteger bigInteger2 = new BigInteger(new StringBuilder((String) arg2).reverse().toString(), 10);
            BigInteger bigIntegerResult = bigInteger1.add(bigInteger2);
            System.out.println(System.currentTimeMillis() - start);
            System.out.println("BigInteger:" + bigIntegerResult.toString(10));

        } else if (arg1 instanceof Byte[]) {
            start = System.currentTimeMillis();

            byte[] arg1Byte = new byte[((Byte[]) arg1).length];
            int maxIndex = ((Byte[]) arg1).length - 1;
            for (int i = 0; i <= maxIndex; i++) {
                arg1Byte[i] = ((Byte[]) arg1)[maxIndex - i];
            }
            byte[] arg2Byte = new byte[((Byte[]) arg1).length];
            maxIndex = ((Byte[]) arg2).length - 1;
            for (int i = 0; i <= maxIndex; i++) {
                arg2Byte[i] = ((Byte[]) arg2)[maxIndex - i];
            }

            BigInteger bigInteger1 = new BigInteger(arg1Byte);
            BigInteger bigInteger2 = new BigInteger(arg2Byte);
            BigInteger bigIntegerResult = bigInteger1.add(bigInteger2);
            System.out.println(System.currentTimeMillis() - start);
            System.out.println("BigInteger:" + bigIntegerResult.toString(10));
        } else if (arg1 instanceof Integer[]) {
//            start=System.currentTimeMillis();
//
//            int[] arg1int=new int[((Integer[]) arg1).length];
//            int maxIndex=((Integer[]) arg1).length-1;
//            for (int i=0;i<=maxIndex;i++){
//                arg1int[i]=((Integer[]) arg1)[maxIndex-i];
//            }
//
//            int[] arg2int=new int[((Integer[]) arg2).length];
//            maxIndex=((Integer[]) arg2).length-1;
//            for (int i=0;i<=maxIndex;i++){
//                arg2int[i]=((Integer[]) arg2)[maxIndex-i];
//            }
//
//            BigInteger bigInteger1=new BigInteger(arg1int);
//            BigInteger bigInteger2=new BigInteger(arg2int);
//            BigInteger bigIntegerResult= bigInteger1.add(bigInteger2);
//            System.out.println(System.currentTimeMillis()-start);
//            System.out.println("BigInteger:"+bigIntegerResult.toString(10));
        }

    }

    private void assertString() {

        Collection<Character> result = LongArithmetic.Sum.<Character>sum(
                ((String) arg1).chars().mapToObj(c -> (char) c).toArray(Character[]::new)
                , ((String) arg2).chars().mapToObj(c -> (char) c).toArray(Character[]::new)
                , LongArithmetic.Sum.SumTwo.characterDecimal);

        String res = result.stream().map(String::valueOf).collect(Collectors.joining());
        System.out.println(arg1 + "+" + arg2 + "=\n" + res);
        System.out.println(expected);
        assertTrue(res.equals(expected));
    }

    private void assertBytes() {
        Collection<Byte> result = LongArithmetic.Sum.<Byte>sum(
                (Byte[]) arg1
                , (Byte[]) arg2
                , LongArithmetic.Sum.SumTwo.unsignedByte);

        Byte[] res = result.toArray(new Byte[result.size()]);

        System.out.println(Arrays.toString((Byte[]) arg1) + "+" + Arrays.toString((Byte[]) arg2) + "=\n" + Arrays.toString(res));
        System.out.println(Arrays.toString((Byte[]) expected));

        assertTrue(Arrays.deepEquals(res, (Byte[]) expected));
    }

    private void assertIntegers() {
        Collection<Integer> result = LongArithmetic.Sum.<Integer>sum(
                (Integer[]) arg1
                , (Integer[]) arg2
                , LongArithmetic.Sum.SumTwo.unsignedInteger);

        Integer[] res = result.toArray(new Integer[result.size()]);

        System.out.println(Arrays.toString((Integer[]) arg1) + "+" + Arrays.toString((Integer[]) arg2) + "=\n" + Arrays.toString(res));
        System.out.println(Arrays.toString((Integer[]) expected));

        assertTrue(Arrays.deepEquals(res, (Integer[]) expected));
    }

}