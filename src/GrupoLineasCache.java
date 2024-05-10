import java.util.ArrayList;

public class GrupoLineasCache {

    private ArrayList<Linea> lineas;
    private ArrayList<Linea> ordenUso;


    GrupoLineasCache(int _cantidadLineas)
    {

        this.lineas     = new ArrayList<>();
        this.ordenUso   = new ArrayList<>();


        // Agrega las Lineas
        for (int i = 0; i < _cantidadLineas; i++)
        {

            this.lineas.add(new Linea());

        }

    }


    // Buscar Linea en el grupo (de forma lineal)
    public Linea buscarLinea(int tag){

        for (Linea l: this.lineas)
        {

            if( l.getTag() == tag)
            {

                this.usoLinea(l); // Marca uso de Linea
                return l;

            }
        }

        return null;

    }




    public Linea agregarLinea(int tag){

        for (Linea l: this.lineas) {

            if(l.getTag() == -1) // Encuentra línea vacia
            {
                l.reemplazarTag(tag);
                this.usoLinea(l);
                return l;
            }

        }

        return null; // Caché llena
    }





    public Linea eliminarLinea()
    {

        Linea lineaEliminada = this.ordenUso.get(0);

        for (int i=0 ; i < this.lineas.size() ; i ++) {

            if(this.ordenUso.get(0) == this.lineas.get(i))
            {

                this.ordenUso.remove(0);   // Elimina linea de los usos

                this.lineas.remove(i);           // Elimina la Linea de Caché
                this.lineas.add(i,new Linea());  // Agrega una linea limpia en su lugar

                return lineaEliminada;
            }

        }

        return null;
    }





    private void usoLinea(Linea linea)
    {


        int posibleIndice = this.lineaHaSidoUsada(linea);

        if(posibleIndice != -1)
        {

            this.ordenUso.remove(posibleIndice);

        }


        this.ordenUso.add(linea);



    }

    private int lineaHaSidoUsada(Linea linea){

        for (int i=0 ; i < this.ordenUso.size() ; i++)
        {

            if(this.ordenUso.get(i) == linea)
            {
                return i;
            }

        }

        return -1;
    }


}
