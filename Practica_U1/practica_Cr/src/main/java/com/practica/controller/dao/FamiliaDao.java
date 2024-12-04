package com.practica.controller.dao;

import java.lang.reflect.Method;

import com.google.gson.Gson;
import com.practica.controller.dao.implement.AdapterDao;
import com.practica.controller.dao.services.GeneradorServices;
import com.practica.controller.tda.list.LinkedList;
import com.practica.models.Familia;
import com.practica.models.Generador;

@SuppressWarnings("FieldMayBeFinal")

public class FamiliaDao extends AdapterDao<Familia> {
    private Familia familia;
    private LinkedList<Familia> listAll;
    private Gson g = new Gson();

    public FamiliaDao() {
        super(Familia.class);
    }

    public Familia getFamilia() {
        if (familia == null) {
            familia = new Familia();
        }
        return familia;
    }

    public void setFamilia(Familia familia) {
        this.familia = familia;
    }

    public LinkedList<Familia> getListAll() throws Exception {
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll;
    }

    public Boolean save() throws Exception {
        Integer id = listAll().getSize() + 1;
        getFamilia().setId(Integer.valueOf(id));
        this.persist(this.familia);
        this.listAll = listAll();
        return true;
    }

    public Boolean update() throws Exception {
        this.merge(familia, Integer.valueOf(familia.getId()));
        this.listAll = listAll();
        return true;
    }

    public Boolean delete() throws Exception {
        if (listAll == null) {
            listAll = listAll();
        }
        this.delete(Integer.valueOf(familia.getId()));
        reindexIds();
        return true;
    }

    public LinkedList listaPosibles(Integer index) throws Exception {
        GeneradorServices gs = new GeneradorServices();
        LinkedList listita = gs.listAll();
        Generador[] aux = (Generador[]) listita.toArray();
        LinkedList list = new LinkedList<>();
        Familia f = getFamiliaById(index);
        for (Generador g : aux) {
            if (g.getPrecio() <= f.getPresupuesto())
                list.add(g);
        }
        return list;
    }

    public void reindexIds() throws Exception {
        LinkedList<Familia> listAll = listAll();
        for (int i = 0; i < listAll.getSize(); i++) {
            Familia fa = listAll.get(i);
            fa.setId(Integer.valueOf(i + 1));
            this.merge(fa, i + 1);
        }
    }

    public LinkedList<Familia> listaFamiliasConGenerador() throws Exception {
        LinkedList<Familia> listAll = listAll();
        LinkedList<Familia> list = new LinkedList<>();
        for (int i = 0; i < listAll.getSize(); i++) {
            Familia fa = listAll.get(i);
            if (fa.getTieneGenerador()) {
                list.add(fa);
            }
        }
        return list;
    }

    public Familia getFamiliaById(Integer id) throws Exception {
        return get(id);
    }

    public String getFamiliaJsonByIdIndex(Integer index) throws Exception {
        return g.toJson(get(index));
    }

    public String toJsonAll() throws Exception {
        return g.toJson(getListAll());
    }

    public void setListAll(LinkedList<Familia> listAll) {
        this.listAll = listAll;
    }
    /*public LinkedList<Familia> filtrarListaPor(String attribute, Object dato) throws Exception {
        LinkedList<Familia> lista = listAll();
        LinkedList<Familia> familias = new LinkedList<>();

        if (!lista.isEmpty()) {
            Familia[] familiasArray = lista.toArray();
            for (Familia p : familiasArray) {
                if (getValue(p, attribute).toString()
                        .equalsIgnoreCase(dato.toString())) {
                    familias.add(p);
                }
            }
        }
        return familias;
    }*/

    public LinkedList<Familia> filtrarListaPor(String attribute, Object dato) throws Exception {
        // Ordenar la lista por el atributo
        LinkedList<Familia> lista = this.ordenar(attribute, 1, "quick"); // Orden ascendente por defecto
        LinkedList<Familia> resultList = new LinkedList<>();
    
        // Realizar búsqueda binaria lineal
        int left = 0, right = lista.getSize() - 1;
        int firstOccurrence = -1;
    
        while (left <= right) {
            int mid = (left + right) / 2;
            Familia midValue = lista.get(mid);
    
            if (getValue(midValue, attribute).toString().equalsIgnoreCase(dato.toString())) {
                if (mid == 0 || !getValue(lista.get(mid - 1), attribute).toString().equalsIgnoreCase(dato.toString())) {
                    firstOccurrence = mid;
                    break;
                } else {
                    right = mid - 1;
                }
            } else if (getValue(midValue, attribute).toString().compareToIgnoreCase(dato.toString()) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    
        if (firstOccurrence == -1) {
            return resultList; 
        }
    
        // Recopilar todas las ocurrencias consecutivas
        for (int i = firstOccurrence; i < lista.getSize(); i++) {
            Familia current = lista.get(i);
            if (getValue(current, attribute).toString().equalsIgnoreCase(dato.toString())) {
                resultList.add(current);
            } else {
                break;
            }
        }
    
        return resultList;
    }
    
    public Familia buscarObjetoPor(String attribute, Object dato) throws Exception {
        LinkedList<Familia> lista = listAll();
        Familia p = null;

        if (!lista.isEmpty()) {
            Familia[] familias = lista.toArray();
            for (int i = 0; i < familias.length; i++) {
                if (getValue(familias[i], attribute).toString()
                        .equalsIgnoreCase(dato.toString())) {
                    p = familias[i];
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
                return lista;
        }
    }
}
