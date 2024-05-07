
public class Main {
    public static void main(String[] args) {

        String hex = "aaaa";
        String bin = ConversorNumerico.deHexABin(hex);
        int    dec = ConversorNumerico.deBinADec(bin);

        System.out.println(hex);
        System.out.println(bin);
        System.out.println(dec);

    }
}