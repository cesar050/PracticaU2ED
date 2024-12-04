package com.practica.controller.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.practica.controller.dao.implement.AdapterDao;
import com.practica.controller.tda.list.LinkedList;
import com.practica.controller.tda.stack.Stack;
import com.practica.eventos.EventoCrud;
import com.practica.eventos.TipoCrud;
import com.practica.models.Familia;

@SuppressWarnings("FieldMayBeFinal")

public class EventoCrudDao extends AdapterDao<EventoCrud> {
    private EventoCrud eventoCrud;
    private LinkedList<EventoCrud> listAll;

    public void setListAll(LinkedList<EventoCrud> listAll) {
        this.listAll = listAll;
    }

    private Gson g = new Gson();

    public EventoCrudDao() {
        super(EventoCrud.class);
    }

    public EventoCrud getEventoCrud() {
        if (this.eventoCrud == null) {
            this.eventoCrud = new EventoCrud();
        }
        return this.eventoCrud;
    }

    public void setEventoCrud(EventoCrud eventoCrud) {
        this.eventoCrud = eventoCrud;
    }

    public LinkedList<EventoCrud> getAllEventosCrud() throws Exception {
        if (listAll == null) {
            listAll = listAll();
        }
        return listAll;
    }

    public LinkedList<EventoCrud> listLastEvent(Integer limit) throws Exception {
        LinkedList<EventoCrud> list = listAll();
        Stack<EventoCrud> last = new Stack<>(limit);
        EventoCrud[] array = list.toArray();

        for (EventoCrud ev : array) {
            if (ev == null) {
                continue;
            }

            if (last.getSize().equals(limit)) {
                Stack<EventoCrud> temp = new Stack<>(limit);
                while (last.getSize() != 0) {
                    temp.push(last.pop());
                }
                temp.pop();
                while (temp.getSize() != 0) {
                    last.push(temp.pop());
                }
            }

            last.push(ev);
        }

        LinkedList<EventoCrud> result = new LinkedList<>();
        while (last.getSize() != 0) {
            result.add(last.pop());
        }

        return result;
    }

    public Boolean save() throws Exception {
        Integer id = getAllEventosCrud().getSize() + 1;
        getEventoCrud().setId(id);
        this.persist(this.eventoCrud);
        listAll = null;
        return true;
    }

    public Boolean registrarEvento(TipoCrud tipo, String mensaje) throws Exception {
        String horaFecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        EventoCrud evento = new EventoCrud(tipo, mensaje, horaFecha);
        setEventoCrud(evento);
        return save();
    }

    public String toJson() throws Exception {
        return g.toJson(this.eventoCrud);
    }

    public EventoCrud getEventoCrudById(Integer id) throws Exception {
        return get(id);
    }

    public String getEventoCrudJsonById(Integer id) throws Exception {
        return g.toJson(getEventoCrudById(id));

    }

    public EventoCrud getEventoCrudByIndex(Integer index) throws Exception {
        return get(index);
    }

    public String getEventoCrudJsonByIndex(Integer index) throws Exception {
        return g.toJson(get(index));
    }

    public TipoCrud getTipoEvento(String tipoEvento) {
        return TipoCrud.valueOf(tipoEvento);
    }

    public TipoCrud[] getTipoEvento() {
        return TipoCrud.values();
    }

     public LinkedList<Familia> ordenar(String atribute, Integer type, String metodo) throws Exception{
        LinkedList<Familia> lista = listAll();
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
