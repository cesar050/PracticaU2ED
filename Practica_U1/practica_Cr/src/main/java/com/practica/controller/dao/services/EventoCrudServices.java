package com.practica.controller.dao.services;

import com.practica.controller.dao.EventoCrudDao;
import com.practica.controller.tda.list.LinkedList;
import com.practica.eventos.EventoCrud;
import com.practica.eventos.TipoCrud;
import com.practica.models.Familia;

public class EventoCrudServices {
    private final EventoCrudDao obj;

    public EventoCrudServices() {
        obj = new EventoCrudDao();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean registrarEvento(TipoCrud tipo, String mensaje) throws Exception {
        return obj.registrarEvento(tipo, mensaje);
    }

    public LinkedList<EventoCrud> getAllEventosCrud() throws Exception {
        return obj.getAllEventosCrud();
    }

    public void setEventoCrud(EventoCrud eventoCrud) {
        obj.setEventoCrud(eventoCrud);
    }

    public String toJson() throws Exception {
        return obj.toJson();
    }

    public EventoCrud getEventoCrud() {
        return obj.getEventoCrud();
    }

    public EventoCrud getEventoCrudById(Integer id) throws Exception {
        return obj.getEventoCrudById(id);
    }

    public String getEventoCrudJsonById(Integer id) throws Exception {
        return obj.getEventoCrudJsonById(id);
    }

    public LinkedList<EventoCrud> getListAll() {
        return obj.listAll();
    }

    public void setListAll(LinkedList<EventoCrud> listAll) {
        obj.setListAll(listAll);
    }

    public LinkedList<EventoCrud> listLastEvent(Integer limit) throws Exception {
        return obj.listLastEvent(limit);
    }

    public LinkedList<Familia> OrdenarPorMertodo(String atribute, Integer type, String metodo)throws Exception{
        return obj.ordenar(atribute, type, metodo);
    }
}
