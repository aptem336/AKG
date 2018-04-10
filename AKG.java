package akg;

import java.math.BigInteger;
import java.util.Random;

public class AKG {

    //для сравнений
    private static BigInteger one = BigInteger.valueOf(1);
    private static BigInteger _one = BigInteger.valueOf(-1);
    //для возведения
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
        //предположительно простое число
        BigInteger n;
        //пока число не простое
        do {
            //генерируем новое случайное число
            n = newNumber(P);
            //проверим простоту числа
        } while (!testRepeater(n, 5));
        return n;
    }

    private static BigInteger newNumber(int P) {
        //генерируем число длиной P бит
        BigInteger newNumber = new BigInteger(P, new Random());
        //добавляем единицу в начало и конец
        newNumber = newNumber.setBit(0);
        newNumber = newNumber.setBit(P - 1);
        return newNumber;
    }

    private static boolean testRepeater(BigInteger n, int k) {
        //раскладываем n - 1
        BigInteger t = n.subtract(one);
        //n - 1 = 2 ^ s * t 
        int s = 0;
        //четное? (младший бит = 0)
        while (t.setBit(0).equals(0)) {
            //делим на два (сдвигаем вправо)
            t.shiftRight(1);
            //увеличиваем счётчик (сколько раз делиться на 2 = степень двойки в разложении)
            s += 1;
        }
        for (int i = 0; i < k; i++) {
            //не прошёл хотя бы раз - составное
            if (!miller_rabin(n, t, s)) {
                return false;
            }
        }
        //если прошёл k раз - простое
        return true;
    }

    private static boolean miller_rabin(BigInteger n, BigInteger t, int s) {
        //выбираем свидетеля простоты
        BigInteger a;
        do {
            a = new BigInteger(n.bitLength(), new Random());
            //собcтвенно проверка на принадлежность интервалу [2; n - 2]
        } while (a.compareTo(two) < 0 || a.compareTo(n) > 0);
        //возводим в нечетную часть n - 1
        //x = a ^ t mon n
        BigInteger x = a.modPow(t, n);
        //если 1 или -1 на этом шаге, то на следующем (возведении к квадрат) будет равным 1
        if (x.equals(one) || x.equals(_one)) {
            return true;
        }
        //возводим в квадрат столько раз, скольким равна степень двойки
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
        //дошли до n - 1 => составное
        return false;
    }

    //<editor-fold defaultstate="collapsed" desc="методы выведения переменных">
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
