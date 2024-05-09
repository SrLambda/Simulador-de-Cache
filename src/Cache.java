import java.util.ArrayList;

public class Cache {

    private final int tamBloque;
    private final int tamCache;
    private final int tamGrupo;
    private final int tamLinea;
    private final int cantGrupos;

    private boolean write_allocate;
    private boolean write_through;

    private final ArrayList<GrupoLineasCache> direccionesDatos;
    private final ArrayList<GrupoLineasCache> direccionesInstrucciones;

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


        this.cantGrupos = (this.tamCache/this.tamGrupo);

        if(split)
        {

            // ----   CREA LA CACHE CON SPLIT   ----

            this.direccionesDatos = new ArrayList<GrupoLineasCache>();
            this.direccionesInstrucciones = new ArrayList<GrupoLineasCache>();

            for(int i=0; i < (this.cantGrupos / 2) ; i++)
            {
                this.direccionesDatos.add(new GrupoLineasCache(this.tamGrupo));
                this.direccionesInstrucciones.add(new GrupoLineasCache(this.tamGrupo));

            }

        }
        else
        {

            // ----   CREA LA CACHE SIN SPLIT   ----

            this.direccionesDatos = new ArrayList<GrupoLineasCache>();
            this.direccionesInstrucciones = this.direccionesDatos;

            for(int i=0; i < (this.cantGrupos) ; i++)
            {
                this.direccionesDatos.add(new GrupoLineasCache(this.tamGrupo));

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


    public void impFin()
    {

    }


    public void accion(int accion,String direccion)
    {

        int grupo = this.obtenerGrupo(direccion);
        int   tag = this.obtenerTag(direccion);



        Linea linea;  // Linea de cache que se quiere utilizar



        switch(accion)
        {

            case 0:  // leer dato


                // Busca el tag
                linea = this.buscarDireccion(grupo,tag,false);



                if(linea == null) // MISS
                {


                    this.num_fal_datos++;
                    this.lecturas_ram++;
                    this.tiempo += 100;


                    // Agregar en caché
                    linea = this.direccionesDatos.get(grupo).agregarLinea(tag);



                    // No hay espacio en Caché
                    if(linea == null)
                    {

                        // Elimina una linea
                        linea = this.direccionesDatos.get(grupo).eliminarLinea();


                        // Write-Back
                        if(!this.write_through && linea.esDirty())
                        {
                            this.escritura_ram++;
                            this.tiempo += 100;
                        }


                        // Agregar en caché
                        this.direccionesDatos.get(grupo).agregarLinea(tag);

                    }


                }


                this.num_ref_datos++; //  HIT
                this.tiempo += 5;     //  TIEMPO

                break;

            case 1:  // escribir dato


                // Busca el tag
                linea = this.buscarDireccion(grupo,tag,false);



                // MISS
                if(linea == null)
                {

                    this.num_fal_datos++;
                    this.lecturas_ram++;
                    this.tiempo += 100;


                    if(this.write_allocate)
                    {

                        // WRITE ALLOCATE

                        this.escritura_ram++;
                        this.tiempo += 100;

                    }
                    else
                    {

                        // WRITE NO ALLOCATE



                        // Agregar en caché
                        linea = this.direccionesDatos.get(grupo).agregarLinea(tag);



                        // No hay espacio en Caché
                        if(linea == null)
                        {

                            // Elimina una linea
                            linea = this.direccionesDatos.get(grupo).eliminarLinea();


                            // Write-Back
                            if(!this.write_through && linea.esDirty())
                            {
                                this.escritura_ram++;
                                this.tiempo += 100;
                            }


                            // Agregar en caché
                            this.direccionesDatos.get(grupo).agregarLinea(tag);


                        }

                    }
                }



                if(this.write_through && !this.write_allocate) // Evitar escribir 2 veces
                {
                    // Escribe a Ram

                    this.escritura_ram++;
                    this.tiempo += 100;

                }
                else if (linea != null)
                {
                    // Se marca Dirty

                    linea.escritura();


                    //  HIT
                    this.num_ref_datos++;
                    this.tiempo += 5;

                }

                break;

            case 2:  // leer instrucción


                // Busca el tag
                linea = this.buscarDireccion(grupo,tag,true);


                // MISS
                if(linea == null)
                {

                    this.num_fal_instrucciones++;
                    this.lecturas_ram++;
                    this.tiempo += 100;


                    // Agregar en caché
                    linea = this.direccionesInstrucciones.get(grupo).agregarLinea(tag);


                    // No hay espacio en Caché
                    if(linea == null)
                    {

                        // Elimina una linea
                        linea = this.direccionesInstrucciones.get(grupo).eliminarLinea();


                        // Write-Back
                        if(!this.write_through && linea.esDirty())
                        {
                            this.escritura_ram++;
                            this.tiempo += 100;
                        }


                        // Agregar en caché
                        this.direccionesInstrucciones.get(grupo).agregarLinea(tag);

                    }

                }


                //  HIT
                this.num_ref_instrucciones++;
                this.tiempo += 5;


                break;
        }
    }

    // Buscar direccion

    private Linea buscarDireccion( int grupo , int tag , boolean esInstruccion)
    {

        Linea linea;

        if(esInstruccion)
        {
            linea = this.direccionesInstrucciones.get(grupo).buscarLinea(tag);
        }
        else
        {
            linea = this.direccionesDatos.get(grupo).buscarLinea(tag);
        }

        return linea;
    }






    // Funciones para descomponer direcciones

    private int obtenerTag(String direccion){

        StringBuilder tag = new StringBuilder();

        int fin = direccion.length() - 1 - this.tamBloque - this.tamLinea ; 

        for (int index = 0; index < fin; index++)
        {

            tag.append(direccion.charAt(index));
            
        }

        return ConversorNumerico.deBinADec(tag.toString());
    }

    private int obtenerGrupo(String direccion){

        StringBuilder line = new StringBuilder();

        int inicio = direccion.length() - 1 - this.tamBloque - this.tamLinea ;
        int fin    = direccion.length() - 1 - this.tamBloque;

        for (int index = inicio; index < fin; index++)
        {

            line.append(direccion.charAt(index));
            
        }

        int dirMemoriaPrincipal = ConversorNumerico.deBinADec(line.toString());

        return (dirMemoriaPrincipal % this.cantGrupos);
    }


    
}
