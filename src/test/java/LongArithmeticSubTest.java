import ol.utils.LongArithmetic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class LongArithmeticSubTest {
    Object arg1;
    Object arg2;
    Object expected;

    public LongArithmeticSubTest(Object arg1, Object arg2, Object expected) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Object[][] parameters1() {
        return new Object[][]{
                {"001", "1", "990"},
                {"101", "1", "001"},
                {"19", "3", "88"},
                //in hex (127)0x7F0x7F - (-1)0xFF = (-128)0x80(126)0x7E
                //in bit order 0x7F_7F - (-1)0xFF = (1)0x7E_80
                {new Byte[]{127, 127}, new Byte[]{-1}, new Byte[]{-128, 126}},

                //in hex (0,1)0x00_0x01 - (-1)0xFF = (1)0x01
                //in bit order 0x01_00 - (-1)0xFF = (1)0x01
                {new Byte[]{0, 1}, new Byte[]{-1}, new Byte[]{1, 0}},

                //in hex (-1)0xFF - (1)0x01 = 0xFE(-2) ((low byte)0x00 (hight byte)0x01 )
                {new Byte[]{-1}, new Byte[]{1}, new Byte[]{-2}},

                //in hex (-1)0xFFFF_FFFF - (1)0x0000_0010 = 0x100 ((low byte)0x00 (hight byte)0x01 )
                {new Integer[]{-1}, new Integer[]{2}, new Integer[]{-3}},

                //in hex (-1)0xFFFF_FFFF - (1)0x1000_0000 = 0x7FFF_FFFF ((low byte)0x00 (hight byte)0x01 )
                {new Integer[]{-1}, new Integer[]{0x8000_0000}, new Integer[]{0x7FFF_FFFF}},
        };
    }

    @Before
    public void initialise() {
    }

    @Test
    public void testMethod() {
        if (arg1 instanceof String) {
            assertString();
        } else if (arg1 instanceof Byte[]) {
            assertBytes();
        } else if (arg1 instanceof Integer[]) {
            assertIntegers();
        }
    }

    private void assertString() {
        Collection<Character> result = LongArithmetic.Sub.sub(
                ((String) arg1).chars().mapToObj(c -> (char) c).toArray(Character[]::new)
                , ((String) arg2).chars().mapToObj(c -> (char) c).toArray(Character[]::new)
                , LongArithmetic.Sub.SubTwo.characterDecimal);

        String res = result.stream().map(String::valueOf).collect(Collectors.joining());
        System.out.println(arg1 + "-" + arg2 + "=\n" + res);
        System.out.println(expected);
        assertTrue(res.equals(expected));
    }

    private void assertBytes() {
        Collection<Byte> result = LongArithmetic.Sub.sub(
                (Byte[]) arg1
                , (Byte[]) arg2
                , LongArithmetic.Sub.SubTwo.unsignedByte);

        Byte[] res = result.toArray(new Byte[result.size()]);

        System.out.println(Arrays.toString((Byte[]) arg1) + "-" + Arrays.toString((Byte[]) arg2) + "=\n" + Arrays.toString(res));
        System.out.println(Arrays.toString((Byte[]) expected));

        assertTrue(Arrays.deepEquals(res, (Byte[]) expected));
    }

    private void assertIntegers() {
        Collection<Integer> result = LongArithmetic.Sub.sub(
                (Integer[]) arg1
                , (Integer[]) arg2
                , LongArithmetic.Sub.SubTwo.unsignedInteger);

        Integer[] res = result.toArray(new Integer[result.size()]);

        System.out.println(Arrays.toString((Integer[]) arg1) + "-" + Arrays.toString((Integer[]) arg2) + "=\n" + Arrays.toString(res));
        System.out.println(Arrays.toString((Integer[]) expected));

        assertTrue(Arrays.deepEquals(res, (Integer[]) expected));
    }
}
