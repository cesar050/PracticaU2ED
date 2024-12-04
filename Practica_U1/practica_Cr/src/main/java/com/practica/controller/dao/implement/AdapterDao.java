package com.practica.controller.dao.implement;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.Scanner;
import com.practica.controller.tda.list.LinkedList;
import com.google.gson.Gson;

public class AdapterDao<T> implements InterfazDao<T> {
    @SuppressWarnings("FieldMayBeFinal")
    private Class<T> clazz;
    private Gson g;
    
    public static String URL = "media" + File.separator;

    public AdapterDao(Class clazz) {
        this.clazz = clazz;
        this.g = new Gson();
    }
    
    @Override
    public void persist(T object) throws Exception {
        LinkedList<T> list = listAll();
        list.add(object); 
        String info = g.toJson(list.toArray());
        saveFile(info);
    }
        
    @Override
    public void merge(T object, Integer index) throws Exception {
        LinkedList<T> list = listAll();
        T[] temp = (T[]) list.toArray();
        if (index <= 0 || index > temp.length) {
            throw new IndexOutOfBoundsException("indice fuera de limites");
        }
        T actual = temp[index - 1];
        if (actual == null) {
            throw new Exception("No existe el objeto en la posicion");
        }
        list.update(object, index -  1);
        String data = g.toJson(list.toArray());
        saveFile(data);
    }
    @Override
    public LinkedList listAll()  {
        LinkedList list = new LinkedList<>();
        try {
            String data = readFile();

            T[] matrix = (T[])g.fromJson(data, java.lang.reflect.Array.newInstance(clazz, 0).getClass());
            list.toList(matrix);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public T get(Integer id) throws Exception {
        T obj = null;
        LinkedList<T> list = listAll();
        if (!list.isEmpty()) {
            T[] matrix = list.toArray();
            int inicio = 0;
            int fin = matrix.length - 1;
            while (inicio <= fin) {
                int medio = (inicio + fin) / 2;
                obj = matrix[medio];
                Integer ident = getIdent(obj);
                if (ident.equals(id)) {
                    return obj;
                } else if (ident < id) {
                    inicio = medio + 1;
                } else {
                    fin = medio - 1;
                }
            }
        }
        return obj;
    }

    private Integer getIdent(T obj) throws Exception {
        try {
            Method method = null;
            for (Method m : clazz.getMethods()) {
                if (m.getName().equalsIgnoreCase("getId")) {
                    method = m;
                    break;
                }
            }
            if (method == null) {
                for (Method m : clazz.getSuperclass().getMethods()) {
                    if (m.getName().equalsIgnoreCase("getId")) {
                        method = m;
                        break;
                    }
                }
            }
            if (method != null) {
                return (Integer) method.invoke(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }
    @Override
    public void delete(Integer index) throws Exception {
        LinkedList<T> list = listAll();
        list.delete(index - 1);
        String info = g.toJson(list.toArray());
        saveFile(info);
    }

    @SuppressWarnings("ConvertToTryWithResources")
    private String readFile() throws Exception {
        Scanner in = new Scanner(new FileReader(URL + clazz.getSimpleName() + ".json"));
        StringBuilder sb = new StringBuilder();
        while (in.hasNext()) {
            sb.append(in.nextLine());
        }
        in.close();
        return sb.toString();
    }

    private void saveFile(String data) throws Exception {
        FileWriter f = new FileWriter(URL + clazz.getSimpleName() + ".json");
        f.write(data);
        f.flush();
        f.close();
    }
}

