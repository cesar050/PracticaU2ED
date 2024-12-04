package com.practica.controller.dao.services;

import com.practica.controller.dao.GeneradorDao;
import com.practica.controller.tda.list.LinkedList;
import com.practica.models.Familia;
import com.practica.models.Generador;

public class GeneradorServices {
    private GeneradorDao obj;

    public GeneradorServices(){
        this.obj = new GeneradorDao();
    }

    public Boolean save() throws Exception{
        return obj.save();
    }

    public LinkedList listAll() throws Exception{
        return obj.getListAll();
    }

    public Generador getGenerador(){
        return obj.getGenerador();
    }

    public void setGenerador(Generador generador){
        obj.setGenerador(generador);
    }

    public Generador getGeneradorBy(Integer id) throws Exception{
        return obj.getGeneradorBy(id);
    }

    public String getGeneradorJsonByIdIndex(Integer index) throws Exception{
        return obj.getGeneradorJsonByIdIndex(index);
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }
    public LinkedList<Generador> OrdenarPorMertodo(String atribute, Integer type, String metodo)throws Exception{
        return obj.ordenar(atribute,type, metodo);
    }

    public LinkedList<Generador> filtrarPor(String attribute, Object value)throws Exception{
        return obj.filtrarListaPor(attribute, value);
    }

    public Generador buscarGeneradorPor(String attribute, Object value)throws Exception{
        return obj.buscarObjetoPor(attribute, value);
    }
}
