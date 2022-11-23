package domain.classes;

import domain.exceptions.InvalidExpression;
import domain.exceptions.InvalidWordFormat;

import java.util.LinkedList;

public abstract class Comparator<T> {

    protected LinkedList<T> list = new LinkedList<>();

//-----------------------------------------------
    public LinkedList<T> getList() {
        return list;
    }

    public abstract boolean compare (T t) throws InvalidWordFormat, InvalidExpression;

    public void addToList(T t) {
        list.add(t);
    }


}
