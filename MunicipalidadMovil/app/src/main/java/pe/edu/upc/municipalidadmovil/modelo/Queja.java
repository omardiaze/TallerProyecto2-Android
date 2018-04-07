package pe.edu.upc.municipalidadmovil.modelo;

/**
 * Created by usuario on 16/02/2018.
 */
 
public class Queja {

    public Queja(int id, String tipo, String descripcion, String imagen, String correo, String fecreg, String estado, String direccion, String latitud, String longitud) {
        Id = id;
        Tipo = tipo;
        Descripcion = descripcion;
        Imagen = imagen;
        Correo = correo;
        Fecreg = fecreg;
        Estado = estado;
        Direccion = direccion;
        Latitud = latitud;
        Longitud = longitud;
    }

    private int Id;
    private String Tipo;
    private String Descripcion;
    private String Imagen;
    private String Correo;
    private String Fecreg;
    private String Estado;
    private String Direccion;
    private String Latitud;
    private String Longitud;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getFecreg() {
        return Fecreg;
    }

    public void setFecreg(String fecreg) {
        Fecreg = fecreg;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }
}
