package com.practica.controller.tda.list;

import java.lang.reflect.Method;

import com.practica.controller.excepcion.ListEmptyException;

public class LinkedList <E>{
    private Node<E> head;
    private Node<E> tail;
    private Integer size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public Node<E> getHead() {
        return head;
    }

    public void setHead(Node<E> head) {
        this.head = head;
    }

    public Node<E> getTail() {
        return tail;
    }

    public void setTail(Node<E> tail) {
        this.tail = tail;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * Metodo verifica si la lista esta vacia
     * 
     * @return
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Metodo que agrega un elemento header a la lista
     * 
     * @param data
     */
    public void addHeader(E data) {
        Node<E> aux = new Node<>(data);
        if (isEmpty()) {
            head = aux;
            tail = head;
        } else {
            aux.setNext(head);
            head = aux;
        }
        size++;
    }

    private void addTail(E data) {
        Node<E> aux = new Node<>(data);
        if (isEmpty()) {
            head = aux;
            tail = head;
        } else {
            tail.setNext(aux);
            tail = aux;
        }
        size++;
    }

    public void add(E data) {
        addTail(data);
    }

    /**
     * metodo que agrega un elemento en la posicion index
     * 
     * @param data
     * @param index
     * @throws ListEmptyException
     * @throws IndexOutOfBoundsException
     */
    public void add(E data, Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Indice fuera de rango");
        }

        if (index == 0) {
            addHeader(data);
        } else if (index.intValue() == size) {
            addTail(data);
        } else {
            Node<E> search = getNode(index - 1);

            Node<E> aux = new Node<>(data);
            aux.setNext(search.getNext());
            search.setNext(aux);
            size++;
        }
    }

    private Node<E> getNode(Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista esta vacia");
        }
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indice fuera de rango");
        }
        if (index == size - 1) {
            return tail;
        }
        Node<E> search = head;
        Integer count = 0;
        while (count < index) {
            search = search.getNext();
            count++;
        }
        return search;
    }

    public E get(Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        return getNode(index).getData();
    }

    public void set(Integer index, E data) throws ListEmptyException, IndexOutOfBoundsException {
        getNode(index).setData(data);
    }

    public void reset() {
        head = null;
        tail = null;
        size = 0;
    }

    public E delete(Integer index) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista esta vacia");
        } else if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indice fuera de rango");
        } else if (index == 0) {
            return deleteHeader();
        } else if (index == size - 1) {
            return deleteTail();
        } else {
            Node<E> prevNode = getNode(index - 1);
            Node<E> actualNode = getNode(index);
            E element = prevNode.getData();
            Node<E> nextNode = actualNode.getNext();
            actualNode = null;
            prevNode.setNext(nextNode);
            size--;
            return element;
        }
    }

    public E deleteHeader() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista esta vacia");
        } else {
            E element = head.getData();
            Node<E> aux = head.getNext();
            head = aux;
            if (size == 1) {
                tail = null;
            }
            size--;
            return element;
        }
    }

    public E deleteTail() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista esta vacia");
        } else {
            E element = tail.getData();
            Node<E> aux = getNode(size - 2);
            if (aux == null) {
                head = null;
                tail = null;
                if (size == 2) {
                    tail = head;
                } else {
                    head = null;
                }
            } else {
                tail = null;
                tail = aux;
                tail.setNext(null);
            }
            size--;
            return element;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> search = head;
        while (search != null) {
            sb.append(search.getData());
            if (search.getNext() != null) {
                sb.append(" => ");
            }
            search = search.getNext();
        }
        return sb.toString();
    }

    public void update(E data, Integer post) throws ListEmptyException, IndexOutOfBoundsException {
        if (isEmpty()) {
            throw new ListEmptyException("La lista esta vacia");
        }
        if (post < 0 || post >= size) {
            throw new IndexOutOfBoundsException("Indice fuera de rango");
        } else if (post == 0) {
            head.setData(data);
        } else if (post == size - 1) {
            tail.setData(data);
        } else {
            Node<E> search = head;
            Integer count = 0;
            while (count < post) {
                count++;
                search = search.getNext();
            }
            search.setData(data);
        }
    }

    public LinkedList<E> duplicar_lista() {
        LinkedList<E> clonar = new LinkedList<>();
        Node<E> aux = head;
        while (aux != null) {
            clonar.add(aux.getData());
            aux = aux.getNext();
        }
        return clonar;
    }

    public LinkedList<E> order(String attribute, Integer type) throws Exception {
        if (!isEmpty()) {
            E data = this.head.getData();
            if (data instanceof Object) {
                E[] lista = this.toArray();
                reset();
                for (int i = 1; i < lista.length; i++) {
                    E aux = lista[i];
                    int j = i - 1;
                    while (j >= 0 && attributeCompare(attribute, lista[j], aux, type)) {
                        lista[j + 1] = lista[j--];

                    }
                    lista[j + 1] = aux;
                }
                this.toList(lista);
            }
        }
        return this;
    }

    private Boolean attributeCompare(String attribute, E a, E b, Integer type) throws Exception {
        return compare(existAttribute(a, attribute), existAttribute(b, attribute), type);
    }

    private Object existAttribute(E a, String attribute) throws Exception {
        Method method = null;
        attribute = attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
        attribute = "get" + attribute;
        for (Method m : a.getClass().getMethods()) {
            if (m.getName().contains(attribute)) {
                method = m;
                break;
            }
        }
        if (method == null) {
            for (Method m : a.getClass().getSuperclass().getMethods()) {
                if (m.getName().contains(attribute)) {
                    method = m;
                    break;
                }
            }
        }
        if (method != null) {
            return method.invoke(a);
        }

        return null;
    }

    private Boolean compare(Object a, Object b, Integer type) throws Exception {
        if(a == null || b == null){
            return false;
        }
        switch (type) {
            case 0:
                if (a instanceof Number) {
                    Number a1 = (Number) a;
                    Number b1 = (Number) b;
                    return a1.doubleValue() > b1.doubleValue();
                } else {
                    return (a.toString().toLowerCase()).compareTo(b.toString().toLowerCase()) > 0;
                }
            default:
                if (a instanceof Number) {
                    Number a1 = (Number) a;
                    Number b1 = (Number) b;
                    return a1.doubleValue() < b1.doubleValue();
                } else {
                    return (a.toString().toLowerCase()).compareTo(b.toString().toLowerCase()) < 0;
                }

        }
    }

    public E[] toArray() {
        E[] matrix = null;
        if (!isEmpty()) {

            Class clazz = head.getData().getClass();
            matrix = (E[]) java.lang.reflect.Array.newInstance(clazz, size);
            Node<E> aux = head;
            for (int i = 0; i < size; i++) {
                matrix[i] = aux.getData();
                aux = aux.getNext();
            }
        }
        return matrix;
    }

    public LinkedList<E> toList(E[] matrix) {
        reset();
        for (E matrix1 : matrix) {
            add(matrix1);
        }
        return this;
    }


    // ShellSort
    public LinkedList<E> shellSort(Integer type) {
        if (!isEmpty()) {
            E[] lista = this.toArray();
            reset();
            int n = lista.length;
            switch (type) {
                case 1:
                    for (int gap = n / 2; gap > 0; gap /= 2) {
                        for (int i = gap; i < n; i += 1) {
                            E temp = lista[i];
                            int j;
                            for (j = i; j >= gap
                                    && lista[j - gap].toString().compareTo(temp.toString()) > 0; j -= gap) {
                                lista[j] = lista[j - gap];
                            }
                            lista[j] = temp;
                        }
                    }
                    break;
                default:
                    for (int gap = n / 2; gap > 0; gap /= 2) {
                        for (int i = gap; i < n; i += 1) {
                            E temp = lista[i];
                            int j;
                            for (j = i; j >= gap
                                    && lista[j - gap].toString().compareTo(temp.toString()) < 0; j -= gap) {
                                lista[j] = lista[j - gap];
                            }
                            lista[j] = temp;
                        }
                    }
                    break;
            }
            this.toList(lista);
        }
        return this;
    }

    // QuilSort
    public LinkedList<E> quickSort(Integer type) {
        if (!isEmpty()) {
            E[] lista = this.toArray();
            reset();
            quickSort(lista, 0, lista.length - 1, type);
            this.toList(lista);
        }
        return this;
    }

    // Metodo para ordenar la lista con quicksort
    private void quickSort(E[] lista, Integer low, Integer high, Integer type) {
        if (low < high) {
            Integer pi = particion(lista, low, high, type);
            quickSort(lista, low, pi - 1, type);
            quickSort(lista, pi + 1, high, type);
        }
    }

    
    private Integer particion(E[] lista, Integer low, Integer high, Integer type) {
        E pivot = lista[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            Boolean condicion = (type == 1) ? (lista[j].toString().compareTo(pivot.toString()) < 0)
                    : (lista[j].toString().compareTo(pivot.toString()) > 0);
            if (condicion) {
                i++;
                E aux = lista[i];
                lista[i] = lista[j];
                lista[j] = aux;

            }
        }
        E aux = lista[i + 1];
        lista[i + 1] = lista[high];
        lista[high] = aux;
        return i + 1;
    }

    // Metodo para medir el tiempo de algoritmos
 
    public LinkedList<E> mergeSort(Integer type) {
        if (!isEmpty()) {
            E[] lista = this.toArray();
            reset();
            mergeSort(lista, 0, lista.length - 1, type);
            this.toList(lista);
        }
        return this;
    }

    private void mergeSort(E[] lista, Integer l, Integer r, Integer type) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(lista, l, m, type);
            mergeSort(lista, m + 1, r, type);
            merge(lista, l, m, r, type);
        }
    }

    private void merge(E[] lista, Integer l, Integer m, Integer r, Integer type) {
        int n1 = m - l + 1;
        int n2 = r - m;

        E L[] = (E[]) new Object[n1];
        E R[] = (E[]) new Object[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = lista[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = lista[m + 1 + j];

        int i = 0, j = 0;

        int k = l;
        while (i < n1 && j < n2) {
            Boolean condicion = (type == 1) ? (L[i].toString().compareTo(R[j].toString()) <= 0)
                    : (L[i].toString().compareTo(R[j].toString()) >= 0);

            if (condicion) {
                lista[k] = L[i];
                i++;
            } else {
                lista[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            lista[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            lista[k] = R[j];
            j++;
            k++;
        }
    }

    // Metodos busqueda datos primitivos
    public Integer binarySarch(E data) {
        E[] lista = this.toArray();
        Integer low = 0, high = lista.length - 1;
        while (low <= high) {
            Integer mid = low + (high - low) / 2;
            if (lista[mid].equals(data)) {
                return mid;
            }
            if (lista[mid].toString().compareTo(data.toString()) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }

    public Integer linearSearch(E data) {
        E[] lista = this.toArray();
        Integer positio = -1;
        for (int i = 0; i < lista.length; i++) {
            if (lista[i].equals(data)) {
                positio = i;
                break;
            }
        }
        return positio;
    }
    //metodos para ordenar por atributos
    public LinkedList<E> shellSort(String attribute, Integer type) throws Exception {
        if (!isEmpty()) {
            E[] lista = this.toArray();
            reset();
            int n = lista.length;

            for (int gap = n / 2; gap > 0; gap /= 2) {
                for (int i = gap; i < n; i++) {
                    E aux = lista[i];
                    int j = i;

                    while (j >= gap && attributeCompare(attribute, lista[j - gap], aux, type)) {
                        lista[j] = lista[j - gap];
                        j -= gap;
                    }

                    lista[j] = aux;
                }
            }

            this.toList(lista);
        }
        return this;
    }

    public LinkedList<E> quickSort(String attribute, Integer type) throws Exception {
        if (!isEmpty()) {
            E[] lista = this.toArray();
            reset();
            quickSort(lista, 0, lista.length - 1, attribute, type);
            this.toList(lista);
        }
        return this;
    }

    private void quickSort(E[] lista, Integer low, Integer high, String attribute, Integer type) throws Exception {
        if (low < high) {

            Integer pivot = particion(lista, low, high, attribute, type);

            quickSort(lista, low, pivot - 1, attribute, type);
            quickSort(lista, pivot + 1, high, attribute, type);
        }
    }

    private Integer particion(E[] lista, Integer low, Integer high, String attribute, Integer type) throws Exception {
        E pivot = lista[high];

        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (attributeCompare(attribute, lista[j], pivot, type)) {
                i++;
                E aux = lista[i];
                lista[i] = lista[j];
                lista[j] = aux;
            }
        }

        E aux = lista[i + 1];
        lista[i + 1] = lista[high];
        lista[high] = aux;

        return i + 1;
    }

    public LinkedList<E> mergeSort(String atribute, Integer type) throws Exception {
        if (!isEmpty()) {
            E[] lista = this.toArray();
            reset();
            mergeSort(lista, 0, lista.length - 1, atribute, type);
            this.toList(lista);
        }
        return this;
    }

    private void mergeSort(E[] lista, Integer l, Integer r, String attribute, Integer type) throws Exception {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(lista, l, m, attribute, type);
            mergeSort(lista, m + 1, r, attribute, type);
            merge(lista, l, m, r, attribute, type);
        }
    }

    private void merge(E[] lista, Integer l, Integer m, Integer r, String attribute, Integer type) throws Exception {
        int n1 = m - l + 1;
        int n2 = r - m;

        E[] L = (E[]) java.lang.reflect.Array.newInstance(lista.getClass().getComponentType(), n1);
        E[] R = (E[]) java.lang.reflect.Array.newInstance(lista.getClass().getComponentType(), n2);

        for (int i = 0; i < n1; i++) {
            L[i] = lista[l + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = lista[m + 1 + j];
        }

        int leftIndex = 0, rightIndex = 0;
        int k = l;

        while (leftIndex < n1 && rightIndex < n2) {
            boolean condicion = attributeCompare(attribute, L[leftIndex], R[rightIndex], type);
            if (condicion) {
                lista[k] = L[leftIndex];
                leftIndex++;
            } else {
                lista[k] = R[rightIndex];
                rightIndex++;
            }
            k++;
        }

        while (leftIndex < n1) {
            lista[k] = L[leftIndex];
            leftIndex++;
            k++;
        }

        while (rightIndex < n2) {
            lista[k] = R[rightIndex];
            rightIndex++;
            k++;
        }
    }
    
    public LinkedList<E> linearBinarySearch(E value) throws Exception {

        this.quickSort(1); 
    
        LinkedList<E> resultList = new LinkedList<>();
    
       
        int left = 0, right = this.getSize() - 1;
        int firstOccurrence = -1;
    
        while (left <= right) {
            int mid = (left + right) / 2;
            E midValue = this.get(mid);
    
            if (midValue.equals(value)) {
                if (mid == 0 || !this.get(mid - 1).equals(value)) {
                    firstOccurrence = mid;
                    break;
                } else {
                    right = mid - 1;
                }
            } else if (midValue.toString().compareTo(value.toString()) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    
        if (firstOccurrence == -1) {
            return resultList; 
        }
    
        for (int i = firstOccurrence; i < this.getSize(); i++) {
            E current = this.get(i);
            if (current.equals(value)) {
                resultList.add(current);
            } else {
                break;
            }
        }
    
        return resultList;
    }
    
}
