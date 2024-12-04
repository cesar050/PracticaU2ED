package com.practica.controller.dao.services;

import com.practica.controller.dao.FamiliaDao;
import com.practica.controller.tda.list.LinkedList;
import com.practica.models.Familia;

public class FamiliaServices {
    private FamiliaDao obj;

    public FamiliaServices() {
        this.obj = new FamiliaDao();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public LinkedList listAll() throws Exception {
        return obj.getListAll();
    }

    public LinkedList obtenerPosibles(Integer index) throws Exception {
        return obj.listaPosibles(index);
    }

    public Familia getFamilia() {
        return obj.getFamilia();
    }

    public void setFamilia(Familia familia) {
        obj.setFamilia(familia);
    }

    public Familia getFamiliaById(Integer id) throws Exception {
        return obj.getFamiliaById(id);
    }

    public String getFamiliaJsonByIdIndex(Integer index) throws Exception {
        return obj.getFamiliaJsonByIdIndex(index);
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }

    public LinkedList<Familia> listTieneGerador() throws Exception {
        return obj.listaFamiliasConGenerador();
    }
    public LinkedList<Familia> OrdenarPorMertodo(String atribute, Integer type, String metodo)throws Exception{
        return obj.ordenar(atribute, type, metodo);
    }
    public LinkedList<Familia> filtrarPor(String attribute, Object value)throws Exception{
        return obj.filtrarListaPor(attribute, value);
    }

    public Familia buscarFamiliaPor(String attribute, Object value)throws Exception{
        return obj.buscarObjetoPor(attribute, value);
    }
}
