package wimt.othertree.client;

/**
 * Created by Nick Cuthbert on 29/03/2016.
 */
class OtherTreeTypeUtil {
    static byte[] toByteArray(String hexString){
        return Base64.decode(hexString, Base64.DEFAULT);
    }

    static int[] toIntArray(byte[] bytes){
        final int[] ints = new int[bytes.length];
        for(int i=0;i<ints.length;i++){
            ints[i] = bytes[i]& 0xFF;
        }
        return ints;
    }

}
