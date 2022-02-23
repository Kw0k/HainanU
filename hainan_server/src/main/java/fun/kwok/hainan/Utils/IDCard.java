package fun.kwok.hainan.Utils;

public class IDCard {
    private static int[] w = {7,9,10,5,8,4,2,1,6,
            3,7,9,10,5,8,4,2};
    public static boolean isIDCard(String idcardno){
        idcardno=idcardno.toUpperCase();
            char[] c=idcardno.toCharArray();
            if (c.length<18)
                return false;
            int sum=0;
            for (int i = 0; i < w.length; i++) {
                sum+=(c[i]-'0')*w[i];
            }
            char[] verifyCode="10X98765432".toCharArray();
            char ch =verifyCode[sum%11];
            return c[17]==ch;

    }
}
