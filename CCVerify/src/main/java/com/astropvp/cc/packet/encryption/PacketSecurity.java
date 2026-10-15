package com.astropvp.cc.packet.encryption;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PacketSecurity {
    private static String SECRET_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6cIRRHt6FT6ujZCMVdJiT72I08J4o+VjQqsW13LW14cgsJCc3mOh0D4WoXDgD4smcAOwYHmE8s7BdcTxwRZB+t/sB65MP186AKuZZuuzZ6rxBkrQpnKGliTXYuvyE0pz2fhI4b2lrefLxN8R9RoASM+utjV4o8H0qOeuhRUlNPKqslLc1xTy4rjbUQLkh+ZdXX1e5NYkFn72s7QlcSkBwXdRN7p3rDKTau/7YCz+mvnv1/dfINVhz2GKrgOsgyhVxdLTcDIpVZ/b6Lc+2G0OIgQaNCSwRtsPxKLronpoDFcvlVpI6ibHx6QzEBIw4MhiU0XhXRzuV+Df+lDl62MnTQIDAQAB";
    private static PublicKey a;

    public static byte[] a(byte[] var0) {
        try{
            Cipher var6 = Cipher.getInstance("RSA");
            var6.init(Cipher.DECRYPT_MODE, a);
            return var6.doFinal(var0);
        }catch(Throwable thr){
            thr.printStackTrace();
        }
        return null;
    }

    private static PublicKey a(String var0) {
        try{
            byte[] var6 = Base64.getDecoder().decode(var0);
            X509EncodedKeySpec var7 = new X509EncodedKeySpec(var6);
            KeyFactory var8 = KeyFactory.getInstance("RSA");
            return var8.generatePublic(var7);
        }catch(Throwable thr){
            thr.printStackTrace();
        }
        return null;
    }

    static {
        a = a(SECRET_KEY);
    }
}
