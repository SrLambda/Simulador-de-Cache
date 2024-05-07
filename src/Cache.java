import java.util.ArrayList;

public class Cache {

    private int tamBloque;
    private int tamCache;
    private int tamGrupo;
    private int tamLinea;

    private boolean write_allocate;
    private boolean write_through;

    private ArrayList direccionesDatos;
    private ArrayList direccionesInstrucciones;
    private ArrayList orden_de_usos;

    private int num_ref_instrucciones;
    private int num_ref_datos;
    private int num_fal_instrucciones;
    private int num_fal_datos;
    private int lecturas_ram;
    private int escritura_ram;
    private int tiempo;

    Cache(int bs,int cs,int gs,boolean  wa,boolean wt,boolean split){

        this.tamBloque = (int) ( Math.log(bs * 4) / Math.log(2) ); // Tamaño de bloque

        this.tamLinea  = (int)     ( Math.log(gs) / Math.log(2) ); // Tamaño de linea

        this.tamCache = cs;  // Tamaño de cache

        this.tamGrupo = gs;  // Tamaño de grupo 
                             // (Con esto se decide si es DirectMap., FullyAsso. o SetAsso.)


        if(split)
        {
        
            this.direccionesDatos = new ArrayList<Direccion>();
            this.direccionesInstrucciones = new ArrayList<Direccion>();

            for(int i=0; i < (this.tamCache/2) ; i++)
            {
                this.direccionesDatos.add(new Direccion());
                this.direccionesInstrucciones.add(new Direccion());

            }

        }
        else
        {

            this.direccionesDatos = new ArrayList<Direccion>();
            this.direccionesInstrucciones = this.direccionesDatos;

            for(int i=0; i < (this.tamCache) ; i++)
            {
                this.direccionesDatos.add(new Direccion());

            }

        }


        this.num_ref_instrucciones = 0;
        this.num_fal_instrucciones = 0;
        this.num_ref_datos         = 0;
        this.num_fal_datos         = 0;
        this.lecturas_ram          = 0;
        this.escritura_ram         = 0;
        this.tiempo                = 0; 

    }

    public void accion(int accion,String direccion)
    {

        int linea = this.makeLine(direccion);
        int   tag = this.makeTag(direccion);

        
        switch(accion)
        {

            case 0:  // leer dato

                break;

            case 1:  // escribir dato

                break;

            case 2:  // leer instrucción

                break;
        }
    }

    private int makeTag(String direccion){

        StringBuilder tag = new StringBuilder();

        int fin = direccion.length() - 1 - this.tamBloque - this.tamLinea ; 

        for (int index = 0; index < fin; index++) {

            tag.append(direccion.charAt(index));
            
        }

        return ConversorNumerico.deBinADec(tag.toString());
    }

    private int makeLine(String direccion){

        StringBuilder line = new StringBuilder();

        int inicio = direccion.length() - 1 - this.tamBloque - this.tamLinea ;
        int fin    = direccion.length() - 1 - this.tamBloque;

        for (int index = inicio; index < fin; index++) {

            line.append(direccion.charAt(index));
            
        }

        return ConversorNumerico.deBinADec(line.toString());
    }

    public void impFin(){

    }
    
}
