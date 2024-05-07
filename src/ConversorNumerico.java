import java.lang.reflect.Array;

public class ConversorNumerico {


    // Conversion de hexadecimal a binario
    public static String deHexABin(String hexadecimal)  // Ej: 4a1e
    {

        StringBuilder binario = new StringBuilder();

        for (int i = 0; i < hexadecimal.length(); i++)
        {

           binario.append(digHexABin(hexadecimal.charAt(i)));

        }


        return binario.toString();
    }

    public static int deBinADec(String binario)
    {
        int decimal = 0;
        int largo = binario.length();

        for (int i = 0; i < largo; i++)
        {

            if(binario.charAt(largo-1-i) == '1')
            {
                decimal += (int) (Math.pow(2,i));
            }

        }

        return decimal;

    }



    // ==================== Funciones Auxiliares ========================== //

    private static String digHexABin(char digito){

        switch (digito){

            case '0':
                return "0000";

            case '1':
                return "0001";

            case '2':
                return "0010";

            case '3':
                return "0011";

            case '4':
                return "0100";

            case '5':
                return "0101";

            case '6':
                return "0110";

            case '7':
                return "0111";

            case '8':
                return "1000";

            case '9':
                return "1001";

            case 'a':
                return "1010";

            case 'b':
                return "1011";

            case 'c':
                return "1100";

            case 'd':
                return "1101";

            case 'e':
                return "1110";

            case 'f':
                return "1111";

            default:
                throw new IllegalArgumentException("Digito no hexadecimal");

        }
    }
}
