package pe.edu.upc.municipalidadmovil.modelo;

/**
 * Created by usuario on 16/02/2018.
 */
  
public class Usuario {
    public Usuario(int id, String nombre, String apePat, String apeMat, String dni, String direccion, String latitud, String longitud, String fecNac, String sexo, String correo, String imagen) {
        Id = id;
        Nombre = nombre;
        ApePat = apePat;
        ApeMat = apeMat;
        Dni = dni;
        Direccion = direccion;
        Latitud = latitud;
        Longitud = longitud;
        FecNac = fecNac;
        Sexo = sexo;
        Correo = correo;
        Imagen = imagen;
    }

    private int Id;
    private String Nombre;
    private String ApePat;
    private String ApeMat;
    private String Dni;
    private String Direccion;
    private String Latitud;
    private String Longitud;
    private String FecNac;
    private String Sexo;
    private String Correo;
    private String Imagen;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApePat() {
        return ApePat;
    }

    public void setApePat(String apePat) {
        ApePat = apePat;
    }

    public String getApeMat() {
        return ApeMat;
    }

    public void setApeMat(String apeMat) {
        ApeMat = apeMat;
    }

    public String getDni() {
        return Dni;
    }

    public void setDni(String dni) {
        Dni = dni;
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

    public String getFecNac() {
        return FecNac;
    }

    public void setFecNac(String fecNac) {
        FecNac = fecNac;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String sexo) {
        Sexo = sexo;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }
}
