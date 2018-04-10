package akg;

import java.math.BigInteger;
import java.util.Random;

public class AKG {

    //��� ���������
    private static BigInteger one = BigInteger.valueOf(1);
    private static BigInteger _one = BigInteger.valueOf(-1);
    //��� ����������
    private static BigInteger two = BigInteger.valueOf(2);

    public static void main(String[] args) {
        int P = 32;
        BigInteger n = getPrimary(P);
        System.out.println("=================================================================");
        System.out.println("DEC\t\tHEX\t\tBIN");
        System.out.println("=================================================================");
        System.out.println(n + "\t" + toHex(n.longValue()) + "\t" + toBin(n.longValue()));
        System.out.println("=================================================================");
    }

    private static BigInteger getPrimary(int P) {
        //���������������� ������� �����
        BigInteger n;
        //���� ����� �� �������
        do {
            //���������� ����� ��������� �����
            n = newNumber(P);
            //�������� �������� �����
        } while (!testRepeater(n, 5));
        return n;
    }

    private static BigInteger newNumber(int P) {
        //���������� ����� ������ P ���
        BigInteger newNumber = new BigInteger(P, new Random());
        //��������� ������� � ������ � �����
        newNumber = newNumber.setBit(0);
        newNumber = newNumber.setBit(P - 1);
        return newNumber;
    }

    private static boolean testRepeater(BigInteger n, int k) {
        //������������ n - 1
        BigInteger t = n.subtract(one);
        //n - 1 = 2 ^ s * t 
        int s = 0;
        //������? (������� ��� = 0)
        while (t.setBit(0).equals(0)) {
            //����� �� ��� (�������� ������)
            t.shiftRight(1);
            //����������� ������� (������� ��� �������� �� 2 = ������� ������ � ����������)
            s += 1;
        }
        for (int i = 0; i < k; i++) {
            //�� ������ ���� �� ��� - ���������
            if (!miller_rabin(n, t, s)) {
                return false;
            }
        }
        //���� ������ k ��� - �������
        return true;
    }

    private static boolean miller_rabin(BigInteger n, BigInteger t, int s) {
        //�������� ��������� ��������
        BigInteger a;
        do {
            a = new BigInteger(n.bitLength(), new Random());
            //���c������ �������� �� �������������� ��������� [2; n - 2]
        } while (a.compareTo(two) < 0 || a.compareTo(n) > 0);
        //�������� � �������� ����� n - 1
        //x = a ^ t mon n
        BigInteger x = a.modPow(t, n);
        //���� 1 ��� -1 �� ���� ����, �� �� ��������� (���������� � �������) ����� ������ 1
        if (x.equals(one) || x.equals(_one)) {
            return true;
        }
        //�������� � ������� ������� ���, �������� ����� ������� ������
        for (int j = 1; j < s; j++) {
            x = x.modPow(two, n);
            //
            if (x.equals(one)) {
                return false;
            }
            //
            if (x.equals(_one)) {
                return true;
            }
        }
        //����� �� n - 1 => ���������
        return false;
    }

    //<editor-fold defaultstate="collapsed" desc="������ ��������� ����������">
    private static String toBin(long num) {
        return Long.toBinaryString(num);
    }

    private static String toHex(long num) {
        return Long.toHexString(num).toUpperCase();
    }

    private static String addSeparators(String string, int position, String delimer) {
        return string.replaceAll("(.{" + position + "})", "$1" + delimer);
    }

    private static String formatToLen(String string, int length) {
        return String.format("%" + length + "s", string).replace(' ', '0');
    }
    //</editor-fold>
}
