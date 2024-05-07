import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        int tamCache  =  256;
        int tamBloque =  2;
        int tipocache = -1;
        int cantGrupo =  1;

        boolean writeThrough  = false;
        boolean writeAllocate = true;
        boolean split         = false;
 
        try {

            for (int i = 0; i < args.length - 1; i++)
            {

                switch (args[i])
                {
                    case "-bs":

                        tamBloque = Integer.parseInt(args[i + 1]);
                        break;

                    case "-cs":

                        tamCache  = Integer.parseInt(args[i + 1]);
                        cantGrupo = 1;
                        break;

                    case "-wt":

                        writeThrough = true;
                        break;

                    case "-fa":

                        if(tipocache==-1)
                        {

                            tipocache = 0;
                            cantGrupo = tamCache;

                        }

                        break;

                    case "-sa":

                        if(tipocache==-1)
                        {

                            tipocache = 1;
                            cantGrupo = Integer.parseInt(args[i + 1]);

                        }

                        break;

                    case "-wna":

                        if(writeAllocate)
                        {

                            writeAllocate = false;

                        }

                        break;

                    case "-split":
                        
                        if(!split)
                        {

                            split = true;

                        }
                        break;
                }
            }

        }
        catch (Exception e)
        {
            System.out.println("Error al leer los argumentos");
        }


        Cache cache = new Cache(tamBloque, tipocache, cantGrupo, writeAllocate, writeThrough , split);


        String archivoPath = args[args.length-1];
        String delimitador = " ";

        int accion;
        String direccion;



        try (BufferedReader lector = new BufferedReader(new FileReader(archivoPath)))
        {
            String linea;

            while ((linea = lector.readLine()) != null)
            {

                String[] tokens = linea.split(delimitador);                // lee linea
                accion          = Integer.parseInt(tokens[0]);             // obtiene acción
                direccion       = ConversorNumerico.deHexABin(tokens[1]);  // obtiene direccion 
                
                cache.accion(accion, direccion);

            }

        } catch (IOException e)
        {

            System.err.println("Ocurrió un error al leer el archivo.");
            e.printStackTrace();

        }

        cache.impFin();

    }
}