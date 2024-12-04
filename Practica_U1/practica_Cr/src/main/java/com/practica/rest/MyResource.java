package com.practica.rest;

import com.practica.controller.tda.list.LinkedList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("myresource")
public class MyResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        
        int tiempoInicio;
        int tiempoFinal;
        int tiempoTotal;
        int busqueda;
        LinkedList<Integer> lista = ListaRandom(10000);
        LinkedList<Integer> lista2 = lista.duplicar_lista();
        LinkedList<Integer> lista3 = lista.duplicar_lista();
        //LinkedList<Integer> lista4 = lista.duplicar_lista();
        LinkedList<Integer> lista5 = lista.duplicar_lista();
        
        System.out.println("======================================================");
        // Medición de ShellSort
        tiempoInicio = (int) System.currentTimeMillis();
        lista.shellSort(1);
        tiempoFinal = (int) System.currentTimeMillis();
        tiempoTotal = tiempoFinal - tiempoInicio;
        System.out.println("------------------------------------------------------");
        System.out.println("ShellSort 1: " + tiempoTotal + " milisegundos");
        System.out.println("------------------------------------------------------");

        // Medición de QuickSort
        tiempoInicio = (int) System.currentTimeMillis();
        lista2.quickSort(1);
        tiempoFinal = (int) System.currentTimeMillis();
        tiempoTotal = tiempoFinal - tiempoInicio;
        System.out.println("------------------------------------------------------");
        System.out.println("QuickSort 1: " + tiempoTotal + " milisegundos");
        System.out.println("------------------------------------------------------");

        // Medición de MergeSort
        tiempoInicio = (int) System.currentTimeMillis();
        lista3.mergeSort(1);
        tiempoFinal = (int) System.currentTimeMillis();
        tiempoTotal = tiempoFinal - tiempoInicio;
        System.out.println("------------------------------------------------------");
        System.out.println("MergeSort 1: " + tiempoTotal + " milisegundos");
        System.out.println("------------------------------------------------------");

        System.out.println("======================================================");
        System.out.println("======================================================");

        // Medición de búsqueda
        Integer value = (int) (Math.random() * 60000);

        // Búsqueda lineal
        lista5.shellSort(1);
        tiempoInicio = (int) System.nanoTime();
        busqueda = lista5.linearSearch(value);
        tiempoFinal = (int) System.nanoTime();
        tiempoTotal = tiempoFinal - tiempoInicio;
        System.out.println("------------------------------------------------------");
        System.out.println("LinearSearch : " + tiempoTotal + " ns en posicion: " + busqueda);
        System.out.println("------------------------------------------------------");

        // Ordenar con shellSort antes de la búsqueda binaria

        tiempoInicio = (int) System.nanoTime();
        busqueda = lista5.binarySarch(value);
        tiempoFinal = (int) System.nanoTime();
        tiempoTotal = tiempoFinal - tiempoInicio;
        System.out.println("------------------------------------------------------");
        System.out.println("BinarySearch con mergeSort : " + tiempoTotal + " ns en posicion: " + busqueda);
        System.out.println("------------------------------------------------------");
        System.out.println("======================================================");
        // Mensaje de respuesta para el cliente
        return "Medicion completada correctamente";
    }

    // Generar lista con números aleatorios
    LinkedList<Integer> ListaRandom(Integer cantidad) {
        LinkedList<Integer> listitia = new LinkedList<>();
        for (int i = 0; i < cantidad; i++) {
            listitia.add((int) (Math.random() * 100000));
        }
        return listitia;
    }
}
