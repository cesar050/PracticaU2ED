package com.practica.controller.dao;

import java.lang.reflect.Method;

import com.google.gson.Gson;
import com.practica.controller.dao.implement.AdapterDao;
import com.practica.controller.dao.services.GeneradorServices;
import com.practica.controller.tda.list.LinkedList;
import com.practica.models.Familia;
import com.practica.models.Generador;

public class GeneradorDao  extends AdapterDao<Generador>{
    private Generador generador;
    private LinkedList<Generador> listAll;
    private Gson g = new Gson();

    public GeneradorDao(){
        super(Generador.class);
    }

    public Generador getGenerador(){
        if(generador == null){
            generador = new Generador();
        }
        return generador;
    }

    public void setGenerador(Generador generador){
        this.generador = generador;
    }

    public LinkedList<Generador> getListAll() throws Exception{
        if(listAll == null){
            this.listAll = listAll();
        }
        return listAll;
    }

    public Boolean save() throws Exception {
        Integer id = listAll().getSize() +1;
        getGenerador().setId(String.valueOf(id));
        this.persist(this.generador);
        this.listAll = listAll();
        return true;
    }

    public Boolean update() throws Exception{
        this.merge(generador, Integer.valueOf(generador.getId()));
        this.listAll = listAll();
        return true;
    }

    public Boolean delete() throws Exception{
        if(listAll == null){
            listAll = listAll();
        }
        this.delete(Integer.valueOf(generador.getId()));
        reindexIds();
        return true;
    }

    

    public void reindexIds()throws Exception{
        LinkedList<Generador> listAll = listAll();
        for(int i = 0; i < listAll.getSize(); i++){
            Generador ge = listAll.get(i);
            ge.setId(String.valueOf(i + 1));
            this.merge(ge, i +1);
        }
    
    }

    public Generador getGeneradorBy(Integer id) throws Exception{
        return get(id);
    }

    public String getGeneradorJsonByIdIndex(Integer index) throws Exception{
        return g.toJson(get(index));
    }

    public String toJsonAll()throws Exception{
        return g.toJson(getListAll());
    }

    public void setListAll(LinkedList<Generador> listAll){
        this.listAll = listAll;
    }


    public LinkedList<Generador> filtrarListaPor(String attribute, Object dato) throws Exception {
        LinkedList<Generador> lista = listAll();
        LinkedList<Generador> generadores = new LinkedList<>();

        if (!lista.isEmpty()) {
            Generador[] generadoresArray = lista.toArray();
            for (Generador p : generadoresArray) {
                if (getValue(p, attribute).toString()
                        .equalsIgnoreCase(dato.toString())) {
                    generadores.add(p);
                }
            }
        }
        return generadores;
    }
    public Generador buscarObjetoPor(String attribute, Object dato) throws Exception {
        LinkedList<Generador> lista = listAll();
        Generador p = null;

        if (!lista.isEmpty()) {
            Generador[] generador = lista.toArray();
            for (int i = 0; i < generador.length; i++) {
                if (getValue(generador[i], attribute).toString()
                        .equalsIgnoreCase(dato.toString())) {
                    p = generador[i];
                    break;
                }
            }
        }
        return p;
    }

    private Object getValue(Object object, String attribute) throws Exception {
        Method method = object.getClass()
                .getMethod("get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1));
        return method.invoke(object);
    }

    public LinkedList<Generador> ordenar(String atribute, Integer type, String metodo) throws Exception{
        LinkedList<Generador> lista = listAll();
        switch (metodo) {
            case "shell":
                return lista.shellSort(atribute, type);
            case "quick":
                return lista.quickSort(atribute, type);
            case "merge":
                return lista.mergeSort(atribute, type);
            default:
                break;
        }
        lista.shellSort(atribute, type);
        return lista;
    }

}