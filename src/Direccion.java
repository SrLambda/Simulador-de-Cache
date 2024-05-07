public class Direccion {
    
    private int tag;
    private boolean dirty;

    Direccion(){

        this.tag = -1;
        this.dirty = false;

    }


    public void escritura()
    {
        if(!this.dirty)
        {
            this.dirty = true;
        }
    }

    public void reemplazarTag(int nuevo_tag){

        if(this.dirty)
        {
            this.dirty = false;
        }

        this.tag = nuevo_tag;
    }


    public int getTag(){
        return tag;
    }
}
